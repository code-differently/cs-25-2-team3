import { useState } from "react";
import { Footer } from "../components/footer/footer";
import { NavBar } from "../components/navbar/navbar";
import { useFirestore } from "../hooks/useFirestore";
import { Timestamp } from "firebase/firestore";

export default function CreateForumPage() {
    const { createForum } = useFirestore();
    const [isLoading, setIsLoading] = useState(false);
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        question: "",
        category: "",
        timeLimit: 24, // hours
    });

    const categories = [
        "Technology",
        "Programming", 
        "Career Advice",
        "Education",
        "AI/Machine Learning",
        "Web Development",
        "Mobile Development",
        "General Discussion",
        "Other"
    ];

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            // Calculate end time based on time limit
            const endTime = new Date();
            endTime.setHours(endTime.getHours() + formData.timeLimit);

            const forumData = {
                title: formData.title,
                description: formData.description,
                question: formData.question,
                creatorId: "anonymous", // TODO: Replace with actual user ID when auth is implemented
                creatorName: "Anonymous User", // TODO: Replace with actual username
                endTime: Timestamp.fromDate(endTime),
                isActive: true,
                isAdminDeleted: false,
                tags: formData.category ? [formData.category] : [],
                messages: [""],
                summary: "",
                actionRoadmap: [""],
            };

            const forumId = await createForum(forumData);
            
            // Redirect to the forums page after successful creation
            window.location.href = '/forums';
        } catch (error) {
            console.error("Failed to create forum:", error);
            alert("Failed to create forum. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'timeLimit' ? parseInt(value) : value
        }));
    };

    return (
        <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">
            <NavBar />

            <main className="flex-1 px-8 py-6">
                <div className="max-w-2xl mx-auto">
                    <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-8">Create New Forum</h1>
                    
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div>
                            <label htmlFor="title" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                                Forum Title *
                            </label>
                            <input
                                type="text"
                                id="title"
                                name="title"
                                required
                                value={formData.title}
                                onChange={handleInputChange}
                                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                                placeholder="Enter a clear, descriptive title for your forum"
                            />
                        </div>

                        <div>
                            <label htmlFor="description" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                                Description *
                            </label>
                            <textarea
                                id="description"
                                name="description"
                                required
                                value={formData.description}
                                onChange={handleInputChange}
                                rows={4}
                                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                                placeholder="Provide a detailed description of what this forum is about"
                            />
                        </div>

                        <div>
                            <label htmlFor="question" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                                Main Question *
                            </label>
                            <textarea
                                id="question"
                                name="question"
                                required
                                value={formData.question}
                                onChange={handleInputChange}
                                rows={3}
                                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                                placeholder="What specific question do you want the community to answer?"
                            />
                        </div>

                        <div>
                            <label htmlFor="category" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                                Category
                            </label>
                            <select
                                id="category"
                                name="category"
                                value={formData.category}
                                onChange={handleInputChange}
                                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                            >
                                <option value="">Select a category</option>
                                {categories.map(category => (
                                    <option key={category} value={category}>
                                        {category}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label htmlFor="timeLimit" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                                Time Limit (hours) *
                            </label>
                            <select
                                id="timeLimit"
                                name="timeLimit"
                                required
                                value={formData.timeLimit}
                                onChange={handleInputChange}
                                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                            >
                                <option value={1}>1 hour</option>
                                <option value={6}>6 hours</option>
                                <option value={12}>12 hours</option>
                                <option value={24}>24 hours (1 day)</option>
                                <option value={48}>48 hours (2 days)</option>
                                <option value={72}>72 hours (3 days)</option>
                                <option value={168}>1 week</option>
                            </select>
                            <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
                                The forum will automatically close after this time period
                            </p>
                        </div>

                        <div className="flex gap-4 pt-4">
                            <button
                                type="submit"
                                disabled={isLoading}
                                className="flex-1 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-semibold py-2 px-4 rounded-lg transition-colors"
                            >
                                {isLoading ? "Creating..." : "Create Forum"}
                            </button>
                            <button
                                type="button"
                                onClick={() => window.history.back()}
                                className="px-6 py-2 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
                            >
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            </main>

            <Footer />
        </div>
    );
}
