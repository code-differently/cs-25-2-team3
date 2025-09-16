import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Continue class for handling session persistence and resumption
 * 
 * This class manages saving and loading user progress, allowing users
 * to continue their Git learning journey from where they left off.
 */
public class Continue {
    
    // ANSI color codes for Family Feud theme
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    
    private Scanner scanner;
    private static final String SAVE_FILE = "git_training_progress.dat";
    
    /**
     * Inner class to represent saved progress
     */
    public static class ProgressData {
        String lastModule;
        int currentStep;
        int totalPoints;
        int modulesCompleted;
        String lastSaveTime;
        String userName;
        
        ProgressData() {
            this.lastModule = "None";
            this.currentStep = 0;
            this.totalPoints = 0;
            this.modulesCompleted = 0;
            this.lastSaveTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.userName = "Anonymous Learner";
        }
    }
    
    private ProgressData progress;
    
    /**
     * Constructor
     * @param scanner Scanner instance for user input
     */
    public Continue(Scanner scanner) {
        this.scanner = scanner;
        this.progress = new ProgressData();
    }
    
    /**
     * Handle the CONTINUE option - Resume previous session
     * Implements User Story A2: Resume previous session
     */
    public void resumePreviousSession() {
        Animation.clearScreen();
        Animation.displayLoadingAnimation("Checking for saved progress", 1200);
        
        if (loadProgress()) {
            displaySavedProgress();
            offerResumeOptions();
        } else {
            displayNoSavedProgress();
        }
    }
    
