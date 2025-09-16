/**
 * Animation Utility Class
 * 
 * Handles all visual animations and screen management for the Git Training CLI.
 * Designed with enterprise-grade separation of concerns and reusability.
 * 
 * Key Features:
 * - Cross-platform screen clearing
 * - Configurable animation sequences
 * - Resource-efficient display methods
 * - Thread-safe animation handling
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 * @since 2025-09-16
 */
public class AnimationRenderer {
    
    // ANSI Color Constants - Following Google Style Guide
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String RED = "\033[31m";
    
    // Animation Configuration
    private static final int DEFAULT_FRAME_DELAY = 300;
    private static final int ANIMATION_FRAMES = 9;
    
    /**
     * Display the signature Steve Harvey animated welcome sequence
     * 
     * This method provides the core branding animation for the application.
     * Optimized for performance and visual impact.
     */
    public static void displayWelcomeAnimation() {
        try {
            displayLoadingSequence();
            displayCharacterAnimation();
            clearScreen();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Graceful degradation - skip animation
        }
    }
    
    /**
     * Display loading sequence with progressive indicators
     */
    private static void displayLoadingSequence() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            clearScreen();
            System.out.println(GREEN + "\n\n\n\n\n\n\n");
            System.out.println("                    Loading Family Feud Git Edition");
            String dots = ".".repeat(i + 1);
            System.out.println("                           " + dots);
            Thread.sleep(DEFAULT_FRAME_DELAY);
        }
    }
    
    /**
     * Display the main character animation sequence
     */
    private static void displayCharacterAnimation() throws InterruptedException {
        // Frame 1 - Character appears
        displayFrame(1, "◦", "◦", "────", "───────────────", "Steve Harvey incoming...");
        Thread.sleep(400);
        
        // Frame 2 - Eyes opening
        displayFrame(2, "◐", "◑", "╲___╱", "╲_______________╱", "Eyes opening...");
        Thread.sleep(400);
        
        // Frame 3 - Looking left
        displayFrame(3, "◐", "●", "╲___╱", "╲_______________╱", "Looking around...");
        Thread.sleep(350);
        
        // Frame 4 - Looking center
        displayFrame(4, "●", "●", "╲___╱", "╲_______________╱", "Making eye contact...");
        Thread.sleep(350);
        
        // Frame 5 - Looking right
        displayFrame(5, "●", "◑", "╲___╱", "╲_______________╱", "Checking things out...");
        Thread.sleep(350);
        
        // Frame 6 - Back to center
        displayFrame(6, "●", "●", "╲___╱", "╲_______________╱", "Getting ready...");
        Thread.sleep(300);
        
        // Frame 7 - The classic wink
        displayFrame(7, "─", "●", "╲___╱", "╲_______________╱", "*STEVE HARVEY WINK*");
        Thread.sleep(600);
        
        // Frame 8 - Bigger smile
        displayFrame(8, "●", "●", "╲___╱", "╲_________________╱", "Survey says... WELCOME!");
        Thread.sleep(800);
        
        // Frame 9 - Final celebration
        displayFinalFrame();
        Thread.sleep(1000);
    }
    
    /**
     * Display a character animation frame
     * 
     * @param frameNumber The frame number for debugging
     * @param leftEye Left eye character
     * @param rightEye Right eye character
     * @param nose Nose character sequence
     * @param mouth Mouth character sequence
     * @param message Frame message
     */
    private static void displayFrame(int frameNumber, String leftEye, String rightEye, 
                                   String nose, String mouth, String message) {
        clearScreen();
        System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
        System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
        System.out.println(YELLOW + "                  ╱   " + BOLD + leftEye + RESET + YELLOW + 
                          "             " + BOLD + rightEye + RESET + YELLOW + "   ╲" + RESET);
        System.out.println(YELLOW + "                 │                         │" + RESET);
        System.out.println(YELLOW + "                 │          " + BOLD + nose + RESET + YELLOW + 
                          "          │" + RESET);
        System.out.println(YELLOW + "                 │                         │" + RESET);
        System.out.println(YELLOW + "                 │    " + BOLD + mouth + RESET + YELLOW + 
                          "    │" + RESET);
        System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
        System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
        
        // Color-coded messages based on frame importance
        String colorCode = frameNumber == 7 ? RED + BOLD : GREEN;
        System.out.println(colorCode + "\n                    " + message + RESET);
    }
    
    /**
     * Display the final celebration frame
     */
    private static void displayFinalFrame() {
        clearScreen();
        System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
        System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
        System.out.println(YELLOW + "                  ╱   " + BOLD + "✦" + RESET + YELLOW + 
                          "             " + BOLD + "✦" + RESET + YELLOW + "   ╲" + RESET);
        System.out.println(YELLOW + "                 │                         │" + RESET);
        System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + 
                          "          │" + RESET);
        System.out.println(YELLOW + "                 │                         │" + RESET);
        System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + 
                          "   │" + RESET);
        System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
        System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
        System.out.println(RED + BOLD + "\n              🎉 LET'S GET THIS STARTED! 🎉" + RESET);
    }
    
    /**
     * Clear the console screen with cross-platform compatibility
     * 
     * Implements fallback strategy for maximum compatibility across environments.
     */
    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("windows")) {
                // Windows command
                new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()
                    .start()
                    .waitFor();
            } else {
                // Unix/Linux/MacOS ANSI escape sequence
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: print newlines
            System.out.print("\n".repeat(50));
        }
    }
    
    /**
     * Display a customizable loading animation
     * 
     * @param message The message to display during loading
     * @param durationMs Total duration in milliseconds
     */
    public static void displayLoadingAnimation(String message, int durationMs) {
        try {
            final int steps = 3;
            final int stepDuration = durationMs / steps;
            
            for (int i = 0; i < steps; i++) {
                clearScreen();
                System.out.println(GREEN + "\n\n\n\n\n\n\n");
                System.out.println("                    " + message);
                System.out.println("                           " + ".".repeat(i + 1));
                Thread.sleep(stepDuration);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Display progress bar with percentage
     * 
     * @param percent Progress percentage (0-100)
     * @param width Width of the progress bar
     * @param label Label for the progress bar
     */
    public static void displayProgressBar(int percent, int width, String label) {
        int filledLength = (percent * width) / 100;
        
        System.out.print(label + ": [");
        for (int i = 0; i < width; i++) {
            if (i < filledLength) {
                System.out.print(GREEN + "█" + RESET);
            } else {
                System.out.print("░");
            }
        }
        System.out.println("] " + percent + "%");
    }
}
