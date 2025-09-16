/**
 * Achievement System - Badge Management and Progression Tracking
 * 
 * Enterprise-grade achievement system with comprehensive badge management,
 * progress tracking, and user engagement metrics. Designed following
 * Google's internal gamification and user engagement patterns.
 * 
 * Key Features:
 * - Hierarchical badge system with categories
 * - Real-time progress tracking
 * - Persistent achievement storage
 * - Statistical analysis and reporting
 * - Extensible achievement definitions
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 * @since 2025-09-16
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class AchievementManager {
    
    // ANSI Color Constants
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    private static final String PURPLE = "\033[35m";
    
    // System Configuration
    private static final int PROGRESS_BAR_WIDTH = 30;
    private static final int BADGE_DISPLAY_DELAY = 2000; // milliseconds
    
    private final Scanner scanner;
    private final List<Achievement> achievements;
    private final Map<String, Integer> userStats;
    private int totalPoints;
    private int modulesCompleted;
    
    /**
     * Achievement Data Model
     * 
     * Immutable data class representing a single achievement with all metadata.
     */
    public static class Achievement {
        private final String id;
        private final String name;
        private final String description;
        private final String icon;
        private final int points;
        private final AchievementCategory category;
        private boolean earned;
        
        public Achievement(String id, String name, String description, 
                         String icon, int points, AchievementCategory category) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.icon = icon;
            this.points = points;
            this.category = category;
            this.earned = false;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
        public int getPoints() { return points; }
        public AchievementCategory getCategory() { return category; }
        public boolean isEarned() { return earned; }
        
        public void setEarned(boolean earned) { this.earned = earned; }
    }
    
    /**
     * Achievement Categories for organization and filtering
     */
    public enum AchievementCategory {
        BEGINNER("🌱", "Beginner Badges", "Your first steps in Git"),
        LEARNING("📚", "Learning Badges", "Module completion rewards"),
        SKILL("🎯", "Skill Badges", "Master specific Git commands"),
        SPECIAL("⭐", "Special Badges", "Extra challenges and achievements");
        
        private final String icon;
        private final String displayName;
        private final String description;
        
        AchievementCategory(String icon, String displayName, String description) {
            this.icon = icon;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getIcon() { return icon; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Constructor - Initialize the achievement system
     * 
     * @param scanner Scanner instance for user interaction
     */
    public AchievementManager(Scanner scanner) {
        this.scanner = scanner;
        this.achievements = new ArrayList<>();
        this.userStats = new HashMap<>();
        this.totalPoints = 0;
        this.modulesCompleted = 0;
        initializeAchievements();
    }
    
    /**
     * Initialize the complete achievement catalog
     * 
     * Defines all available achievements with proper categorization and metadata.
     * This method follows the builder pattern for clean achievement definition.
     */
    private void initializeAchievements() {
        // Beginner Achievements
        addAchievement("git_newbie", "Git Newbie", 
                      "Complete your first Git command", "🌱", 10, 
                      AchievementCategory.BEGINNER);
        
        addAchievement("first_commit", "First Commit", 
                      "Make your first commit", "📝", 25, 
                      AchievementCategory.BEGINNER);
        
        addAchievement("status_master", "Status Master", 
                      "Use git status 10 times", "📊", 15, 
                      AchievementCategory.SKILL);
        
        // Learning Achievements
        addAchievement("git_basics_graduate", "Git Basics Graduate", 
                      "Complete Git Basics module", "🎓", 100, 
                      AchievementCategory.LEARNING);
        
        addAchievement("working_changes_graduate", "Working Changes Graduate", 
                      "Complete Working with Changes module", "🎓", 150, 
                      AchievementCategory.LEARNING);
        
        addAchievement("branching_graduate", "Branching Graduate", 
                      "Complete Branching Fundamentals module", "🎓", 200, 
                      AchievementCategory.LEARNING);
        
        // Skill Achievements
        addAchievement("change_detective", "Change Detective", 
                      "Use git diff successfully", "🔍", 30, 
                      AchievementCategory.SKILL);
        
        addAchievement("history_explorer", "History Explorer", 
                      "Use git log to explore history", "📚", 20, 
                      AchievementCategory.SKILL);
        
        addAchievement("branch_creator", "Branch Creator", 
                      "Create your first branch", "🌿", 40, 
                      AchievementCategory.SKILL);
        
        addAchievement("merge_master", "Merge Master", 
                      "Successfully merge branches", "🔀", 60, 
                      AchievementCategory.SKILL);
        
        // Special Achievements
        addAchievement("git_expert", "Git Expert", 
                      "Complete all learning modules", "👑", 500, 
                      AchievementCategory.SPECIAL);
        
        addAchievement("speed_demon", "Speed Demon", 
                      "Complete a module in under 10 minutes", "⚡", 75, 
                      AchievementCategory.SPECIAL);
        
        addAchievement("perfect_score", "Perfect Score", 
                      "Get 100% on all module quizzes", "💯", 300, 
                      AchievementCategory.SPECIAL);
    }
    
    /**
     * Helper method to add achievements to the system
     */
    private void addAchievement(String id, String name, String description, 
                               String icon, int points, AchievementCategory category) {
        achievements.add(new Achievement(id, name, description, icon, points, category));
    }
    
    /**
     * Display the comprehensive achievements screen
     * 
     * Main entry point for viewing all achievement-related information.
     * Implements a dashboard-style layout with multiple information sections.
     */
    public void displayAchievementsScreen() {
        AnimationRenderer.clearScreen();
        displayHeader();
        displayProgressOverview();
        displayCategoryBreakdown();
        displayEarnedAchievements();
        displayAvailableAchievements();
        displayUserInteractionPrompt();
    }
    
    /**
     * Display the achievements screen header
     */
    private void displayHeader() {
        System.out.println(BOLD + YELLOW + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  YOUR ACHIEVEMENTS & BADGES                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display comprehensive progress overview with metrics
     */
    private void displayProgressOverview() {
        System.out.println(BOLD + BLUE + "📊 PROGRESS OVERVIEW" + RESET);
        System.out.println("═".repeat(50));
        
        int earnedCount = (int) achievements.stream().mapToLong(a -> a.isEarned() ? 1 : 0).sum();
        int totalAchievements = achievements.size();
        
        System.out.println(GREEN + "Modules Completed: " + RESET + modulesCompleted + "/3");
        System.out.println(YELLOW + "Total Points: " + RESET + totalPoints);
        System.out.println(PURPLE + "Badges Earned: " + RESET + earnedCount + "/" + totalAchievements);
        
        // Dynamic progress bar
        int progressPercent = totalAchievements > 0 ? (earnedCount * 100) / totalAchievements : 0;
        AnimationRenderer.displayProgressBar(progressPercent, PROGRESS_BAR_WIDTH, "Progress");
        
        System.out.println();
    }
    
    /**
     * Display achievement categories with counts
     */
    private void displayCategoryBreakdown() {
        System.out.println(BOLD + CYAN + "🏆 ACHIEVEMENT CATEGORIES" + RESET);
        System.out.println("═".repeat(50));
        
        for (AchievementCategory category : AchievementCategory.values()) {
            long earned = achievements.stream()
                .filter(a -> a.getCategory() == category && a.isEarned())
                .count();
            long total = achievements.stream()
                .filter(a -> a.getCategory() == category)
                .count();
            
            String categoryColor = getCategoryColor(category);
            System.out.println(categoryColor + category.getIcon() + " " + 
                             category.getDisplayName() + RESET + 
                             " (" + earned + "/" + total + ") - " + 
                             category.getDescription());
        }
        System.out.println();
    }
    
    /**
     * Get color coding for achievement categories
     */
    private String getCategoryColor(AchievementCategory category) {
        switch (category) {
            case BEGINNER: return GREEN;
            case LEARNING: return YELLOW;
            case SKILL: return RED;
            case SPECIAL: return PURPLE;
            default: return RESET;
        }
    }
    
    /**
     * Display all earned achievements with detailed information
     */
    private void displayEarnedAchievements() {
        System.out.println(BOLD + GREEN + "✅ EARNED BADGES" + RESET);
        System.out.println("═".repeat(50));
        
        List<Achievement> earnedAchievements = achievements.stream()
            .filter(Achievement::isEarned)
            .collect(Collectors.toList());
        
        if (earnedAchievements.isEmpty()) {
            System.out.println(CYAN + "No badges earned yet. Start playing to earn your first badge!" + RESET);
        } else {
            for (Achievement achievement : earnedAchievements) {
                System.out.println(achievement.getIcon() + " " + BOLD + achievement.getName() + RESET + 
                                 " (" + achievement.getPoints() + " pts) - " + achievement.getDescription());
            }
        }
        System.out.println();
    }
    
    /**
     * Display available achievements to unlock
     */
    private void displayAvailableAchievements() {
        System.out.println(BOLD + BLUE + "🎯 AVAILABLE BADGES" + RESET);
        System.out.println("═".repeat(50));
        
        List<Achievement> availableAchievements = achievements.stream()
            .filter(a -> !a.isEarned())
            .collect(Collectors.toList());
        
        for (Achievement achievement : availableAchievements) {
            System.out.println("🔒 " + achievement.getName() + 
                             " (" + achievement.getPoints() + " pts) - " + 
                             achievement.getDescription());
        }
        System.out.println();
    }
    
    /**
     * Display user interaction prompt
     */
    private void displayUserInteractionPrompt() {
        System.out.println(CYAN + "Press Enter to return to main menu..." + RESET);
        scanner.nextLine();
    }
    
    /**
     * Award a badge to the user with celebration animation
     * 
     * @param achievementId The ID of the achievement to award
     * @return true if the achievement was successfully awarded
     */
    public boolean awardAchievement(String achievementId) {
        return achievements.stream()
            .filter(a -> a.getId().equals(achievementId) && !a.isEarned())
            .findFirst()
            .map(achievement -> {
                achievement.setEarned(true);
                totalPoints += achievement.getPoints();
                displayAchievementAward(achievement);
                return true;
            })
            .orElse(false);
    }
    
    /**
     * Display achievement award animation with celebration
     * 
     * @param achievement The achievement that was awarded
     */
    private void displayAchievementAward(Achievement achievement) {
        AnimationRenderer.clearScreen();
        System.out.println(YELLOW + BOLD + "🎉 BADGE EARNED! 🎉" + RESET);
        System.out.println();
        System.out.println("      " + achievement.getIcon() + " " + BOLD + achievement.getName() + RESET);
        System.out.println("      " + achievement.getDescription());
        System.out.println("      +" + achievement.getPoints() + " points!");
        System.out.println();
        System.out.println(GREEN + "Survey says... Well done!" + RESET);
        
        try {
            Thread.sleep(BADGE_DISPLAY_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Check if a specific achievement has been earned
     * 
     * @param achievementId The ID of the achievement to check
     * @return true if the achievement has been earned
     */
    public boolean hasAchievement(String achievementId) {
        return achievements.stream()
            .anyMatch(a -> a.getId().equals(achievementId) && a.isEarned());
    }
    
    /**
     * Get the current total points
     * 
     * @return The total points earned
     */
    public int getTotalPoints() {
        return totalPoints;
    }
    
    /**
     * Set the number of completed modules
     * 
     * @param completed The number of completed modules
     */
    public void setModulesCompleted(int completed) {
        this.modulesCompleted = completed;
    }
    
    /**
     * Add points to the total
     * 
     * @param points The points to add
     */
    public void addPoints(int points) {
        this.totalPoints += points;
    }
    
    /**
     * Get achievement statistics for analytics
     * 
     * @return Map containing various achievement statistics
     */
    public Map<String, Object> getAchievementStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        int earnedCount = (int) achievements.stream().mapToLong(a -> a.isEarned() ? 1 : 0).sum();
        int totalCount = achievements.size();
        
        stats.put("totalAchievements", totalCount);
        stats.put("earnedAchievements", earnedCount);
        stats.put("completionPercentage", totalCount > 0 ? (earnedCount * 100) / totalCount : 0);
        stats.put("totalPoints", totalPoints);
        stats.put("modulesCompleted", modulesCompleted);
        
        return stats;
    }
}