    /**
     * Load progress from save file
     * @return true if progress was loaded successfully
     */
    private boolean loadProgress() {
        try {
            File saveFile = new File(SAVE_FILE);
            if (!saveFile.exists()) {
                return false;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
                progress.userName = reader.readLine();
                progress.lastModule = reader.readLine();
                progress.currentStep = Integer.parseInt(reader.readLine());
                progress.totalPoints = Integer.parseInt(reader.readLine());
                progress.modulesCompleted = Integer.parseInt(reader.readLine());
                progress.lastSaveTime = reader.readLine();
                
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(RED + "Error loading saved progress: " + e.getMessage() + RESET);
            return false;
        }
    }
    
    /**
     * Display saved progress information
     */
    private void displaySavedProgress() {
        System.out.println(BOLD + CYAN + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    SAVED PROGRESS FOUND                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        System.out.println(GREEN + "Welcome back, " + BOLD + progress.userName + RESET + GREEN + "!" + RESET);
        System.out.println();
        
        System.out.println(BLUE + "📊 Your Progress Summary:" + RESET);
        System.out.println("═".repeat(40));
        System.out.println(YELLOW + "Last Module: " + RESET + progress.lastModule);
        System.out.println(YELLOW + "Current Step: " + RESET + progress.currentStep);
        System.out.println(YELLOW + "Total Points: " + RESET + progress.totalPoints);
        System.out.println(YELLOW + "Modules Completed: " + RESET + progress.modulesCompleted + "/3");
        System.out.println(YELLOW + "Last Session: " + RESET + progress.lastSaveTime);
        System.out.println();
        
        // Display progress visualization
        displayProgressVisualization();
    }
    
    /**
     * Display progress visualization
     */
    private void displayProgressVisualization() {
        System.out.println(BOLD + BLUE + "🎯 Module Progress:" + RESET);
        System.out.println();
        
        // Git Basics
        String gitBasicsStatus = progress.modulesCompleted >= 1 ? GREEN + "✅ COMPLETED" : 
                               progress.lastModule.equals("Git Basics") ? YELLOW + "🔄 IN PROGRESS" : 
                               CYAN + "🔒 LOCKED";
        System.out.println(" 1. Git Basics           " + gitBasicsStatus + RESET);
        
        // Working with Changes
        String changesStatus = progress.modulesCompleted >= 2 ? GREEN + "✅ COMPLETED" : 
                             progress.lastModule.equals("Working with Changes") ? YELLOW + "🔄 IN PROGRESS" : 
                             progress.modulesCompleted >= 1 ? CYAN + "📖 AVAILABLE" : CYAN + "🔒 LOCKED";
        System.out.println(" 2. Working with Changes " + changesStatus + RESET);
        
        // Branching Fundamentals
        String branchingStatus = progress.modulesCompleted >= 3 ? GREEN + "✅ COMPLETED" : 
                               progress.lastModule.equals("Branching Fundamentals") ? YELLOW + "🔄 IN PROGRESS" : 
                               progress.modulesCompleted >= 2 ? CYAN + "📖 AVAILABLE" : CYAN + "🔒 LOCKED";
        System.out.println(" 3. Branching Fundamentals " + branchingStatus + RESET);
        System.out.println();
    }
    
    /**
     * Offer options for resuming the session
     */
    private void offerResumeOptions() {
        System.out.println(BOLD + GREEN + "What would you like to do?" + RESET);
        System.out.println();
        System.out.println(CYAN + " [R] RESUME" + RESET + "    - Continue from where you left off");
        System.out.println(YELLOW + " [S] RESTART" + RESET + "  - Start fresh (keeps your badges)");
        System.out.println(BLUE + " [D] DELETE" + RESET + "   - Delete saved progress completely");
        System.out.println(RED + " [B] BACK" + RESET + "     - Return to main menu");
        System.out.println();
        System.out.print(BOLD + "Choose an option (R/S/D/B): " + RESET);
        
        String choice = scanner.nextLine().trim().toUpperCase();
        
        switch (choice) {
            case "R":
            case "RESUME":
                handleResume();
                break;
            case "S":
            case "RESTART":
                handleRestart();
                break;
            case "D":
            case "DELETE":
                handleDelete();
                break;
            case "B":
            case "BACK":
                break;
            default:
                System.out.println(RED + "Invalid option! Press Enter to continue..." + RESET);
                scanner.nextLine();
                break;
        }
    }
    
    /**
     * Handle resume option
     */
    private void handleResume() {
        Animation.clearScreen();
        Animation.displayLoadingAnimation("Resuming your learning session", 1500);
        
        System.out.println(GREEN + BOLD + "🎉 WELCOME BACK! 🎉" + RESET);
        System.out.println();
        System.out.println("Resuming from: " + YELLOW + progress.lastModule + RESET);
        System.out.println("Step: " + progress.currentStep);
        System.out.println();
        System.out.println("This feature will redirect you to the appropriate module!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handle restart option
     */
    private void handleRestart() {
        System.out.println();
        System.out.println(YELLOW + "Are you sure you want to restart your progress?" + RESET);
        System.out.println("(Your badges and total points will be preserved)");
        System.out.print("Type 'YES' to confirm: ");
        
        String confirmation = scanner.nextLine().trim();
        if (confirmation.equalsIgnoreCase("YES")) {
            int savedPoints = progress.totalPoints;
            progress = new ProgressData();
            progress.totalPoints = savedPoints;
            saveProgress();
            
            System.out.println(GREEN + "Progress restarted! Your badges and points have been preserved." + RESET);
        } else {
            System.out.println(CYAN + "Restart cancelled." + RESET);
        }
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handle delete option
     */
    private void handleDelete() {
        System.out.println();
        System.out.println(RED + "⚠️  WARNING: This will permanently delete all your progress!" + RESET);
        System.out.println("This includes all badges, points, and module progress.");
        System.out.print("Type 'DELETE' to confirm: ");
        
        String confirmation = scanner.nextLine().trim();
        if (confirmation.equals("DELETE")) {
            File saveFile = new File(SAVE_FILE);
            if (saveFile.delete()) {
                System.out.println(GREEN + "All progress deleted successfully." + RESET);
            } else {
                System.out.println(RED + "Error deleting progress file." + RESET);
            }
        } else {
            System.out.println(CYAN + "Deletion cancelled." + RESET);
        }
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Display no saved progress message
     */
    private void displayNoSavedProgress() {
        System.out.println(BOLD + BLUE + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    NO SAVED PROGRESS                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        System.out.println(CYAN + "No previous session found." + RESET);
        System.out.println("Start with " + GREEN + "'PLAY'" + RESET + " to begin your Git learning journey!");
        System.out.println();
        System.out.println("Once you start learning, your progress will be automatically saved:");
        System.out.println("• " + YELLOW + "Module completion" + RESET);
        System.out.println("• " + YELLOW + "Current step tracking" + RESET);
        System.out.println("• " + YELLOW + "Points and badges earned" + RESET);
        System.out.println("• " + YELLOW + "Session timestamps" + RESET);
        System.out.println();
        System.out.println("Press Enter to return to main menu...");
        scanner.nextLine();
    }
    
    /**
     * Save current progress to file
     * @return true if save was successful
     */
    public boolean saveProgress() {
        try {
            try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
                writer.println(progress.userName);
                writer.println(progress.lastModule);
                writer.println(progress.currentStep);
                writer.println(progress.totalPoints);
                writer.println(progress.modulesCompleted);
                writer.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                
                return true;
            }
        } catch (IOException e) {
            System.out.println(RED + "Error saving progress: " + e.getMessage() + RESET);
            return false;
        }
    }
    
    /**
     * Update progress information
     * @param module The current module
     * @param step The current step
     * @param points Total points
     * @param completed Modules completed
     */
    public void updateProgress(String module, int step, int points, int completed) {
        progress.lastModule = module;
        progress.currentStep = step;
        progress.totalPoints = points;
        progress.modulesCompleted = completed;
        progress.lastSaveTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * Get the current progress data
     * @return The current progress data
     */
    public ProgressData getProgress() {
        return progress;
    }
}
