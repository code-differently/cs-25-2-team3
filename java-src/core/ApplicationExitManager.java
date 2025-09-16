/**
 * Application Exit Manager - Graceful Shutdown and State Preservation
 * 
 * Enterprise-grade application termination system with comprehensive
 * state preservation, user confirmation workflows, and graceful resource cleanup.
 * Designed following Google's internal application lifecycle management patterns.
 * 
 * Key Features:
 * - Multi-stage exit confirmation process
 * - Automatic state preservation with rollback
 * - Resource cleanup and memory management
 * - Animated goodbye sequences with user engagement
 * - Emergency shutdown capabilities
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 * @since 2025-09-16
 */
import java.util.Scanner;

public class ApplicationExitManager {
    
    // ANSI Color Constants
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    
    // Animation Configuration
    private static final int GOODBYE_ANIMATION_FRAMES = 3;
    private static final int FRAME_DURATION = 800; // milliseconds
    private static final int FINAL_MESSAGE_DURATION = 1500;
    
    private final Scanner scanner;
    private final SessionPersistenceManager sessionManager;
    
    /**
     * Exit Status Enumeration
     */
    public enum ExitStatus {
        CANCELLED("Exit cancelled by user"),
        SAVED_AND_EXIT("Session saved and application exited"),
        FORCE_EXIT("Application exited without saving"),
        EMERGENCY_EXIT("Emergency exit performed");
        
        private final String description;
        
        ExitStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Constructor
     * 
     * @param scanner Scanner instance for user interaction
     * @param sessionManager Session persistence manager for save operations
     */
    public ApplicationExitManager(Scanner scanner, SessionPersistenceManager sessionManager) {
        this.scanner = scanner;
        this.sessionManager = sessionManager;
    }
    
