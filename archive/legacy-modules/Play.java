import java.util.Scanner;

/**
 * Play class for handling the game/learning functionality
 * 
 * This class manages all the interactive learning modules and game mechanics
 * for the Git training application.
 */
public class Play {
    
    // ANSI color codes for Family Feud theme
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    
    private Scanner scanner;
    
    /**
     * Constructor
     * @param scanner Scanner instance for user input
     */
    public Play(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Handle the PLAY option - Start new learning modules
     * Implements User Story A1: View available learning modules
     */
    public void startLearningSession() {
        Animation.clearScreen();
        System.out.println(GREEN + BOLD + "STARTING NEW LEARNING SESSION..." + RESET);
        System.out.println();
        
        displayAvailableModules();
        
        System.out.print(CYAN + "Choose a module (1-3) or 'B' to go back: " + RESET);
        String choice = scanner.nextLine().trim().toUpperCase();
        
        switch (choice) {
            case "1":
                startGitBasicsModule();
                break;
            case "2":
                startWorkingWithChangesModule();
                break;
            case "3":
                startBranchingModule();
                break;
            case "B":
            case "BACK":
                return;
            default:
                System.out.println(RED + "Invalid choice! Press Enter to continue..." + RESET);
                scanner.nextLine();
                break;
        }
    }
    
    /**
     * Display available learning modules
     */
    private void displayAvailableModules() {
        System.out.println(BOLD + BLUE + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                 AVAILABLE GIT LEARNING MODULES               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(GREEN + " [1] Git Basics" + RESET + "              - Learn init, add, commit, status");
        System.out.println("     " + CYAN + "Perfect for beginners! Master the fundamentals." + RESET);
        System.out.println();
        System.out.println(YELLOW + " [2] Working with Changes" + RESET + "    - Master status, diff, log, reset");
        System.out.println("     " + CYAN + "Intermediate level. Understand change management." + RESET);
        System.out.println();
        System.out.println(RED + " [3] Branching Fundamentals" + RESET + " - Branch, checkout, merge, rebase");
        System.out.println("     " + CYAN + "Advanced level. Master Git workflows." + RESET);
        System.out.println();
        System.out.println(BOLD + "Each module includes:" + RESET);
        System.out.println("  • Interactive tutorials");
        System.out.println("  • Hands-on practice");
        System.out.println("  • Achievement badges");
        System.out.println("  • Progress tracking");
        System.out.println();
    }
    
    /**
     * Start Git Basics learning module
     */
    private void startGitBasicsModule() {
        Animation.clearScreen();
        Animation.displayLoadingAnimation("Loading Git Basics Module", 1500);
        
        System.out.println(GREEN + BOLD + "🌟 GIT BASICS MODULE 🌟" + RESET);
        System.out.println();
        System.out.println("Welcome to your first Git adventure!");
        System.out.println("In this module, you'll learn:");
        System.out.println();
        System.out.println("• " + YELLOW + "git init" + RESET + "   - Initialize a new repository");
        System.out.println("• " + YELLOW + "git add" + RESET + "    - Stage files for commit");
        System.out.println("• " + YELLOW + "git commit" + RESET + " - Save changes to repository");
        System.out.println("• " + YELLOW + "git status" + RESET + " - Check repository status");
        System.out.println();
        
        // Simulate interactive learning
        System.out.println(CYAN + "Let's start with creating your first repository!" + RESET);
        System.out.println();
        System.out.println("Task 1: Initialize a new Git repository");
        System.out.println("Type the command you would use: ");
        System.out.print("> ");
        
        String command = scanner.nextLine().trim();
        if (command.equalsIgnoreCase("git init")) {
            System.out.println(GREEN + "✅ Correct! Repository initialized successfully!" + RESET);
            System.out.println("You earned: " + YELLOW + "★ First Repository Badge!" + RESET);
        } else {
            System.out.println(RED + "❌ Not quite right. The correct command is 'git init'" + RESET);
        }
        
        System.out.println();
        System.out.println("This is just a preview! Full module implementation coming soon.");
        System.out.println("Press Enter to return to main menu...");
        scanner.nextLine();
    }
    
    /**
     * Start Working with Changes learning module
     */
    private void startWorkingWithChangesModule() {
        Animation.clearScreen();
        Animation.displayLoadingAnimation("Loading Working with Changes Module", 1500);
        
        System.out.println(YELLOW + BOLD + "📊 WORKING WITH CHANGES MODULE 📊" + RESET);
        System.out.println();
        System.out.println("Master the art of change management in Git!");
        System.out.println("In this module, you'll learn:");
        System.out.println();
        System.out.println("• " + CYAN + "git status" + RESET + " - Monitor your working directory");
        System.out.println("• " + CYAN + "git diff" + RESET + "   - Compare changes between versions");
        System.out.println("• " + CYAN + "git log" + RESET + "    - View commit history");
        System.out.println("• " + CYAN + "git reset" + RESET + "  - Undo changes safely");
        System.out.println();
        System.out.println("Prerequisites: Complete Git Basics Module first!");
        System.out.println();
        System.out.println("This module implementation is coming in Sprint 2!");
        System.out.println("Press Enter to return to main menu...");
        scanner.nextLine();
    }
    
    /**
     * Start Branching Fundamentals learning module
     */
    private void startBranchingModule() {
        Animation.clearScreen();
        Animation.displayLoadingAnimation("Loading Branching Fundamentals Module", 1500);
        
        System.out.println(RED + BOLD + "🌿 BRANCHING FUNDAMENTALS MODULE 🌿" + RESET);
        System.out.println();
        System.out.println("Become a Git branching master!");
        System.out.println("In this module, you'll learn:");
        System.out.println();
        System.out.println("• " + GREEN + "git branch" + RESET + "   - Create and list branches");
        System.out.println("• " + GREEN + "git checkout" + RESET + " - Switch between branches");
        System.out.println("• " + GREEN + "git merge" + RESET + "    - Combine branch changes");
        System.out.println("• " + GREEN + "git rebase" + RESET + "   - Alternative merging strategy");
        System.out.println();
        System.out.println("Prerequisites: Complete Git Basics and Working with Changes!");
        System.out.println();
        System.out.println("This advanced module implementation is coming in Sprint 3!");
        System.out.println("Press Enter to return to main menu...");
        scanner.nextLine();
    }
    
    /**
     * Display a practice challenge
     * @param challenge The challenge description
     * @param correctAnswer The correct answer
     * @return true if user answered correctly
     */
    public boolean displayChallenge(String challenge, String correctAnswer) {
        System.out.println(BOLD + BLUE + "CHALLENGE: " + RESET + challenge);
        System.out.print("> ");
        String userAnswer = scanner.nextLine().trim();
        
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            System.out.println(GREEN + "✅ Excellent work!" + RESET);
            return true;
        } else {
            System.out.println(RED + "❌ Not quite right. The answer was: " + correctAnswer + RESET);
            return false;
        }
    }
}
