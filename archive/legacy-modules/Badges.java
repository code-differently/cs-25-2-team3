import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Badges class for handling achievements and progression system
 * 
 * This class manages all badge tracking, achievement unlocking,
 * and progress visualization for the Git training application.
 */
public class Badges {
    
    // ANSI color codes for Family Feud theme
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    private static final String PURPLE = "\033[35m";
    
    private Scanner scanner;
    private List<Badge> earnedBadges;
    private int totalPoints;
    private int modulesCompleted;
    
    /**
     * Inner class to represent a Badge
     */
    private static class Badge {
        String name;
        String description;
        String icon;
        int points;
        boolean earned;
        
        Badge(String name, String description, String icon, int points) {
            this.name = name;
            this.description = description;
            this.icon = icon;
            this.points = points;
            this.earned = false;
        }
    }
    
    /**
     * Constructor
     * @param scanner Scanner instance for user input
     */
    public Badges(Scanner scanner) {
        this.scanner = scanner;
        this.earnedBadges = new ArrayList<>();
        this.totalPoints = 0;
        this.modulesCompleted = 0;
        initializeBadges();
    }
    
    /**
     * Initialize all available badges
     */
    private void initializeBadges() {
        // Add all available badges to the system
        earnedBadges.add(new Badge("Git Newbie", "Complete your first Git command", "🌱", 10));
        earnedBadges.add(new Badge("First Commit", "Make your first commit", "📝", 25));
        earnedBadges.add(new Badge("Status Master", "Use git status 10 times", "📊", 15));
        earnedBadges.add(new Badge("Git Basics Graduate", "Complete Git Basics module", "🎓", 100));
        earnedBadges.add(new Badge("Change Detective", "Use git diff successfully", "🔍", 30));
        earnedBadges.add(new Badge("History Explorer", "Use git log to explore history", "📚", 20));
        earnedBadges.add(new Badge("Working Changes Graduate", "Complete Working with Changes module", "🎓", 150));
        earnedBadges.add(new Badge("Branch Creator", "Create your first branch", "🌿", 40));
        earnedBadges.add(new Badge("Merge Master", "Successfully merge branches", "🔀", 60));
        earnedBadges.add(new Badge("Branching Graduate", "Complete Branching Fundamentals module", "🎓", 200));
        earnedBadges.add(new Badge("Git Expert", "Complete all learning modules", "👑", 500));
        earnedBadges.add(new Badge("Speed Demon", "Complete a module in under 10 minutes", "⚡", 75));
        earnedBadges.add(new Badge("Perfect Score", "Get 100% on all module quizzes", "💯", 300));
    }
    
    /**
     * Handle the BADGES option - View achievements
     * Implements User Story B7: Achievement and progression systems
     */
    public void displayBadgesScreen() {
        Animation.clearScreen();
        displayHeader();
        displayProgressOverview();
        displayBadgeCategories();
        displayEarnedBadges();
        displayAvailableBadges();
        
        System.out.println();
        System.out.println(CYAN + "Press Enter to return to main menu..." + RESET);
        scanner.nextLine();
    }
    
    /**
     * Display the badges screen header
     */
    private void displayHeader() {
        System.out.println(BOLD + YELLOW + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  YOUR ACHIEVEMENTS & BADGES                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display overall progress overview
     */
    private void displayProgressOverview() {
        System.out.println(BOLD + BLUE + "📊 PROGRESS OVERVIEW" + RESET);
        System.out.println("═".repeat(50));
        
        int earnedCount = (int) earnedBadges.stream().mapToLong(b -> b.earned ? 1 : 0).sum();
        int totalBadges = earnedBadges.size();
        
        System.out.println(GREEN + "Modules Completed: " + RESET + modulesCompleted + "/3");
        System.out.println(YELLOW + "Total Points: " + RESET + totalPoints);
        System.out.println(PURPLE + "Badges Earned: " + RESET + earnedCount + "/" + totalBadges);
        
        // Progress bar
        int progressPercent = totalBadges > 0 ? (earnedCount * 100) / totalBadges : 0;
        displayProgressBar(progressPercent);
        
        System.out.println();
    }
    
    /**
     * Display a progress bar
     * @param percent The percentage to display (0-100)
     */
    private void displayProgressBar(int percent) {
        int barLength = 30;
        int filledLength = (percent * barLength) / 100;
        
        System.out.print("Progress: [");
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                System.out.print(GREEN + "█" + RESET);
            } else {
                System.out.print("░");
            }
        }
        System.out.println("] " + percent + "%");
    }
    
