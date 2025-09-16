import java.util.Scanner;

/**
 * Quit class for handling application exit functionality
 * 
 * This class manages the graceful shutdown of the Git training application,
 * including progress saving and goodbye messages.
 */
public class Quit {
    
    // ANSI color codes for Family Feud theme
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    
    private Scanner scanner;
    private Continue continueManager;
    
    /**
     * Constructor
     * @param scanner Scanner instance for user input
     * @param continueManager Continue instance for saving progress
     */
    public Quit(Scanner scanner, Continue continueManager) {
        this.scanner = scanner;
        this.continueManager = continueManager;
    }
    
    /**
     * Handle the QUIT option - Exit application with confirmation
     * @return true if the user confirmed they want to quit
     */
    public boolean handleQuitOption() {
        System.out.println();
        System.out.println(RED + BOLD + "🚪 EXITING APPLICATION..." + RESET);
        System.out.println();
        
        displayQuitOptions();
        
        String choice = scanner.nextLine().trim().toUpperCase();
        
        switch (choice) {
            case "1":
            case "SAVE":
                return handleSaveAndQuit();
            case "2":
            case "QUIT":
                return handleQuitWithoutSaving();
            case "3":
            case "CANCEL":
                handleCancelQuit();
                return false;
            default:
                System.out.println(RED + "Invalid option! Returning to main menu..." + RESET);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return false;
        }
    }
    
    /**
     * Display quit options to the user
     */
    private void displayQuitOptions() {
        System.out.println(BOLD + YELLOW + "What would you like to do?" + RESET);
        System.out.println();
        System.out.println(GREEN + " [1] SAVE & QUIT" + RESET + "     - Save progress and exit");
        System.out.println(RED + " [2] QUIT WITHOUT SAVING" + RESET + " - Exit without saving");
        System.out.println(CYAN + " [3] CANCEL" + RESET + "           - Return to main menu");
        System.out.println();
        System.out.print(BOLD + "Choose an option (1/2/3): " + RESET);
    }
    
    /**
     * Handle save and quit option
     * @return true to confirm quit
     */
    private boolean handleSaveAndQuit() {
        System.out.println();
        Animation.displayLoadingAnimation("Saving your progress", 1200);
        
        if (continueManager.saveProgress()) {
            System.out.println(GREEN + "✅ Progress saved successfully!" + RESET);
        } else {
            System.out.println(RED + "❌ Failed to save progress." + RESET);
            System.out.println("Do you still want to quit? (Y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y") && !confirm.equals("yes")) {
                return false;
            }
        }
        
        displayGoodbyeMessage();
        return true;
    }
    
    /**
     * Handle quit without saving option
     * @return true to confirm quit
     */
    private boolean handleQuitWithoutSaving() {
        System.out.println();
        System.out.println(YELLOW + "⚠️  Are you sure you want to quit without saving?" + RESET);
        System.out.println("Any unsaved progress will be lost.");
        System.out.print("Type 'YES' to confirm: ");
        
        String confirmation = scanner.nextLine().trim();
        if (confirmation.equalsIgnoreCase("YES")) {
            displayGoodbyeMessage();
            return true;
        } else {
            System.out.println(CYAN + "Quit cancelled. Returning to main menu..." + RESET);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }
    
    /**
     * Handle cancel quit option
     */
    private void handleCancelQuit() {
        System.out.println();
        System.out.println(GREEN + "Great choice! Let's continue learning Git!" + RESET);
        System.out.println("Returning to main menu...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Display goodbye message when exiting
     */
    private void displayGoodbyeMessage() {
        Animation.clearScreen();
        
        // Animated goodbye sequence
        displayGoodbyeAnimation();
        
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
        
        displayFinalStats();
        displayMotivationalMessage();
    }
    
    /**
     * Display goodbye animation
     */
    private void displayGoodbyeAnimation() {
        try {
            // Steve Harvey waving goodbye
            for (int i = 0; i < 3; i++) {
                Animation.clearScreen();
                System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
                System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
                
                if (i % 2 == 0) {
                    System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "      👋      " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
                } else {
                    System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "       👋     " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
                }
                
                System.out.println(YELLOW + "                 │                         │" + RESET);
                System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
                System.out.println(YELLOW + "                 │                         │" + RESET);
                System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + "   │" + RESET);
                System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
                System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
                
                if (i == 0) {
                    System.out.println(GREEN + BOLD + "\n                    See you soon!" + RESET);
                } else if (i == 1) {
                    System.out.println(BLUE + BOLD + "\n                  Keep practicing Git!" + RESET);
                } else {
                    System.out.println(RED + BOLD + "\n                Survey says... Goodbye!" + RESET);
                }
                
                Thread.sleep(800);
            }
            
            Animation.clearScreen();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Display final statistics
     */
    private void displayFinalStats() {
        System.out.println(BOLD + CYAN + "📊 SESSION SUMMARY" + RESET);
        System.out.println("═".repeat(40));
        
        // Get progress from continue manager if available
        if (continueManager != null && continueManager.getProgress() != null) {
            var progress = continueManager.getProgress();
            System.out.println(YELLOW + "Total Points Earned: " + RESET + progress.totalPoints);
            System.out.println(YELLOW + "Modules Completed: " + RESET + progress.modulesCompleted + "/3");
            System.out.println(YELLOW + "Last Module: " + RESET + progress.lastModule);
        } else {
            System.out.println(YELLOW + "Thanks for trying Git Training CLI!" + RESET);
        }
        System.out.println();
    }
    
    /**
     * Display motivational closing message
     */
    private void motivationalMessage() {
        String[] messages = {
            "Remember: Every Git expert was once a beginner! 🌟",
            "Practice makes perfect in Git! Keep learning! 💪",
            "Git is a journey, not a destination! 🚀",
            "You're one commit closer to becoming a Git master! ⭐",
            "Version control is power - use it wisely! 🔥"
        };
        
        int randomIndex = (int) (Math.random() * messages.length);
        System.out.println(GREEN + BOLD + messages[randomIndex] + RESET);
        System.out.println();
    }
    
    /**
     * Display motivational message based on progress
     */
    private void displayMotivationalMessage() {
        if (continueManager != null && continueManager.getProgress() != null) {
            var progress = continueManager.getProgress();
            
            if (progress.modulesCompleted == 0) {
                System.out.println(CYAN + "💡 Pro tip: Start with the Git Basics module next time!" + RESET);
            } else if (progress.modulesCompleted == 1) {
                System.out.println(CYAN + "🎯 You're making great progress! Try the next module!" + RESET);
            } else if (progress.modulesCompleted == 2) {
                System.out.println(CYAN + "🔥 Almost there! One more module to become a Git expert!" + RESET);
            } else {
                System.out.println(CYAN + "👑 Congratulations! You're a certified Git master!" + RESET);
            }
        } else {
            motivationalMessage();
        }
        
        System.out.println();
        System.out.println(BOLD + "🎮 Ready to continue your Git journey?" + RESET);
        System.out.println("Run the program again anytime to pick up where you left off!");
        System.out.println();
    }
    
    /**
     * Perform emergency exit (for unexpected situations)
     */
    public void emergencyExit() {
        Animation.clearScreen();
        System.out.println(RED + BOLD + "⚠️  EMERGENCY EXIT" + RESET);
        System.out.println();
        System.out.println("Attempting to save progress...");
        
        if (continueManager != null) {
            continueManager.saveProgress();
        }
        
        System.out.println(YELLOW + "Thanks for using Git Training CLI!" + RESET);
        System.out.println(GREEN + "Survey says... Until next time! 👋" + RESET);
    }
}