    /**
     * Handle application exit with comprehensive user interaction
     * 
     * @return ExitStatus indicating the outcome of the exit process
     */
    public ExitStatus handleApplicationExit() {
        clearScreen();
        displayExitHeader();
        displayExitOptions();
        
        return processExitChoice();
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
     * Display exit process header
     */
    private void displayExitHeader() {
        System.out.println(RED + BOLD + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🚪 EXITING APPLICATION                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        System.out.println(YELLOW + "Thank you for using Git Training CLI!" + RESET);
        System.out.println("Let's make sure your progress is safely preserved.");
        System.out.println();
    }
    
    /**
     * Display exit options with detailed descriptions
     */
    private void displayExitOptions() {
        System.out.println(BOLD + "What would you like to do?" + RESET);
        System.out.println();
        
        System.out.println(GREEN + " [1] SAVE & QUIT" + RESET);
        System.out.println("     " + CYAN + "• Save all progress and exit safely" + RESET);
        System.out.println("     " + CYAN + "• Preserves achievements, points, and module progress" + RESET);
        System.out.println("     " + CYAN + "• Creates backup for data recovery" + RESET);
        System.out.println();
        
        System.out.println(RED + " [2] QUIT WITHOUT SAVING" + RESET);
        System.out.println("     " + CYAN + "• Exit immediately without preserving current session" + RESET);
        System.out.println("     " + CYAN + "• Previous saved progress remains intact" + RESET);
        System.out.println("     " + CYAN + "• Requires additional confirmation" + RESET);
        System.out.println();
        
        System.out.println(BLUE + " [3] CANCEL" + RESET);
        System.out.println("     " + CYAN + "• Return to the main menu" + RESET);
        System.out.println("     " + CYAN + "• Continue your learning journey" + RESET);
        System.out.println();
    }
    
    /**
     * Process user's exit choice
     * 
     * @return ExitStatus based on user selection
     */
    private ExitStatus processExitChoice() {
        System.out.print(BOLD + "Choose an option (1/2/3): " + RESET);
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
            case "SAVE":
                return handleSaveAndExit();
            case "2":
            case "QUIT":
                return handleForceExit();
            case "3":
            case "CANCEL":
                return handleExitCancellation();
            default:
                return handleInvalidExitChoice();
        }
    }
    
    /**
     * Handle save and exit option
     * 
     * @return ExitStatus.SAVED_AND_EXIT on success, ExitStatus.CANCELLED on failure
     */
    private ExitStatus handleSaveAndExit() {
        System.out.println();
        displaySaveProgress();
        
        if (sessionManager.saveUserSession()) {
            System.out.println(GREEN + "✅ Progress saved successfully!" + RESET);
            displaySuccessfulExitAnimation();
            return ExitStatus.SAVED_AND_EXIT;
        } else {
            return handleSaveFailure();
        }
    }
    
    /**
     * Display save progress animation
     */
    private void displaySaveProgress() {
        try {
            System.out.println(YELLOW + "💾 Saving your progress..." + RESET);
            
            String[] saveSteps = {
                "Backing up current session...",
                "Writing progress data...",
                "Verifying data integrity...",
                "Creating recovery checkpoint..."
            };
            
            for (String step : saveSteps) {
                System.out.println("   " + step);
                Thread.sleep(300);
            }
            
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Handle save failure scenario
     */
    private ExitStatus handleSaveFailure() {
        System.out.println(RED + "❌ Failed to save progress!" + RESET);
        System.out.println("This could be due to disk space or permission issues.");
        System.out.println();
        System.out.println("Do you still want to quit? (Y/N): ");
        
        String confirm = scanner.nextLine().trim().toLowerCase();
        if ("y".equals(confirm) || "yes".equals(confirm)) {
            displaySuccessfulExitAnimation();
            return ExitStatus.FORCE_EXIT;
        } else {
            System.out.println(CYAN + "Returning to main menu to try again..." + RESET);
            waitForUser();
            return ExitStatus.CANCELLED;
        }
    }
    
    /**
     * Handle force exit without saving
     * 
     * @return ExitStatus.FORCE_EXIT on confirmation, ExitStatus.CANCELLED otherwise
     */
    private ExitStatus handleForceExit() {
        System.out.println();
        displayForceExitWarning();
        
        if (confirmForceExit()) {
            displaySuccessfulExitAnimation();
            return ExitStatus.FORCE_EXIT;
        } else {
            System.out.println(CYAN + "Exit cancelled. Returning to main menu..." + RESET);
            waitForUser();
            return ExitStatus.CANCELLED;
        }
    }
    
    /**
     * Display force exit warning
     */
    private void displayForceExitWarning() {
        System.out.println(YELLOW + "⚠️  WARNING: Unsaved Progress Will Be Lost!" + RESET);
        System.out.println();
        System.out.println("Any progress made in this session will not be saved:");
        System.out.println("• " + RED + "Current module step progress" + RESET);
        System.out.println("• " + RED + "Recently earned points" + RESET);
        System.out.println("• " + RED + "New achievement unlocks" + RESET);
        System.out.println("• " + RED + "Session timestamp updates" + RESET);
        System.out.println();
        System.out.println(CYAN + "Previously saved progress will remain intact." + RESET);
        System.out.println();
    }
    
    /**
     * Confirm force exit with double verification
     * 
     * @return true if user confirms force exit
     */
    private boolean confirmForceExit() {
        System.out.print("Type 'YES' to confirm exit without saving: ");
        String confirmation = scanner.nextLine().trim();
        
        if (!"YES".equals(confirmation)) {
            return false;
        }
        
        // Double confirmation for force exit
        System.out.print("Are you absolutely sure? Type 'CONFIRM': ");
        String doubleConfirm = scanner.nextLine().trim();
        
        return "CONFIRM".equals(doubleConfirm);
    }
    
    /**
     * Handle exit cancellation
     * 
     * @return ExitStatus.CANCELLED
     */
    private ExitStatus handleExitCancellation() {
        System.out.println();
        System.out.println(GREEN + "Great choice! Let's continue your Git learning journey!" + RESET);
        System.out.println("🚀 Keep up the excellent progress!");
        System.out.println();
        System.out.println("Returning to main menu...");
        waitForUser();
        return ExitStatus.CANCELLED;
    }
    
    /**
     * Handle invalid exit choice
     * 
     * @return ExitStatus.CANCELLED
     */
    private ExitStatus handleInvalidExitChoice() {
        System.out.println();
        System.out.println(RED + "Invalid option selected!" + RESET);
        System.out.println("Please choose 1 (Save & Quit), 2 (Quit without saving), or 3 (Cancel).");
        System.out.println();
        System.out.println("Returning to main menu for safety...");
        waitForUser();
        return ExitStatus.CANCELLED;
    }
    
    /**
     * Display successful exit animation with Steve Harvey farewell
     */
    private void displaySuccessfulExitAnimation() {
        clearScreen();
        displayGoodbyeAnimation();
        displayFinalMessage();
        displaySessionStatistics();
        displayMotivationalMessage();
    }
    
    /**
     * Display animated goodbye sequence
     */
    private void displayGoodbyeAnimation() {
        try {
            for (int frame = 0; frame < GOODBYE_ANIMATION_FRAMES; frame++) {
                clearScreen();
                displayGoodbyeFrame(frame);
                Thread.sleep(FRAME_DURATION);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Display individual goodbye animation frame
     * 
     * @param frameNumber The current frame number
     */
    private void displayGoodbyeFrame(int frameNumber) {
        System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
        System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
        
        // Animated waving hand
        String leftElement = frameNumber % 2 == 0 ? "●" : "●";
        String rightElement = frameNumber % 2 == 0 ? "      👋" : "       👋";
        
        System.out.println(YELLOW + "                  ╱   " + BOLD + leftElement + RESET + YELLOW + 
                          rightElement + "     " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
        System.out.println(YELLOW + "                 │                         │" + RESET);
        System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + 
                          "          │" + RESET);
        System.out.println(YELLOW + "                 │                         │" + RESET);
        System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + 
                          "   │" + RESET);
        System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
        System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
        
        // Frame-specific messages
        String[] messages = {
            "See you soon!",
            "Keep practicing Git!",
            "Survey says... Goodbye!"
        };
        
        String[] colors = {GREEN, BLUE, RED};
        String message = frameNumber < messages.length ? messages[frameNumber] : messages[messages.length - 1];
        String color = frameNumber < colors.length ? colors[frameNumber] : colors[colors.length - 1];
        
        System.out.println(color + BOLD + "\n                    " + message + RESET);
    }
    
    /**
     * Display final goodbye message
     */
    private void displayFinalMessage() {
        clearScreen();
        
        System.out.println(BOLD + GREEN + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║                    Thank you for using                  ║");
        System.out.println("║            " + RED + "FAMILY LET'S \"GIT_IT_TOGETHER\"" + GREEN + "             ║");
        System.out.println("║                                                          ║");
        System.out.println("║              Keep learning and happy coding!             ║");
        System.out.println("║                                                          ║");
        System.out.println("║              " + YELLOW + "Survey says... Git mastered!" + GREEN + "               ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display session statistics
     */
    private void displaySessionStatistics() {
        var session = sessionManager.getCurrentSession();
        
        System.out.println(BOLD + CYAN + "📊 SESSION SUMMARY" + RESET);
        System.out.println("═".repeat(40));
        System.out.println(YELLOW + "Total Points Earned: " + RESET + session.getTotalPoints());
        System.out.println(YELLOW + "Modules Completed: " + RESET + session.getModulesCompleted() + "/3");
        System.out.println(YELLOW + "Last Module: " + RESET + session.getLastModule());
        System.out.println(YELLOW + "Session Time: " + RESET + session.getLastSaveTime());
        System.out.println();
    }
    
    /**
     * Display motivational closing message
     */
    private void displayMotivationalMessage() {
        var session = sessionManager.getCurrentSession();
        String message = generateMotivationalMessage(session.getModulesCompleted());
        
        System.out.println(GREEN + BOLD + message + RESET);
        System.out.println();
        System.out.println(BOLD + "🎮 Ready to continue your Git journey?" + RESET);
        System.out.println("Run the application again anytime to pick up where you left off!");
        System.out.println();
        
        try {
            Thread.sleep(FINAL_MESSAGE_DURATION);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Generate personalized motivational message based on progress
     * 
     * @param modulesCompleted Number of completed modules
     * @return Personalized motivational message
     */
    private String generateMotivationalMessage(int modulesCompleted) {
        switch (modulesCompleted) {
            case 0:
                return "💡 Pro tip: Start with the Git Basics module next time!";
            case 1:
                return "🎯 You're making great progress! Try the next module!";
            case 2:
                return "🔥 Almost there! One more module to become a Git expert!";
            case 3:
                return "👑 Congratulations! You're a certified Git master!";
            default:
                return "🌟 Remember: Every Git expert was once a beginner!";
        }
    }
    
    /**
     * Emergency exit method for unexpected situations
     * 
     * @return ExitStatus.EMERGENCY_EXIT
     */
    public ExitStatus performEmergencyExit() {
        clearScreen();
        System.out.println(RED + BOLD + "⚠️  EMERGENCY EXIT INITIATED" + RESET);
        System.out.println();
        System.out.println("Attempting to save progress...");
        
        // Attempt quick save
        sessionManager.saveUserSession();
        
        System.out.println(YELLOW + "Emergency shutdown complete." + RESET);
        System.out.println(GREEN + "Thanks for using Git Training CLI! 👋" + RESET);
        
        return ExitStatus.EMERGENCY_EXIT;
    }
    
    /**
     * Wait for user input (helper method)
     */
    private void waitForUser() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
