import {
    addDoc,
    collection,
    deleteDoc,
    doc,
    getDocs,
    increment,
    onSnapshot,
    orderBy,
    query,
    serverTimestamp,
    Timestamp,
    updateDoc,
    where
} from 'firebase/firestore';
import { useEffect, useState } from 'react';
import { db } from '../firebase';

// Types for our forum data structure
export interface Forum {
  id: string;
  title: string;
  description: string;
  question: string;
  creatorId: string;
  creatorName: string;
  createdAt: Timestamp;
  endTime?: Timestamp;
  isActive: boolean;
  upvotes: number;
  downvotes: number;
  commentCount: number;
  tags?: string[];
}

export interface Comment {
  id: string;
  forumId: string;
  userId: string;
  userName: string;
  content: string;
  createdAt: Timestamp;
  upvotes: number;
  downvotes: number;
  parentId?: string; // for nested replies
}

export interface Vote {
  id: string;
  userId: string;
  forumId?: string;
  commentId?: string;
  type: 'upvote' | 'downvote';
  createdAt: Timestamp;
}

export function useFirestore() {
  // Forum operations
  const createForum = async (forumData: Omit<Forum, 'id' | 'createdAt' | 'upvotes' | 'downvotes' | 'commentCount'>) => {
    try {
      const docRef = await addDoc(collection(db, 'forums'), {
        ...forumData,
        createdAt: serverTimestamp(),
        upvotes: 0,
        downvotes: 0,
        commentCount: 0,
      });
      return docRef.id;
    } catch (error) {
      console.error('Error creating forum:', error);
      throw error;
    }
  };

  const updateForum = async (forumId: string, updates: Partial<Forum>) => {
    try {
      const forumRef = doc(db, 'forums', forumId);
      await updateDoc(forumRef, updates);
    } catch (error) {
      console.error('Error updating forum:', error);
      throw error;
    }
  };

  const deleteForum = async (forumId: string) => {
    try {
      await deleteDoc(doc(db, 'forums', forumId));
    } catch (error) {
      console.error('Error deleting forum:', error);
      throw error;
    }
  };

  // Comment operations
  const createComment = async (commentData: Omit<Comment, 'id' | 'createdAt' | 'upvotes' | 'downvotes'>) => {
    try {
      const docRef = await addDoc(collection(db, 'comments'), {
        ...commentData,
        createdAt: serverTimestamp(),
        upvotes: 0,
        downvotes: 0,
      });
      
      // Update forum comment count
      const forumRef = doc(db, 'forums', commentData.forumId);
      await updateDoc(forumRef, {
        commentCount: increment(1)
      });
      
      return docRef.id;
    } catch (error) {
      console.error('Error creating comment:', error);
      throw error;
    }
  };

  // Voting operations
  const voteOnForum = async (forumId: string, userId: string, voteType: 'upvote' | 'downvote') => {
    try {
      // Check if user already voted
      const voteQuery = query(
        collection(db, 'votes'),
        where('forumId', '==', forumId),
        where('userId', '==', userId)
      );
      const existingVotes = await getDocs(voteQuery);

      // Remove existing vote if any
      if (!existingVotes.empty) {
        const existingVote = existingVotes.docs[0];
        const existingVoteData = existingVote.data() as Vote;
        
        // Update forum vote counts (remove old vote)
        const forumRef = doc(db, 'forums', forumId);
        if (existingVoteData.type === 'upvote') {
          await updateDoc(forumRef, { upvotes: increment(-1) });
        } else {
          await updateDoc(forumRef, { downvotes: increment(-1) });
        }
        
        // Delete old vote
        await deleteDoc(existingVote.ref);
        
        // If same vote type, just remove (toggle off)
        if (existingVoteData.type === voteType) {
          return;
        }
      }

      // Add new vote
      await addDoc(collection(db, 'votes'), {
        forumId,
        userId,
        type: voteType,
        createdAt: serverTimestamp(),
      });

      // Update forum vote counts
      const forumRef = doc(db, 'forums', forumId);
      if (voteType === 'upvote') {
        await updateDoc(forumRef, { upvotes: increment(1) });
      } else {
        await updateDoc(forumRef, { downvotes: increment(1) });
      }
    } catch (error) {
      console.error('Error voting on forum:', error);
      throw error;
    }
  };

  const voteOnComment = async (commentId: string, userId: string, voteType: 'upvote' | 'downvote') => {
    try {
      // Similar logic for comment voting
      const voteQuery = query(
        collection(db, 'votes'),
        where('commentId', '==', commentId),
        where('userId', '==', userId)
      );
      const existingVotes = await getDocs(voteQuery);

      if (!existingVotes.empty) {
        const existingVote = existingVotes.docs[0];
        const existingVoteData = existingVote.data() as Vote;
        
        const commentRef = doc(db, 'comments', commentId);
        if (existingVoteData.type === 'upvote') {
          await updateDoc(commentRef, { upvotes: increment(-1) });
        } else {
          await updateDoc(commentRef, { downvotes: increment(-1) });
        }
        
        await deleteDoc(existingVote.ref);
        
        if (existingVoteData.type === voteType) {
          return;
        }
      }

      await addDoc(collection(db, 'votes'), {
        commentId,
        userId,
        type: voteType,
        createdAt: serverTimestamp(),
      });

      const commentRef = doc(db, 'comments', commentId);
      if (voteType === 'upvote') {
        await updateDoc(commentRef, { upvotes: increment(1) });
      } else {
        await updateDoc(commentRef, { downvotes: increment(1) });
      }
    } catch (error) {
      console.error('Error voting on comment:', error);
      throw error;
    }
  };

  return {
    createForum,
    updateForum,
    deleteForum,
    createComment,
    voteOnForum,
    voteOnComment,
  };
}

// Custom hook for real-time forum data
export function useForums() {
  const [forums, setForums] = useState<Forum[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const forumsQuery = query(
      collection(db, 'forums'),
      where('isActive', '==', true),
      orderBy('createdAt', 'desc')
    );

    const unsubscribe = onSnapshot(forumsQuery, (snapshot) => {
      const forumsData = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      })) as Forum[];
      
      setForums(forumsData);
      setLoading(false);
    });

    return unsubscribe;
  }, []);

  return { forums, loading };
}

// Custom hook for forum comments
export function useComments(forumId: string) {
  const [comments, setComments] = useState<Comment[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!forumId) return;

    const commentsQuery = query(
      collection(db, 'comments'),
      where('forumId', '==', forumId),
      orderBy('createdAt', 'asc')
    );

    const unsubscribe = onSnapshot(commentsQuery, (snapshot) => {
      const commentsData = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      })) as Comment[];
      
      setComments(commentsData);
      setLoading(false);
    });

    return unsubscribe;
  }, [forumId]);

  return { comments, loading };
}
