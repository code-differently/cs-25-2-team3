import java.util.Scanner;

/**
 * Git Training CLI Application - Main Menu Interface
 * 
 * This class provides the initial CLI screen with interactive menu options
 * for the Git training application as defined in the user stories.
 * 
 * Features:
 * - Interactive terminal-based menu
 * - Four main options: Play, Badges, Continue, Quit
 * - Input validation and error handling
 * - Clear console display formatting
 * - Modular class architecture
 */
public class GitTrainingCLI {
    
    private Scanner scanner;
    private boolean isRunning;
    
    // Modular components
    private Play playManager;
    private Badges badgesManager;
    private Continue continueManager;
    private Quit quitManager;
    
    // ANSI color codes for Family Feud theme
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    private static final String CYAN = "\033[36m";
    
    /**
     * Constructor - Initialize the CLI application
     */
    public GitTrainingCLI() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        
        // Initialize modular components
        this.continueManager = new Continue(scanner);
        this.playManager = new Play(scanner);
        this.badgesManager = new Badges(scanner);
        this.quitManager = new Quit(scanner, continueManager);
    }
    
    /**
     * Main application entry point
     */
    public static void main(String[] args) {
        GitTrainingCLI app = new GitTrainingCLI();
        app.start();
    }
    
    /**
     * Start the CLI application and display main menu
     */
    public void start() {
        displayWelcome();
        
        while (isRunning) {
            displayMainMenu();
            handleUserInput();
        }
        
        displayGoodbye();
        scanner.close();
    }
    
    /**
     * Display welcome message and Steve Harvey face with Family Feud theme
     */
    private void displayWelcome() {
        Animation.clearScreen();
        Animation.displayAnimatedSteve();
        
        System.out.println(BOLD + BLUE + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║            " + RED + "FAMILY LET'S... \"GIT_IT_TOGETHER\"" + BLUE + "             ║");
        System.out.println("║                                                          ║");
        System.out.println("║          " + GREEN + "Learn Git & GitHub Through Practice!" + BLUE + "           ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(GREEN + "Survey says... Welcome to the interactive Git learning experience!" + RESET);
        System.out.println("Master Git commands through hands-on practice in safe sandbox environments.");
        System.out.println();
    }
    
        /**
     * Get the current running status of the application
     * @return boolean indicating if application is still running
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Set the running status of the application
     * @param running boolean to set application running state
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }
    
    /**
     * Get the badges manager for integration purposes
     * @return the badges manager instance
     */
    public Badges getBadgesManager() {
        return badgesManager;
    }
    
    /**
     * Get the continue manager for integration purposes
     * @return the continue manager instance
     */
    public Continue getContinueManager() {
        return continueManager;
    }
}
    
    /**
     * Display the main menu options
     */
    private void displayMainMenu() {
        System.out.println(BOLD + BLUE + "═══════════════ MAIN MENU ═══════════════" + RESET);
        System.out.println();
        System.out.println(GREEN + " [P] PLAY" + RESET + "     - Start new Git learning modules");
        System.out.println(YELLOW + " [B] BADGES" + RESET + "   - View your achievements and progress");
        System.out.println(CYAN + " [C] CONTINUE" + RESET + " - Resume your previous session");
        System.out.println(RED + " [Q] QUIT" + RESET + "     - Exit the application");
        System.out.println();
        System.out.print(BOLD + "Choose an option (P/B/C/Q): " + RESET);
    }
    
    /**
     * Handle user input and route to appropriate methods
     */
    private void handleUserInput() {
        String input = scanner.nextLine().trim().toUpperCase();
        System.out.println();
        
        switch (input) {
            case "P":
            case "PLAY":
                handlePlayOption();
                break;
            case "B":
            case "BADGES":
                handleBadgesOption();
                break;
            case "C":
            case "CONTINUE":
                handleContinueOption();
                break;
            case "Q":
            case "QUIT":
                handleQuitOption();
                break;
            default:
                handleInvalidInput();
                break;
        }
    }
    
    /**
     * Handle the PLAY option - Start new learning modules
     * Implements User Story A1: View available learning modules
     */
    private void handlePlayOption() {
        playManager.startLearningSession();
        Animation.clearScreen();
    }
    
    /**
     * Handle the BADGES option - View achievements
     * Implements User Story B7: Achievement and progression systems
     */
    private void handleBadgesOption() {
        badgesManager.displayBadgesScreen();
        Animation.clearScreen();
    }
    
    /**
     * Handle the CONTINUE option - Resume previous session
     * Implements User Story A2: Resume previous session
     */
    private void handleContinueOption() {
        continueManager.resumePreviousSession();
        Animation.clearScreen();
    }
    
    /**
     * Handle the QUIT option - Exit application
     */
    private void handleQuitOption() {
        isRunning = !quitManager.handleQuitOption();
    }
    
    /**
     * Handle invalid input with helpful error message
     */
    private void handleInvalidInput() {
        System.out.println(RED + "Invalid option!" + RESET);
        System.out.println("Please choose one of the following:");
        System.out.println("  P - Play");
        System.out.println("  B - Badges");
        System.out.println("  C - Continue");
        System.out.println("  Q - Quit");
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        Animation.clearScreen();
    }
    
    /**
     * Display goodbye message when exiting
     */
    private void displayGoodbye() {
        // The Quit class now handles all goodbye functionality
        System.out.println(GREEN + "Thanks for using Git Training CLI!" + RESET);
    }
    
    /**
     * Clear the console screen for better user experience
     */
    private void clearScreen() {
        try {
            // Clear screen command for different operating systems
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: print newlines if clear screen fails
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Get the current running status of the application
     * @return boolean indicating if application is still running
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Set the running status of the application
     * @param running boolean to set application running state
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }
}

