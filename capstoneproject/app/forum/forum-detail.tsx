import { onAuthStateChanged, type User } from "firebase/auth";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router";
import { Footer } from "../components/footer/footer";
import { NavBar } from "../components/navbar/navbar";
import { firebaseAuth } from "../firebase";
import { useComments, useFirestore, useForum } from "../hooks/useFirestore";
import TeaModal from "../message/components/TeaModal";

export default function ForumDetailPage() {
    const { forumId } = useParams();
    const { createComment } = useFirestore();
    const { comments, loading: commentsLoading, error: commentsError } = useComments(forumId || "");
    const { forum, loading, error } = useForum(forumId || "");
    
    const [newMessage, setNewMessage] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [showTeaModal, setShowTeaModal] = useState(false);
    const [user, setCurrentUser] = useState<any>(null);

    // Set up auth state listener once on mount
    useEffect(() => {
        const unsubscribe = onAuthStateChanged(firebaseAuth, (user) => {
        setCurrentUser(user);
        if (!user) {
            // User is redirected to login page
            window.location.href = '/login';
        }
        });
        return () => unsubscribe();
    }, []);

    const handleSubmitMessage = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!newMessage.trim() || !forumId || !forum) return;

        
        // Check if forum is still active
        const endTime = forum.endTime?.toDate();
        const isExpired = endTime && new Date() > endTime;
        
        if (isExpired) {
            alert("This forum has closed and no longer accepts new messages.");
            return;
        }

        setIsSubmitting(true);
        
        try {
            await createComment({
                forumId: forumId,
                userId: "anonymous", // TODO: Replace with actual user ID
                userName: "Anonymous User", // TODO: Replace with actual username
                content: newMessage.trim(),
            });
            
            setNewMessage("");
        } catch (error) {
            console.error("Failed to post message:", error);
            alert("Failed to post message. Please try again.");
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">
                <NavBar />
                <main className="flex-1 flex items-center justify-center">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                        <p className="mt-4 text-gray-600 dark:text-gray-400">Loading forum...</p>
                    </div>
                </main>
                <Footer />
            </div>
        );
    }

    if (error || !forum) {
        return (
            <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">
                <NavBar />
                <main className="flex-1 flex items-center justify-center">
                    <div className="text-center">
                        <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
                            {error || "Forum Not Found"}
                        </h2>
                        <p className="text-gray-600 dark:text-gray-400 mb-4">
                            {error === "Failed to load forum" 
                                ? "There was an error loading the forum. Please try again."
                                : "The forum you're looking for doesn't exist or has been deleted."
                            }
                        </p>
                        <div className="flex gap-4 justify-center">
                            <Link
                                to="/forums"
                                className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors"
                            >
                                Back to Forums
                            </Link>
                            {error === "Failed to load forum" && (
                                <button
                                    onClick={() => window.location.reload()}
                                    className="bg-gray-600 hover:bg-gray-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors"
                                >
                                    Retry
                                </button>
                            )}
                        </div>
                    </div>
                </main>
                <Footer />
            </div>
        );
    }

    const createdAt = forum.createdAt?.toDate();
    const endTime = forum.endTime?.toDate();
    const isExpired = endTime && new Date() > endTime;
    const status = isExpired ? "Closed" : "Open";

    return (
        <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">
            <NavBar />

            <main className="flex-1 px-8 py-6">
                <div className="max-w-4xl mx-auto">
                    {/* Breadcrumb */}
                    <nav className="mb-6">
                        <Link
                            to="/forums"
                            className="text-blue-600 hover:text-blue-700 dark:text-blue-400 dark:hover:text-blue-300"
                        >
                            ← Back to Forums
                        </Link>
                    </nav>

                    {/* Forum Header */}
                    <div className="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 p-6 mb-6">
                        <div className="flex justify-between items-start mb-4">
                            <div className="flex-1">
                                <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
                                    {forum.title}
                                </h1>
                                {forum.tags && forum.tags.length > 0 && (
                                    <div className="flex gap-2 mb-3">
                                        {forum.tags.map((tag, index) => (
                                            <span key={index} className="text-xs px-2 py-1 bg-blue-100 text-blue-700 rounded-full">
                                                {tag}
                                            </span>
                                        ))}
                                    </div>
                                )}
                            </div>
                            <span className={`text-sm px-3 py-1 rounded-full font-semibold ml-4 ${
                                status === "Open" ? "bg-green-100 text-green-700" : "bg-gray-200 text-gray-600"
                            }`}>
                                {status}
                            </span>
                        </div>

                        <p className="text-gray-700 dark:text-gray-300 mb-4 text-lg">
                            {forum.description}
                        </p>

                        <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-4 mb-4">
                            <h3 className="font-semibold text-blue-900 dark:text-blue-100 mb-2">Question:</h3>
                            <p className="text-blue-800 dark:text-blue-200">{forum.question}</p>
                        </div>

                        <div className="flex justify-between items-center text-sm text-gray-500 dark:text-gray-400">
                            <div>
                                Created by {forum.creatorName} • {createdAt ? createdAt.toLocaleDateString() : 'Unknown date'}
                            </div>
                            <div className="flex gap-4">
                                <span>{forum.commentCount || 0} responses</span>
                                <span>↑ {forum.upvotes || 0}</span>
                                <span>↓ {forum.downvotes || 0}</span>
                            </div>
                        </div>

                        {endTime && (
                            <div className="mt-3 text-sm text-gray-600 dark:text-gray-400">
                                {isExpired ? 'Forum closed' : 'Forum closes'} on {endTime.toLocaleDateString()} at {endTime.toLocaleTimeString()}
                            </div>
                        )}
                        <button
                          onClick={() => {
                            if (comments.length === 0) {
                              console.log("[TeaModal] Disabled — no messages to analyze");
                              return;
                            }
                            setShowTeaModal(true);
                          }}
                          disabled={comments.length === 0}
                          className={`bg-pink-600 hover:bg-pink-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors mt-4 ml-4 ${
                            comments.length === 0 ? "opacity-50 cursor-not-allowed" : ""
                          }`}
                        >
                          What's Tea (🍵)
                        </button>
                        {comments.length === 0 && (
                          <p className="text-sm text-gray-500 mt-2">
                            Post at least one message before spilling the tea ☕
                          </p>
                        )}
                    </div>

                    {/* Forum Messages */}
                    <div className="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 p-6">
                        <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
                            Messages
                        </h2>

                        {commentsLoading ? (
                            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                        ) : commentsError ? (
                            <p className="text-red-500 text-center">
                                Failed to load messages. Please try again later.
                            </p>
                        ) : comments.length === 0 ? (
                            <p className="text-gray-500 text-center">
                                No messages yet. Be the first to respond!
                            </p>
                        ) : (
                            <div className="space-y-4">
                                {comments.map((comment) => (
                                    <div key={comment.id} className="p-4 bg-gray-50 dark:bg-gray-700 rounded-lg border border-gray-200 dark:border-gray-600">
                                        <div className="flex items-center justify-between mb-2">
                                            <div className="flex items-center">
                                                <div className="w-10 h-10 rounded-full bg-gray-300 dark:bg-gray-600 mr-3"></div>
                                                <div>
                                                    <p className="text-sm font-semibold text-gray-900 dark:text-white">
                                                        {comment.userName}
                                                    </p>
                                                    <p className="text-xs text-gray-500 dark:text-gray-400">
                                                        {comment.createdAt?.toDate().toLocaleString()}
                                                    </p>
                                                </div>
                                            </div>
                                            <div className="flex gap-2">
                                                <button className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300 text-xs">
                                                    Reply
                                                </button>
                                                <button className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300 text-xs">
                                                    Upvote
                                                </button>
                                                <button className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300 text-xs">
                                                    Downvote
                                                </button>
                                            </div>
                                        </div>
                                        <p className="text-gray-700 dark:text-gray-300 text-sm">
                                            {comment.content}
                                        </p>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    {/* New Message Form */}
                    {!isExpired && (
                        <div className="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 p-6 mt-6">
                            <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
                                {comments.length > 0 ? "Reply to this forum" : "Be the first to comment"}
                            </h2>

                            <form onSubmit={handleSubmitMessage} className="flex flex-col gap-4">
                                <textarea
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    className="p-4 text-gray-900 dark:text-white bg-gray-50 dark:bg-gray-700 rounded-lg border border-gray-200 dark:border-gray-600 focus:ring-2 focus:ring-blue-600 dark:focus:ring-blue-500 transition-all"
                                    rows={4}
                                    placeholder="Write your message here..."
                                    required
                                ></textarea>
                                <button
                                    type="submit"
                                    disabled={isSubmitting}
                                    className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors disabled:opacity-50"
                                >
                                    {isSubmitting ? "Sending..." : "Send Message"}
                                </button>
                            </form>
                        </div>
                    )}
                </div>
            </main>

            <Footer />

            {showTeaModal && (
  <TeaModal
    onClose={() => setShowTeaModal(false)}
    messages={comments}
    forumId={forumId}
    forumTitle={forum?.title || ""}
    forumDescription={forum?.description}
    forumQuestion={forum?.question}
    category={forum?.tags?.[0]}
  />
)}
        </div>
    );
}
function setCurrentUser(user: User | null) {
    throw new Error("Function not implemented.");
}

