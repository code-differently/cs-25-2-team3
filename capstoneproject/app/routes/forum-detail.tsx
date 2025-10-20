import { useState } from "react";
import { Link, useParams } from "react-router";
import { Footer } from "../components/footer/footer";
import { NavBar } from "../components/navbar/navbar";
import { useComments, useFirestore, useForum } from "../hooks/useFirestore";

export function meta() {
    return [
        { title: "Forum Discussion" },
        { name: "description", content: "Join the discussion in this forum." },
    ];
}

export default function ForumDetail() {
    const { forumId } = useParams();
    const { createComment } = useFirestore();
    const { comments, loading: commentsLoading, error: commentsError } = useComments(forumId || "");
    const { forum, loading, error } = useForum(forumId || "");
    
    const [newMessage, setNewMessage] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

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
                    </div>

                    {/* Message Form */}
                    {!isExpired && (
                        <div className="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 p-6 mb-6">
                            <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Add Your Response</h3>
                            <form onSubmit={handleSubmitMessage}>
                                <textarea
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    placeholder="Share your thoughts, suggestions, or answer to the question..."
                                    className="w-full px-4 py-3 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white resize-none"
                                    rows={4}
                                    required
                                />
                                <div className="flex justify-end mt-4">
                                    <button
                                        type="submit"
                                        disabled={isSubmitting || !newMessage.trim()}
                                        className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-semibold py-2 px-6 rounded-lg transition-colors"
                                    >
                                        {isSubmitting ? "Posting..." : "Post Response"}
                                    </button>
                                </div>
                            </form>
                        </div>
                    )}

                    {/* Messages */}
                    <div className="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 p-6">
                        <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
                            Responses ({comments.length})
                        </h3>

                        {commentsError ? (
                            <div className="text-center py-8">
                                <p className="text-red-600 dark:text-red-400 mb-2">{commentsError}</p>
                                <button 
                                    onClick={() => window.location.reload()} 
                                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm"
                                >
                                    Retry
                                </button>
                            </div>
                        ) : commentsLoading ? (
                            <div className="text-center py-8">
                                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
                                <p className="mt-2 text-gray-600 dark:text-gray-400">Loading responses...</p>
                            </div>
                        ) : comments.length === 0 ? (
                            <div className="text-center py-8">
                                <p className="text-gray-600 dark:text-gray-400">No responses yet. Be the first to contribute!</p>
                            </div>
                        ) : (
                            <div className="space-y-4">
                                {comments.map((comment) => {
                                    const commentDate = comment.createdAt?.toDate();
                                    return (
                                        <div key={comment.id} className="border-l-4 border-blue-200 dark:border-blue-700 pl-4 py-2">
                                            <div className="flex justify-between items-start mb-2">
                                                <span className="font-medium text-gray-900 dark:text-white">
                                                    {comment.userName}
                                                </span>
                                                <span className="text-sm text-gray-500 dark:text-gray-400">
                                                    {commentDate ? commentDate.toLocaleString() : 'Unknown date'}
                                                </span>
                                            </div>
                                            <p className="text-gray-700 dark:text-gray-300 mb-2">
                                                {comment.content}
                                            </p>
                                            <div className="flex gap-4 text-sm text-gray-500 dark:text-gray-400">
                                                <span>↑ {comment.upvotes || 0}</span>
                                                <span>↓ {comment.downvotes || 0}</span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        )}
                    </div>
                </div>
            </main>

            <Footer />
        </div>
    );
}
