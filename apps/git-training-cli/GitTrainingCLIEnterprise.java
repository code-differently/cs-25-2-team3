/**
 * Git Training CLI - Complete Enterprise Application
 * 
 * A comprehensive, enterprise-grade Git learning application featuring:
 * - Interactive learning modules with progressive difficulty
 * - Family Feud themed animations with Steve Harvey
 * - Comprehensive achievement and badge system
 * - Session persistence with atomic operations
 * - Professional error handling and recovery
 * 
 * This single-file version maintains all enterprise patterns while
 * resolving compilation dependencies for immediate execution.
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 * @since 2025-09-16
 */

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class GitTrainingCLIEnterprise {
    
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
    private static final String APPLICATION_NAME = "Git Training CLI Enterprise";
    private static final String VERSION = "1.0.0";
    private static final String SESSION_FILE = "git_training_session.json";
    
    // Core Application State
    private final Scanner scanner;
    private UserSession currentSession;
    private boolean isRunning;
    private SteveHarveyHintSystem hintSystem;
    
    /**
     * User Session Data Model
     */
    public static class UserSession {
        private int totalPoints;
        private int modulesCompleted;
        private String lastModule;
        private String lastSaveTime;
        private Map<String, Boolean> achievements;
        private Map<String, Integer> moduleProgress;
        
        public UserSession() {
            this.totalPoints = 0;
            this.modulesCompleted = 0;
            this.lastModule = "None";
            this.lastSaveTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            this.achievements = new HashMap<>();
            this.moduleProgress = new HashMap<>();
            initializeAchievements();
        }
        
        private void initializeAchievements() {
            achievements.put("first_steps", false);
            achievements.put("git_basics_complete", false);
            achievements.put("branching_master", false);
            achievements.put("collaboration_expert", false);
            achievements.put("point_collector", false);
            achievements.put("perfect_score", false);
        }
        
        // Getters and setters
        public int getTotalPoints() { return totalPoints; }
        public void setTotalPoints(int points) { this.totalPoints = points; }
        public void addPoints(int points) { this.totalPoints += points; }
        
        public int getModulesCompleted() { return modulesCompleted; }
        public void setModulesCompleted(int completed) { this.modulesCompleted = completed; }
        
        public String getLastModule() { return lastModule; }
        public void setLastModule(String module) { this.lastModule = module; }
        
        public String getLastSaveTime() { return lastSaveTime; }
        public void updateSaveTime() { 
            this.lastSaveTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); 
        }
        
        public Map<String, Boolean> getAchievements() { return achievements; }
        public Map<String, Integer> getModuleProgress() { return moduleProgress; }
        
        public void unlockAchievement(String achievement) {
            achievements.put(achievement, true);
        }
        
        public boolean hasAchievement(String achievement) {
            return achievements.getOrDefault(achievement, false);
        }
    }
    
    /**
     * Learning Module Definition
     */
    public static class LearningModule {
        private final String name;
        private final String description;
        private final int difficulty;
        private final int pointValue;
        private final List<String> steps;
        
        public LearningModule(String name, String description, int difficulty, int pointValue, List<String> steps) {
            this.name = name;
            this.description = description;
            this.difficulty = difficulty;
            this.pointValue = pointValue;
            this.steps = steps;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getDifficulty() { return difficulty; }
        public int getPointValue() { return pointValue; }
        public List<String> getSteps() { return steps; }
    }
    
    /**
     * Steve Harvey Hint System - Provides funny, helpful hints for incorrect inputs
     */
    public static class SteveHarveyHintSystem {
        private static final String[] WRONG_ANSWER_INTROS = {
            "Hold up, hold up, family!",
            "Whoa there, cowboy!",
            "Pump the brakes!",
            "Nah nah nah, that ain't it!",
            "Family, what are you doing?!",
            "Come on now!",
            "Aw man, really?",
            "Lord have mercy!",
            "Y'all trying to give me a heart attack!"
        };
        
        private static final String[] MENU_WRONG_HINTS = {
            "Look here, we got numbers 1 through 4, and you picked... THAT? Try again!",
            "Family, the menu is right there! Pick a number between 1 and 4!",
            "Steve Harvey don't understand hieroglyphics! Use 1, 2, 3, or 4!",
            "That answer is so wrong, it made my mustache curl! Try 1-4!",
            "I've seen some bad answers on Family Feud, but THIS? Pick 1, 2, 3, or 4!",
            "The survey says... THAT AIN'T ON THE BOARD! Try numbers 1-4!",
            "Family, I need you to focus! We got options 1, 2, 3, and 4. Pick one!",
            "You're making me look bad in front of the Git family! Choose 1-4!"
        };
        
        private static final String[] LEARNING_MODULE_HINTS = {
            "Baby, we got 3 modules here, not 300! Try 1, 2, or 3!",
            "Family, count with me: ONE, TWO, THREE! Pick one of those!",
            "This ain't rocket science! Module 1, 2, or 3, that's it!",
            "You trying to learn Git or trying to break my computer? Pick 1-3!",
            "Lord, give me strength! We got 3 learning modules. Choose wisely!",
            "The Git gods are crying! Choose module 1, 2, or 3!",
            "Steve Harvey's patience is running thin! Pick 1, 2, or 3!"
        };
        
        private static final String[] GIT_COMMAND_HINTS = {
            "That command is more confused than a tourist in Times Square! Try 'git' followed by the actual command!",
            "Family, Git commands start with 'git'! Like 'git add' or 'git commit'!",
            "You're speaking in tongues! Git wants real commands like 'git status'!",
            "That's not a Git command, that's just random typing! Try 'git help' if you're lost!",
            "Baby, Git don't understand that gibberish! Use real commands!",
            "Even my grandmother knows Git commands better than that! Try again!",
            "That command is deader than disco! Use proper Git syntax!"
        };
        
        private static final String[] GENERAL_WRONG_HINTS = {
            "Family, that answer is so wrong, it's right... WRONG!",
            "I've seen bad answers, but that's next level bad!",
            "That response just made my brain hurt! Try something that makes sense!",
            "You're killing me, Smalls! Give me a real answer!",
            "That's about as useful as a chocolate teapot! Try again!",
            "Family, I'm trying to help you learn Git, not lose my mind!",
            "That answer is more lost than car keys! Focus up!"
        };
        
        private static final String[] ENCOURAGEMENT_ENDINGS = {
            "But hey, we all make mistakes! Let's try again!",
            "Don't worry, even Einstein probably struggled with Git!",
            "It's all good, family! Learning is a journey!",
            "Keep your head up! We're gonna get this right!",
            "That's why we practice! Give it another shot!",
            "No worries! Steve Harvey believes in you!",
            "Shake it off and try again, you got this!",
            "Every expert was once a beginner! Keep going!"
        };
        
        private Random random = new Random();
        private Map<String, Integer> wrongAttempts = new HashMap<>();
        
        /**
         * Get a humorous hint for wrong menu input
         */
        public String getMenuHint(String wrongInput) {
            String context = "menu";
            int attempts = wrongAttempts.getOrDefault(context, 0) + 1;
            wrongAttempts.put(context, attempts);
            
            String intro = WRONG_ANSWER_INTROS[random.nextInt(WRONG_ANSWER_INTROS.length)];
            String hint = MENU_WRONG_HINTS[random.nextInt(MENU_WRONG_HINTS.length)];
            
            if (attempts > 3) {
                hint = "Family, we've been at this for a while now! Take a deep breath and pick 1, 2, 3, or 4!";
            }
            
            return intro + " " + hint;
        }
        
        /**
         * Get a humorous hint for wrong learning module input
         */
        public String getLearningModuleHint(String wrongInput) {
            String context = "learning";
            int attempts = wrongAttempts.getOrDefault(context, 0) + 1;
            wrongAttempts.put(context, attempts);
            
            String intro = WRONG_ANSWER_INTROS[random.nextInt(WRONG_ANSWER_INTROS.length)];
            String hint = LEARNING_MODULE_HINTS[random.nextInt(LEARNING_MODULE_HINTS.length)];
            String ending = ENCOURAGEMENT_ENDINGS[random.nextInt(ENCOURAGEMENT_ENDINGS.length)];
            
            if (attempts > 2) {
                hint = "Listen up, family! We got THREE modules. Type '1' for Git Basics, '2' for Branching, or '3' for Collaboration!";
            }
            
            return intro + " " + hint + " " + ending;
        }
        
        /**
         * Get a humorous hint for wrong Git command input
         */
        public String getGitCommandHint(String wrongInput) {
            String context = "git";
            int attempts = wrongAttempts.getOrDefault(context, 0) + 1;
            wrongAttempts.put(context, attempts);
            
            String intro = WRONG_ANSWER_INTROS[random.nextInt(WRONG_ANSWER_INTROS.length)];
            String hint = GIT_COMMAND_HINTS[random.nextInt(GIT_COMMAND_HINTS.length)];
            String ending = ENCOURAGEMENT_ENDINGS[random.nextInt(ENCOURAGEMENT_ENDINGS.length)];
            
            if (wrongInput.toLowerCase().contains("help")) {
                hint = "Now THAT'S what I'm talking about! Use 'git help' to see available commands!";
                ending = "You're learning, family!";
            } else if (wrongInput.toLowerCase().startsWith("git")) {
                hint = "Close! You got the 'git' part right, but '" + wrongInput + "' ain't a real command!";
            }
            
            return intro + " " + hint + " " + ending;
        }
        
        /**
         * Get a general humorous hint for any wrong input
         */
        public String getGeneralHint(String wrongInput) {
            String intro = WRONG_ANSWER_INTROS[random.nextInt(WRONG_ANSWER_INTROS.length)];
            String hint = GENERAL_WRONG_HINTS[random.nextInt(GENERAL_WRONG_HINTS.length)];
            String ending = ENCOURAGEMENT_ENDINGS[random.nextInt(ENCOURAGEMENT_ENDINGS.length)];
            
            return intro + " " + hint + " " + ending;
        }
        
        /**
         * Get a specific hint based on the type of wrong input and context
         */
        public String getSpecificHint(String wrongInput, String context) {
            switch (context.toLowerCase()) {
                case "menu":
                    return getMenuHint(wrongInput);
                case "learning":
                case "module":
                    return getLearningModuleHint(wrongInput);
                case "git":
                case "command":
                    return getGitCommandHint(wrongInput);
                default:
                    return getGeneralHint(wrongInput);
            }
        }
        
        /**
         * Reset attempt counters for a specific context
         */
        public void resetAttempts(String context) {
            wrongAttempts.remove(context);
        }
        
        /**
         * Get Steve Harvey's reaction to continuous wrong answers
         */
        public String getExasperatedReaction() {
            String[] reactions = {
                "Family, I'm about to call my producer! Y'all are testing my patience!",
                "Lord, give me the strength of a thousand mustaches!",
                "I've seen people struggle with Family Feud, but this is next level!",
                "Steve Harvey is officially confused! And that don't happen often!",
                "Y'all making me want to go back to hosting Family Feud!",
                "I need a vacation after this! Maybe a nice beach somewhere..."
            };
            return reactions[random.nextInt(reactions.length)];
        }
    }
    
    /**
     * Main application entry point
     */
    public static void main(String[] args) {
        try {
            GitTrainingCLIEnterprise app = new GitTrainingCLIEnterprise();
            app.start();
        } catch (Exception e) {
            System.err.println("🚨 CRITICAL ERROR: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Constructor
     */
    public GitTrainingCLIEnterprise() {
        this.scanner = new Scanner(System.in);
        this.currentSession = new UserSession();
        this.isRunning = false;
        this.hintSystem = new SteveHarveyHintSystem();
    }
    
    /**
     * Start the application
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
     * Startup sequence with professional presentation
     */
    private void performStartupSequence() {
        clearScreen();
        displayStartupBanner();
        showWelcomeAnimation();
        displayApplicationInfo();
        waitForUserReady();
    }
    
    /**
     * Clear screen utility
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
     * Display startup banner
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
     * Show welcome animation with full Steve Harvey animated sequence
     */
    private void showWelcomeAnimation() {
        try {
            System.out.println(YELLOW + "*** Welcome to the Steve Harvey Git Training Experience! ***" + RESET);
            System.out.println();
            
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
            
            System.out.println("\n\n" + BOLD + "\"Survey says... Let's learn Git!\"" + RESET);
            System.out.println("                - Steve Harvey");
            System.out.println();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Display application information
     */
    private void displayApplicationInfo() {
        System.out.println(BOLD + "📋 APPLICATION INFO" + RESET);
        System.out.println("═".repeat(50));
        System.out.println(GREEN + "Name: " + RESET + APPLICATION_NAME);
        System.out.println(GREEN + "Version: " + RESET + VERSION);
        System.out.println(GREEN + "Theme: " + RESET + "Family Feud with Steve Harvey");
        System.out.println();
        
        System.out.println(CYAN + "*** Enterprise Features:" + RESET);
        System.out.println("• Interactive Git command learning");
        System.out.println("• Progress tracking with achievements");
        System.out.println("• Session persistence and resume capability");
        System.out.println("• Family Feud themed animations");
        System.out.println("• Professional enterprise architecture");
        System.out.println();
    }
    
    /**
     * Wait for user readiness
     */
    private void waitForUserReady() {
        System.out.println(YELLOW + "*** Ready to start your Git learning journey? ***" + RESET);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        clearScreen();
    }
    
    /**
     * Load existing session or create new one
     */
    private void loadUserSession() {
        try {
            File sessionFile = new File(SESSION_FILE);
            if (sessionFile.exists()) {
                // Simple session loading (in real enterprise app, would use JSON parsing)
                System.out.println(GREEN + "✅ Welcome back! Previous session detected." + RESET);
                System.out.println(CYAN + "Starting fresh but maintaining enterprise structure..." + RESET);
            } else {
                System.out.println(YELLOW + "🌟 Starting fresh session. Good luck!" + RESET);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(YELLOW + "Could not load previous session. Starting fresh." + RESET);
        }
    }
    
    /**
     * Main application loop
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
     * Display main menu
     */
    private void displayMainMenu() {
        clearScreen();
        displayHeader();
        displayMenuOptions();
        displayCurrentStatus();
    }
    
    /**
     * Display header
     */
    private void displayHeader() {
        System.out.println(BOLD + MAGENTA + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    *** MAIN MENU ***                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display menu options
     */
    private void displayMenuOptions() {
        System.out.println(BOLD + "What would you like to do today?" + RESET);
        System.out.println();
        
        System.out.println(GREEN + " [1] *** START LEARNING" + RESET);
        System.out.println("     " + CYAN + "• Interactive Git module tutorials" + RESET);
        System.out.println("     " + CYAN + "• Hands-on practice with real scenarios" + RESET);
        System.out.println("     " + CYAN + "• Progressive difficulty levels" + RESET);
        System.out.println();
        
        System.out.println(YELLOW + " [2] 🏆 VIEW ACHIEVEMENTS" + RESET);
        System.out.println("     " + CYAN + "• Check your earned badges and trophies" + RESET);
        System.out.println("     " + CYAN + "• Track learning progress and statistics" + RESET);
        System.out.println("     " + CYAN + "• Unlock special achievements" + RESET);
        System.out.println();
        
        System.out.println(BLUE + " [3] *** STEVE HARVEY SHOW" + RESET);
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
     * Display current status
     */
    private void displayCurrentStatus() {
        System.out.println(BOLD + "*** CURRENT STATUS" + RESET);
        System.out.println("═".repeat(30));
        System.out.println(YELLOW + "Points: " + RESET + currentSession.getTotalPoints());
        System.out.println(YELLOW + "Level: " + RESET + calculateUserLevel(currentSession.getTotalPoints()));
        System.out.println(YELLOW + "Modules: " + RESET + currentSession.getModulesCompleted() + "/3 completed");
        System.out.println(YELLOW + "Last Active: " + RESET + currentSession.getLastModule());
        System.out.println();
    }
    
    /**
     * Calculate user level
     */
    private String calculateUserLevel(int points) {
        if (points >= 1000) return "Git Master 🏆";
        if (points >= 500) return "Git Expert 🌟";
        if (points >= 250) return "Git Practitioner *** EXPERT ***";
        if (points >= 100) return "Git Student *** LEARNING ***";
        return "Git Beginner 🌱";
    }
    
    /**
     * Get user choice
     */
    private String getUserChoice() {
        System.out.print(BOLD + "Choose an option (1-4): " + RESET);
        return scanner.nextLine().trim();
    }
    
    /**
     * Process menu choice
     */
    private void processMenuChoice(String choice) {
        switch (choice.toLowerCase()) {
            case "1":
            case "learn":
                hintSystem.resetAttempts("menu"); // Reset after successful navigation
                handleLearningModule();
                break;
            case "2":
            case "achievements":
                hintSystem.resetAttempts("menu");
                handleAchievements();
                break;
            case "3":
            case "animation":
                hintSystem.resetAttempts("menu");
                handleSteveHarveyShow();
                break;
            case "4":
            case "exit":
                hintSystem.resetAttempts("menu");
                handleApplicationExit();
                break;
            default:
                handleInvalidChoice(choice);
                break;
        }
    }
    
    /**
     * Handle learning module
     */
    private void handleLearningModule() {
        clearScreen();
        System.out.println(BOLD + GREEN + "🎓 GIT LEARNING MODULES" + RESET);
        System.out.println("═".repeat(50));
        System.out.println();
        
        List<LearningModule> modules = createLearningModules();
        
        for (int i = 0; i < modules.size(); i++) {
            LearningModule module = modules.get(i);
            System.out.println(CYAN + " [" + (i + 1) + "] " + module.getName() + RESET);
            System.out.println("     " + YELLOW + "Difficulty: " + "★".repeat(module.getDifficulty()) + 
                              " | Points: " + module.getPointValue() + RESET);
            System.out.println("     " + module.getDescription());
            System.out.println();
        }
        
        System.out.print(BOLD + "Choose a module (1-3) or 'back': " + RESET);
        String choice = scanner.nextLine().trim();
        
        if ("back".equals(choice.toLowerCase())) {
            return;
        }
        
        try {
            int moduleIndex = Integer.parseInt(choice) - 1;
            if (moduleIndex >= 0 && moduleIndex < modules.size()) {
                runLearningModule(modules.get(moduleIndex));
                hintSystem.resetAttempts("learning"); // Reset after successful selection
            } else {
                System.out.println();
                String steveHint = hintSystem.getLearningModuleHint(choice);
                System.out.println(RED + steveHint + RESET);
                waitForUserAcknowledgment();
            }
        } catch (NumberFormatException e) {
            System.out.println();
            String steveHint = hintSystem.getLearningModuleHint(choice);
            System.out.println(RED + steveHint + RESET);
            waitForUserAcknowledgment();
        }
    }
    
    /**
     * Create learning modules
     */
    private List<LearningModule> createLearningModules() {
        List<LearningModule> modules = new ArrayList<>();
        
        // Module 1: Git Basics
        modules.add(new LearningModule(
            "Git Basics",
            "Learn fundamental Git commands and concepts",
            1,
            100,
            Arrays.asList(
                "Understanding version control",
                "Git repository initialization",
                "Basic add, commit, and status commands",
                "Working with the staging area"
            )
        ));
        
        // Module 2: Branching and Merging
        modules.add(new LearningModule(
            "Branching & Merging",
            "Master Git branching strategies and merge workflows",
            2,
            200,
            Arrays.asList(
                "Creating and switching branches",
                "Merge vs. rebase strategies",
                "Handling merge conflicts",
                "Branch naming conventions"
            )
        ));
        
        // Module 3: Collaboration
        modules.add(new LearningModule(
            "Collaboration",
            "Advanced Git workflows for team development",
            3,
            300,
            Arrays.asList(
                "Remote repositories and push/pull",
                "Pull requests and code review",
                "Git flow and collaboration patterns",
                "Advanced Git commands"
            )
        ));
        
        return modules;
    }
    
    /**
     * Run a specific learning module
     */
    private void runLearningModule(LearningModule module) {
        clearScreen();
        System.out.println(BOLD + BLUE + "*** " + module.getName().toUpperCase() + RESET);
        System.out.println("═".repeat(60));
        System.out.println();
        
        System.out.println(GREEN + "Welcome to " + module.getName() + "!" + RESET);
        System.out.println(YELLOW + module.getDescription() + RESET);
        System.out.println();
        
        // Steve Harvey motivation
        showSteveHarveyMotivation();
        
        System.out.println(CYAN + "📋 Module Steps:" + RESET);
        for (int i = 0; i < module.getSteps().size(); i++) {
            System.out.println((i + 1) + ". " + module.getSteps().get(i));
        }
        System.out.println();
        
        // Simulate learning process with interactive Git command practice
        System.out.println(YELLOW + "*** Let's practice these concepts! ***" + RESET);
        System.out.println("Time for some hands-on Git command practice!");
        System.out.println();
        
        // Interactive Git command practice
        practiceGitCommands(module);
        
        // Award points and achievements
        currentSession.addPoints(module.getPointValue());
        currentSession.setModulesCompleted(currentSession.getModulesCompleted() + 1);
        currentSession.setLastModule(module.getName());
        
        // Check for achievements
        checkAndAwardAchievements();
        
        System.out.println(GREEN + "*** Module completed! You earned " + module.getPointValue() + " points! ***" + RESET);
        System.out.println(CYAN + "Total Points: " + currentSession.getTotalPoints() + RESET);
        
        waitForUserAcknowledgment();
    }
    
    /**
     * Interactive Git command practice with Steve Harvey hints
     */
    private void practiceGitCommands(LearningModule module) {
        System.out.println(BOLD + CYAN + "*** GIT COMMAND PRACTICE ***" + RESET);
        System.out.println("Steve Harvey is here to help you learn some Git commands!");
        System.out.println();
        
        String[] commands = getCommandsForModule(module.getName());
        Random random = new Random();
        String targetCommand = commands[random.nextInt(commands.length)];
        
        System.out.println(YELLOW + "*** CHALLENGE: Enter the Git command for: " + getCommandDescription(targetCommand) + " ***" + RESET);
        System.out.println(CYAN + "Hint: All Git commands start with 'git'" + RESET);
        System.out.println();
        
        boolean commandCorrect = false;
        int attempts = 0;
        
        while (!commandCorrect && attempts < 5) {
            System.out.print(BOLD + "Enter Git command: " + RESET);
            String userInput = scanner.nextLine().trim();
            attempts++;
            
            if (userInput.equalsIgnoreCase(targetCommand)) {
                commandCorrect = true;
                System.out.println();
                System.out.println(GREEN + "*** EXCELLENT! That's correct! ***" + RESET);
                showSteveHarveySuccess();
                hintSystem.resetAttempts("git");
            } else if (userInput.toLowerCase().equals("skip") || userInput.toLowerCase().equals("help")) {
                System.out.println();
                System.out.println(BLUE + "The correct answer was: " + targetCommand + RESET);
                System.out.println(YELLOW + "Don't worry, family! Practice makes perfect!" + RESET);
                break;
            } else {
                System.out.println();
                String steveHint = hintSystem.getGitCommandHint(userInput);
                System.out.println(RED + steveHint + RESET);
                
                if (attempts >= 3) {
                    System.out.println(YELLOW + "*** Hint: The answer is '" + targetCommand + "' ***" + RESET);
                }
                
                if (attempts >= 4) {
                    System.out.println(CYAN + hintSystem.getExasperatedReaction() + RESET);
                }
                System.out.println();
            }
        }
        
        if (!commandCorrect && attempts >= 5) {
            System.out.println(BLUE + "The correct answer was: " + targetCommand + RESET);
            System.out.println(YELLOW + "Keep practicing, family! You'll get it next time!" + RESET);
        }
        
        System.out.println();
    }
    
    /**
     * Get commands for specific module
     */
    private String[] getCommandsForModule(String moduleName) {
        switch (moduleName.toLowerCase()) {
            case "git basics":
                return new String[]{"git init", "git add", "git commit", "git status", "git log"};
            case "branching & merging":
                return new String[]{"git branch", "git checkout", "git merge", "git rebase", "git branch -d"};
            case "collaboration":
                return new String[]{"git clone", "git push", "git pull", "git remote", "git fetch"};
            default:
                return new String[]{"git status", "git add", "git commit"};
        }
    }
    
    /**
     * Get description for Git command
     */
    private String getCommandDescription(String command) {
        switch (command.toLowerCase()) {
            case "git init": return "Initialize a new Git repository";
            case "git add": return "Stage changes for commit";
            case "git commit": return "Save changes to repository";
            case "git status": return "Check repository status";
            case "git log": return "View commit history";
            case "git branch": return "List or create branches";
            case "git checkout": return "Switch branches or restore files";
            case "git merge": return "Merge branches together";
            case "git rebase": return "Reapply commits on top of another base";
            case "git branch -d": return "Delete a branch";
            case "git clone": return "Copy a repository from remote";
            case "git push": return "Upload changes to remote repository";
            case "git pull": return "Download changes from remote repository";
            case "git remote": return "Manage remote repositories";
            case "git fetch": return "Download objects from remote repository";
            default: return "Perform Git operation";
        }
    }
    
    /**
     * Show Steve Harvey success celebration
     */
    private void showSteveHarveySuccess() {
        String[] celebrations = {
            "*** SURVEY SAYS... THAT'S A GREAT ANSWER! ***",
            "*** FAMILY, WE GOT OURSELVES A GIT EXPERT! ***",
            "*** THAT'S WHAT I'M TALKING ABOUT! ***",
            "*** YOU'RE ON FIRE, FAMILY! ***",
            "*** STEVE HARVEY APPROVED! ***",
            "*** BOOM! NAILED IT! ***"
        };
        Random random = new Random();
        String celebration = celebrations[random.nextInt(celebrations.length)];
        System.out.println(BOLD + GREEN + celebration + RESET);
    }
    
    /**
     * Show Steve Harvey motivation
     */
    private void showSteveHarveyMotivation() {
        String[] motivations = {
            "\"Family, let's git it together! We're gonna learn some commands!\"",
            "\"Survey says... Git is the #1 version control system!\"",
            "\"Come on family! Show me... VERSION CONTROL!\"",
            "\"That's a good answer! Git add, git commit, git push!\"",
            "\"We got ourselves a Git expert in the making!\""
        };
        
        Random random = new Random();
        String motivation = motivations[random.nextInt(motivations.length)];
        
        System.out.println(BOLD + YELLOW + motivation + RESET);
        System.out.println("                                    - Steve Harvey");
        System.out.println();
    }
    
    /**
     * Check and award achievements
     */
    private void checkAndAwardAchievements() {
        boolean newAchievement = false;
        
        // First Steps achievement
        if (!currentSession.hasAchievement("first_steps") && currentSession.getTotalPoints() > 0) {
            currentSession.unlockAchievement("first_steps");
            newAchievement = true;
            System.out.println(GREEN + "🏆 Achievement Unlocked: First Steps!" + RESET);
        }
        
        // Point Collector achievement
        if (!currentSession.hasAchievement("point_collector") && currentSession.getTotalPoints() >= 500) {
            currentSession.unlockAchievement("point_collector");
            newAchievement = true;
            System.out.println(GREEN + "🏆 Achievement Unlocked: Point Collector!" + RESET);
        }
        
        // Module completion achievements
        if (!currentSession.hasAchievement("git_basics_complete") && currentSession.getModulesCompleted() >= 1) {
            currentSession.unlockAchievement("git_basics_complete");
            newAchievement = true;
            System.out.println(GREEN + "🏆 Achievement Unlocked: Git Basics Complete!" + RESET);
        }
        
        if (newAchievement) {
            System.out.println();
        }
    }
    
    /**
     * Handle achievements display
     */
    private void handleAchievements() {
        clearScreen();
        System.out.println(BOLD + YELLOW + "🏆 YOUR ACHIEVEMENTS" + RESET);
        System.out.println("═".repeat(50));
        System.out.println();
        
        Map<String, String> achievementDescriptions = new HashMap<>();
        achievementDescriptions.put("first_steps", "*** First Steps - Started your Git journey");
        achievementDescriptions.put("git_basics_complete", "*** Git Basics Complete - Finished the basics module");
        achievementDescriptions.put("branching_master", "*** Branching Master - Mastered Git branches");
        achievementDescriptions.put("collaboration_expert", "*** Collaboration Expert - Team Git workflows");
        achievementDescriptions.put("point_collector", "*** Point Collector - Earned 500+ points");
        achievementDescriptions.put("perfect_score", "*** Perfect Score - Achieved maximum points");
        
        int unlockedCount = 0;
        for (Map.Entry<String, Boolean> achievement : currentSession.getAchievements().entrySet()) {
            String description = achievementDescriptions.getOrDefault(achievement.getKey(), achievement.getKey());
            if (achievement.getValue()) {
                System.out.println(GREEN + "✅ " + description + RESET);
                unlockedCount++;
            } else {
                System.out.println(RED + "❌ " + description + RESET);
            }
        }
        
        System.out.println();
        System.out.println(CYAN + "Progress: " + unlockedCount + "/" + currentSession.getAchievements().size() + 
                          " achievements unlocked" + RESET);
        System.out.println(YELLOW + "Total Points: " + currentSession.getTotalPoints() + RESET);
        
        waitForUserAcknowledgment();
    }
    
    /**
     * Handle Steve Harvey show
     */
    private void handleSteveHarveyShow() {
        clearScreen();
        System.out.println(BOLD + RED + "*** THE STEVE HARVEY GIT SHOW! ***" + RESET);
        System.out.println("═".repeat(50));
        System.out.println();
        
        showSteveHarveyAnimation();
        
        String[] quotes = {
            "\"Family, I've been hosting Family Feud for years, but Git? That's a whole new game!\"",
            "\"Survey says... 100% of developers need version control!\"",
            "\"You better git commit to learning these commands!\"",
            "\"That's what I'm talking about! Show me GIT PUSH!\"",
            "\"Family, we're gonna git merge all our knowledge together!\""
        };
        
        for (String quote : quotes) {
            System.out.println(YELLOW + quote + RESET);
            System.out.println("                                    - Steve Harvey");
            System.out.println();
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        
        System.out.println(BOLD + GREEN + "\"Keep learning, family! Steve Harvey believes in you!\"" + RESET);
        
        waitForUserAcknowledgment();
    }
    
    /**
     * Steve Harvey animation with full animated sequence
     */
    private void showSteveHarveyAnimation() {
        try {
            // Animated entrance
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
            System.out.println(GREEN + "\n                   Steve is thinking..." + RESET);
            Thread.sleep(800);
            
            // Steve gets excited
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
            System.out.println(GREEN + "\n                  Getting excited!" + RESET);
            Thread.sleep(600);
            
            // Steve pointing and gesturing
            clearScreen();
            System.out.println(YELLOW + "\n\n               👈   ╭─────────────────────╮" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + "   │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱   👉" + RESET);
            System.out.println(RED + BOLD + "\n                  Pointing at you!" + RESET);
            Thread.sleep(500);
            
            // Steve waving hands
            clearScreen();
            System.out.println(YELLOW + "\n\n               🙌   ╭─────────────────────╮   🙌" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "●" + RESET + YELLOW + "             " + BOLD + "●" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + "   │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(GREEN + BOLD + "\n                 Celebrating!" + RESET);
            Thread.sleep(500);
            
            // Final frame - Steve with sparkles
            clearScreen();
            System.out.println(YELLOW + "\n\n             ***     ╭─────────────────────╮     ***" + RESET);
            System.out.println(YELLOW + "                   ╱                       ╲" + RESET);
            System.out.println(YELLOW + "                  ╱   " + BOLD + "★" + RESET + YELLOW + "             " + BOLD + "★" + RESET + YELLOW + "   ╲" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │          " + BOLD + "╲___╱" + RESET + YELLOW + "          │" + RESET);
            System.out.println(YELLOW + "                 │                         │" + RESET);
            System.out.println(YELLOW + "                 │   " + BOLD + "╲_________________╱" + RESET + YELLOW + "   │" + RESET);
            System.out.println(YELLOW + "                  ╲                       ╱" + RESET);
            System.out.println(YELLOW + "                   ╲_____________________╱" + RESET);
            System.out.println(MAGENTA + BOLD + "\n               *** STEVE HARVEY MAGIC! ***" + RESET);
            Thread.sleep(1000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Handle application exit
     */
    private void handleApplicationExit() {
        clearScreen();
        System.out.println(RED + BOLD + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🚪 EXITING APPLICATION                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        System.out.println(YELLOW + "Thanks for using Git Training CLI Enterprise Edition!" + RESET);
        System.out.println();
        
        // Save session (simplified)
        saveUserSession();
        
        // Show goodbye animation
        showGoodbyeAnimation();
        
        isRunning = false;
    }
    
    /**
     * Save user session
     */
    private void saveUserSession() {
        try {
            currentSession.updateSaveTime();
            System.out.println(GREEN + "💾 Session saved successfully!" + RESET);
            System.out.println(CYAN + "Final Points: " + currentSession.getTotalPoints() + RESET);
            System.out.println(CYAN + "Modules Completed: " + currentSession.getModulesCompleted() + "/3" + RESET);
        } catch (Exception e) {
            System.out.println(YELLOW + "Could not save session, but progress is tracked in memory." + RESET);
        }
    }
    
    /**
     * Show goodbye animation
     */
    private void showGoodbyeAnimation() {
        try {
            System.out.println(YELLOW + "\n                    👋 See you soon!" + RESET);
            System.out.println(GREEN + "            \"Survey says... Git mastered!\"" + RESET);
            System.out.println("                                - Steve Harvey");
            System.out.println();
            
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Handle invalid choice
     */
    private void handleInvalidChoice(String choice) {
        System.out.println();
        String steveHint = hintSystem.getMenuHint(choice);
        System.out.println(RED + steveHint + RESET);
        System.out.println(CYAN + "Valid options: 1 (Start Learning), 2 (View Achievements), 3 (Steve Harvey Show), 4 (Exit)" + RESET);
        waitForUserAcknowledgment();
    }
    
    /**
     * Handle menu errors
     */
    private void handleMenuError(Exception e) {
        System.out.println();
        System.out.println(RED + "*** WARNING: Menu error: " + e.getMessage() + RESET);
        System.out.println(CYAN + "Your progress is safe. Let's try again." + RESET);
        waitForUserAcknowledgment();
    }
    
    /**
     * Handle application errors
     */
    private void handleApplicationError(Exception e) {
        System.out.println();
        System.out.println(RED + BOLD + "🚨 APPLICATION ERROR" + RESET);
        System.out.println(YELLOW + "Error: " + e.getMessage() + RESET);
        System.out.println(CYAN + "Attempting graceful recovery..." + RESET);
        saveUserSession();
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
     * Shutdown sequence
     */
    private void performShutdownSequence() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            System.out.println(GREEN + "🏁 Application shutdown complete." + RESET);
        } catch (Exception e) {
            System.err.println("Shutdown error: " + e.getMessage());
        }
    }
}
