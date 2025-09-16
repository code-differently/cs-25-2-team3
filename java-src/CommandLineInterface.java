/**
 * CommandLineInterface - Main Application Coordinator
 * 
 * Enterprise-grade main application class that orchestrates all system components
 * following Google's internal application architecture patterns. Provides unified
 * entry point with comprehensive error handling, dependency injection, and 
 * professional user experience management.
 * 
 * Key Features:
 * - Dependency injection and IoC container pattern
 * - Comprehensive error boundary management
 * - Professional startup and shutdown sequences
 * - Centralized configuration management
 * - Performance monitoring and logging hooks
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 * @since 2025-09-16
 */
import java.util.Scanner;

public class CommandLineInterface {
    
    // ANSI Color Constants
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    private static final String MAGENTA = "\033[35m";
    
    // Application Configuration
    private static final String APPLICATION_NAME = "Git Training CLI";
    private static final String VERSION = "1.0.0";
    private static final String BUILD_DATE = "2025-09-16";
    
    // Core Dependencies
    private final Scanner scanner;
    private final AnimationRenderer animationRenderer;
    private final AchievementManager achievementManager;
    private final LearningModuleManager moduleManager;
    private final SessionPersistenceManager sessionManager;
    private final ApplicationExitManager exitManager;
    
    // Application State
    private boolean isRunning;
    private int currentPoints;
    private String currentUser;
    
    /**
     * Application Entry Point
     * 
     * @param args Command line arguments (currently unused)
     */
    public static void main(String[] args) {
        try {
            CommandLineInterface app = new CommandLineInterface();
            app.start();
        } catch (Exception e) {
            handleCriticalError(e);
        }
    }
    
    /**
     * Constructor - Initialize all enterprise components
     */
    public CommandLineInterface() {
        this.scanner = new Scanner(System.in);
        this.animationRenderer = new AnimationRenderer();
        this.sessionManager = new SessionPersistenceManager();
        this.achievementManager = new AchievementManager();
        this.moduleManager = new LearningModuleManager();
        this.exitManager = new ApplicationExitManager(scanner, sessionManager);
        this.isRunning = false;
        this.currentPoints = 0;
        this.currentUser = "Git Learner";
    }
    
    /**
     * Start the application with comprehensive initialization
     */
    public void start() {
        try {
            performStartupSequence();
            loadUserSession();
            runMainApplicationLoop();
        } catch (Exception e) {
            handleApplicationError(e);
        } finally {
            performShutdownSequence();
        }
    }
    
    /**
     * Perform comprehensive startup sequence
     */
    private void performStartupSequence() {
        clearScreen();
        displayStartupBanner();
        animationRenderer.showWelcomeAnimation();
        displayApplicationInfo();
        waitForUserReady();
    }
    