    /**
     * Display badge categories
     */
    private void displayBadgeCategories() {
        System.out.println(BOLD + CYAN + "🏆 BADGE CATEGORIES" + RESET);
        System.out.println("═".repeat(50));
        System.out.println(GREEN + "🌱 Beginner Badges" + RESET + "    - Your first steps in Git");
        System.out.println(YELLOW + "📚 Learning Badges" + RESET + "    - Module completion rewards");
        System.out.println(RED + "🎯 Skill Badges" + RESET + "      - Master specific Git commands");
        System.out.println(PURPLE + "⭐ Special Badges" + RESET + "    - Extra challenges and achievements");
        System.out.println();
    }
    
    /**
     * Display earned badges
     */
    private void displayEarnedBadges() {
        System.out.println(BOLD + GREEN + "✅ EARNED BADGES" + RESET);
        System.out.println("═".repeat(50));
        
        boolean hasEarnedBadges = false;
        for (Badge badge : earnedBadges) {
            if (badge.earned) {
                System.out.println(badge.icon + " " + BOLD + badge.name + RESET + 
                                 " (" + badge.points + " pts) - " + badge.description);
                hasEarnedBadges = true;
            }
        }
        
        if (!hasEarnedBadges) {
            System.out.println(CYAN + "No badges earned yet. Start playing to earn your first badge!" + RESET);
        }
        System.out.println();
    }
    
    /**
     * Display available badges to unlock
     */
    private void displayAvailableBadges() {
        System.out.println(BOLD + BLUE + "🎯 AVAILABLE BADGES" + RESET);
        System.out.println("═".repeat(50));
        
        for (Badge badge : earnedBadges) {
            if (!badge.earned) {
                System.out.println("🔒 " + badge.name + " (" + badge.points + " pts) - " + badge.description);
            }
        }
    }
    
    /**
     * Award a badge to the user
     * @param badgeName The name of the badge to award
     */
    public void awardBadge(String badgeName) {
        for (Badge badge : earnedBadges) {
            if (badge.name.equals(badgeName) && !badge.earned) {
                badge.earned = true;
                totalPoints += badge.points;
                displayBadgeAward(badge);
                break;
            }
        }
    }
    
    /**
     * Display badge award animation
     * @param badge The badge that was awarded
     */
    private void displayBadgeAward(Badge badge) {
        Animation.clearScreen();
        System.out.println(YELLOW + BOLD + "🎉 BADGE EARNED! 🎉" + RESET);
        System.out.println();
        System.out.println("      " + badge.icon + " " + BOLD + badge.name + RESET);
        System.out.println("      " + badge.description);
        System.out.println("      +" + badge.points + " points!");
        System.out.println();
        System.out.println(GREEN + "Survey says... Well done!" + RESET);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Check if a specific badge has been earned
     * @param badgeName The name of the badge to check
     * @return true if the badge has been earned
     */
    public boolean hasBadge(String badgeName) {
        return earnedBadges.stream()
                .anyMatch(badge -> badge.name.equals(badgeName) && badge.earned);
    }
    
    /**
     * Get the current total points
     * @return The total points earned
     */
    public int getTotalPoints() {
        return totalPoints;
    }
    
    /**
     * Set the number of completed modules
     * @param completed The number of completed modules
     */
    public void setModulesCompleted(int completed) {
        this.modulesCompleted = completed;
    }
    
    /**
     * Add points to the total
     * @param points The points to add
     */
    public void addPoints(int points) {
        this.totalPoints += points;
    }
}
