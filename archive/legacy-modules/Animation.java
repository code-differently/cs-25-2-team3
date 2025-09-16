/**
 * Animation class for handling all visual animations in the Git Training CLI
 * 
 * This class contains methods for displaying animated sequences, particularly
 * the Steve Harvey face animation and loading screens.
 */
public class Animation {
    
    // ANSI color codes for Family Feud theme
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String YELLOW = "\033[33m";
    private static final String RED = "\033[31m";
    
    /**
     * Display animated Steve Harvey face during loading
     */
    public static void displayAnimatedSteve() {
        try {
            // Loading animation with progressive dots
            for (int i = 0; i < 3; i++) {
                clearScreen();
                System.out.println(GREEN + "\n\n\n\n\n\n\n");
                System.out.println("                    Loading Family Feud Git Edition");
                String dots = ".".repeat(i + 1);
                System.out.println("                           " + dots);
                Thread.sleep(300);
            }
            
            // Frame 1 - Steve appears (blinking in)
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "◦" + RESET + YELLOW + "             " + BOLD + "◦" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "────" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "───────────────" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + "\n                    Steve Harvey incoming..." + RESET);
            Thread.sleep(400);
            
            // Frame 2 - Eyes opening
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "◐" + RESET + YELLOW + "             " + BOLD + "◑" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "╲_______________╱" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + "\n                       Eyes opening..." + RESET);
            Thread.sleep(400);
            
            // Frame 3 - Steve looking left
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "◐" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "╲_______________╱" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + "\n                    Looking around..." + RESET);
            Thread.sleep(350);
            
            // Frame 4 - Steve looking center
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "╲_______________╱" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + "\n                   Making eye contact..." + RESET);
            Thread.sleep(350);
            
            // Frame 5 - Steve looking right
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "             " + BOLD + "◑" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "╲_______________╱" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + "\n                   Checking things out..." + RESET);
            Thread.sleep(350);
            
            // Frame 6 - Steve back to center, preparing to wink
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "╲_______________╱" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + "\n                      Getting ready..." + RESET);
            Thread.sleep(300);
            
            // Frame 7 - The classic Steve Harvey wink
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "─" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │    " + BOLD + "╲_______________╱" + RESET + YELLOW + "    │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(RED + BOLD + "\n                    *STEVE HARVEY WINK*" + RESET);
            Thread.sleep(600);
            
            // Frame 8 - Steve smiling bigger
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + "   │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + BOLD + "\n                  Survey says... WELCOME!" + RESET);
            Thread.sleep(800);
            
            // Frame 9 - Final celebratory frame
            clearScreen();
            System.out.println(YELLOW + "\n\n                    ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "✦" + RESET + YELLOW + "             " + BOLD + "✦" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + "   │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(RED + BOLD + "\n              🎉 LET'S GET THIS STARTED! 🎉" + RESET);
            Thread.sleep(1000);
            
            clearScreen();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Clear the console screen for better user experience
     */
    public static void clearScreen() {
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
     * Display a loading animation with custom message
     * @param message The message to display during loading
     * @param duration Duration in milliseconds
     */
    public static void displayLoadingAnimation(String message, int duration) {
        try {
            for (int i = 0; i < 3; i++) {
                clearScreen();
                System.out.println(GREEN + "\n\n\n\n\n\n\n");
                System.out.println("                    " + message);
                String dots = ".".repeat(i + 1);
                System.out.println("                           " + dots);
                Thread.sleep(duration / 3);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