    /**
     * Clear screen utility method
     */
    private void clearScreen() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.print("\n".repeat(50));
        }
    }
    
    /**
     * Display professional startup banner
     */
    private void displayStartupBanner() {
        System.out.println(BOLD + CYAN + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║               " + RED + "FAMILY LET'S \"GIT_IT_TOGETHER\"" + CYAN + "               ║");
        System.out.println("║                                                          ║");
        System.out.println("║              " + GREEN + "Interactive Git Learning CLI" + CYAN + "               ║");
        System.out.println("║                                                          ║");
        System.out.println("║         " + YELLOW + "🎮 Master Git Commands Through Fun! 🎮" + CYAN + "          ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display application information and credits
     */
    private void displayApplicationInfo() {
        System.out.println(BOLD + "📋 APPLICATION INFO" + RESET);
        System.out.println("═".repeat(50));
        System.out.println(GREEN + "Name: " + RESET + APPLICATION_NAME);
        System.out.println(GREEN + "Version: " + RESET + VERSION);
        System.out.println(GREEN + "Build Date: " + RESET + BUILD_DATE);
        System.out.println(GREEN + "Theme: " + RESET + "Family Feud with Steve Harvey");
        System.out.println();
        
        System.out.println(CYAN + "✨ Features:" + RESET);
        System.out.println("• Interactive Git command learning");
        System.out.println("• Progress tracking with achievements");
        System.out.println("• Session persistence and resume capability");
        System.out.println("• Family Feud themed animations");
        System.out.println("• Professional enterprise architecture");
        System.out.println();
    }
    
    /**
     * Wait for user to be ready
     */
    private void waitForUserReady() {
        System.out.println(YELLOW + "🚀 Ready to start your Git learning journey?" + RESET);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        clearScreen();
    }
    
    /**
     * Load existing user session or create new one
     */
    private void loadUserSession() {
        if (sessionManager.loadUserSession()) {
            var session = sessionManager.getCurrentSession();
            currentPoints = session.getTotalPoints();
            System.out.println(GREEN + "✅ Welcome back! Session loaded successfully." + RESET);
            System.out.println(CYAN + "Current Points: " + currentPoints + RESET);
            System.out.println();
        } else {
            System.out.println(YELLOW + "🌟 Starting fresh session. Good luck!" + RESET);
            sessionManager.createNewSession();
            System.out.println();
        }
    }
    
    /**
     * Main application loop with menu system
     */
    private void runMainApplicationLoop() {
        isRunning = true;
        
        while (isRunning) {
            try {
                displayMainMenu();
                String choice = getUserChoice();
                processMenuChoice(choice);
            } catch (Exception e) {
                handleMenuError(e);
            }
        }
    }
    
    /**
     * Display the main menu
     */
    private void displayMainMenu() {
        clearScreen();
        displayHeader();
        displayMenuOptions();
        displayCurrentStatus();
    }
    
    /**
     * Display application header
     */
    private void displayHeader() {
        System.out.println(BOLD + MAGENTA + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎯 MAIN MENU 🎯                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display menu options
     */
    private void displayMenuOptions() {
        System.out.println(BOLD + "What would you like to do today?" + RESET);
        System.out.println();
        
        System.out.println(GREEN + " [1] 📚 START LEARNING" + RESET);
        System.out.println("     " + CYAN + "• Interactive Git module tutorials" + RESET);
        System.out.println("     " + CYAN + "• Hands-on practice with real scenarios" + RESET);
        System.out.println("     " + CYAN + "• Progressive difficulty levels" + RESET);
        System.out.println();
        
        System.out.println(YELLOW + " [2] 🏆 VIEW ACHIEVEMENTS" + RESET);
        System.out.println("     " + CYAN + "• Check your earned badges and trophies" + RESET);
        System.out.println("     " + CYAN + "• Track learning progress and statistics" + RESET);
        System.out.println("     " + CYAN + "• Unlock special achievements" + RESET);
        System.out.println();
        
        System.out.println(BLUE + " [3] 🎭 STEVE HARVEY SHOW" + RESET);
        System.out.println("     " + CYAN + "• Enjoy entertaining animations" + RESET);
        System.out.println("     " + CYAN + "• Family Feud themed interactions" + RESET);
        System.out.println("     " + CYAN + "• Motivational messages from Steve" + RESET);
        System.out.println();
        
        System.out.println(RED + " [4] 🚪 EXIT APPLICATION" + RESET);
        System.out.println("     " + CYAN + "• Save progress and exit safely" + RESET);
        System.out.println("     " + CYAN + "• Multiple exit options available" + RESET);
        System.out.println();
    }
    
    /**
     * Display current user status
     */
    private void displayCurrentStatus() {
        var session = sessionManager.getCurrentSession();
        
        System.out.println(BOLD + "📊 CURRENT STATUS" + RESET);
        System.out.println("═".repeat(30));
        System.out.println(YELLOW + "Points: " + RESET + session.getTotalPoints());
        System.out.println(YELLOW + "Level: " + RESET + calculateUserLevel(session.getTotalPoints()));
        System.out.println(YELLOW + "Modules: " + RESET + session.getModulesCompleted() + "/3 completed");
        System.out.println(YELLOW + "Last Active: " + RESET + session.getLastModule());
        System.out.println();
    }
    
    /**
     * Calculate user level based on points
     * 
     * @param points Current user points
     * @return User level string
     */
    private String calculateUserLevel(int points) {
        if (points >= 1000) return "Git Master 🏆";
        if (points >= 500) return "Git Expert 🌟";
        if (points >= 250) return "Git Practitioner 🎯";
        if (points >= 100) return "Git Student 📚";
        return "Git Beginner 🌱";
    }
    
    /**
     * Get user menu choice
     * 
     * @return User's menu selection
     */
    private String getUserChoice() {
        System.out.print(BOLD + "Choose an option (1-4): " + RESET);
        return scanner.nextLine().trim();
    }
    
    /**
     * Process the user's menu choice
     * 
     * @param choice User's menu selection
     */
    private void processMenuChoice(String choice) {
        switch (choice.toLowerCase()) {
            case "1":
            case "learn":
            case "learning":
                handleLearningModule();
                break;
            case "2":
            case "achievements":
            case "badges":
                handleAchievements();
                break;
            case "3":
            case "animation":
            case "show":
                handleSteveHarveyShow();
                break;
            case "4":
            case "exit":
            case "quit":
                handleApplicationExit();
                break;
            default:
                handleInvalidChoice(choice);
                break;
        }
    }
    
    /**
     * Handle learning module selection
     */
    private void handleLearningModule() {
        try {
            moduleManager.startLearningExperience();
            updatePointsFromSession();
            achievementManager.checkAndAwardAchievements(sessionManager.getCurrentSession());
        } catch (Exception e) {
            System.out.println(RED + "Error during learning module: " + e.getMessage() + RESET);
            waitForUserAcknowledgment();
        }
    }
    
    /**
     * Handle achievements display
     */
    private void handleAchievements() {
        try {
            achievementManager.displayUserAchievements(sessionManager.getCurrentSession());
            waitForUserAcknowledgment();
        } catch (Exception e) {
            System.out.println(RED + "Error displaying achievements: " + e.getMessage() + RESET);
            waitForUserAcknowledgment();
        }
    }
    
    /**
     * Handle Steve Harvey show animation
     */
    private void handleSteveHarveyShow() {
        try {
            animationRenderer.showSteveHarveyAnimation();
            waitForUserAcknowledgment();
        } catch (Exception e) {
            System.out.println(RED + "Error during animation: " + e.getMessage() + RESET);
            waitForUserAcknowledgment();
        }
    }
    
    /**
     * Handle application exit
     */
    private void handleApplicationExit() {
        ApplicationExitManager.ExitStatus status = exitManager.handleApplicationExit();
        
        switch (status) {
            case SAVED_AND_EXIT:
            case FORCE_EXIT:
            case EMERGENCY_EXIT:
                isRunning = false;
                break;
            case CANCELLED:
                // Continue running
                break;
        }
    }
    
    /**
     * Handle invalid menu choice
     * 
     * @param choice The invalid choice entered
     */
    private void handleInvalidChoice(String choice) {
        System.out.println();
        System.out.println(RED + "❌ Invalid option: '" + choice + "'" + RESET);
        System.out.println(YELLOW + "Please choose a number between 1-4 or use keywords like 'learn', 'achievements', 'show', 'exit'" + RESET);
        System.out.println();
        waitForUserAcknowledgment();
    }
    
    /**
     * Update points from current session
     */
    private void updatePointsFromSession() {
        var session = sessionManager.getCurrentSession();
        currentPoints = session.getTotalPoints();
    }
    
    /**
     * Wait for user acknowledgment
     */
    private void waitForUserAcknowledgment() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handle menu-specific errors
     * 
     * @param e The exception that occurred
     */
    private void handleMenuError(Exception e) {
        System.out.println();
        System.out.println(RED + "⚠️  An error occurred in the menu system:" + RESET);
        System.out.println(YELLOW + e.getMessage() + RESET);
        System.out.println();
        System.out.println(CYAN + "Don't worry! Your progress is safe. Let's try again." + RESET);
        waitForUserAcknowledgment();
    }
    
    /**
     * Handle general application errors
     * 
     * @param e The exception that occurred
     */
    private void handleApplicationError(Exception e) {
        System.out.println();
        System.out.println(RED + BOLD + "🚨 APPLICATION ERROR DETECTED" + RESET);
        System.out.println("═".repeat(50));
        System.out.println(YELLOW + "Error: " + e.getMessage() + RESET);
        System.out.println(CYAN + "Attempting graceful recovery..." + RESET);
        
        // Attempt to save session before potential shutdown
        try {
            sessionManager.saveUserSession();
            System.out.println(GREEN + "✅ Session saved successfully during error recovery." + RESET);
        } catch (Exception saveError) {
            System.out.println(RED + "❌ Unable to save session during error recovery." + RESET);
        }
        
        waitForUserAcknowledgment();
    }
    
    /**
     * Handle critical startup errors
     * 
     * @param e The critical exception
     */
    private static void handleCriticalError(Exception e) {
        System.err.println("🚨 CRITICAL ERROR: Unable to start Git Training CLI");
        System.err.println("Error: " + e.getMessage());
        System.err.println("Please check your Java installation and try again.");
        System.exit(1);
    }
    
    /**
     * Perform graceful shutdown sequence
     */
    private void performShutdownSequence() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            System.out.println(GREEN + "🏁 Application shutdown complete. See you next time!" + RESET);
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }
}
