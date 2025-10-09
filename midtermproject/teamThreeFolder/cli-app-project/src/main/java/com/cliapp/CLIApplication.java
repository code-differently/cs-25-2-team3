package com.cliapp;

import com.cliapp.commands.BadgeCommand;
import com.cliapp.commands.ContinueCommand;
import com.cliapp.commands.GlossaryCommand;
import com.cliapp.commands.QuestListCommand;
import com.cliapp.exceptions.InvalidInputException;
import com.cliapp.exceptions.NoSavedGameException;
import com.cliapp.io.Console;
import com.cliapp.io.SystemConsole;
import com.cliapp.models.UserSession;
import com.cliapp.services.BadgeManager;
import com.cliapp.services.BadgeService;
import com.cliapp.services.GlossaryService;
import com.cliapp.services.QuestService;

/** Main CLI Application Entry Point Implements User Stories 1, 2, and 3 */
public class CLIApplication {

    private final Console console;
    private boolean isRunning;
    private UserSession userSession;
    private QuestService questService;
    private GlossaryService glossaryService;
    private BadgeManager badgeManager;
    private BadgeService badgeService;

    public CLIApplication() {
        this(new SystemConsole());
    }

    // Constructor for testing
    public CLIApplication(Console console) {
        this.console = console;
        this.isRunning = false;
        this.userSession = new UserSession();
        this.questService = new QuestService();
        this.glossaryService = new GlossaryService();
        this.badgeService = new BadgeService();
        this.badgeManager = new BadgeManager(this.badgeService, this.questService);
    }

    public static void main(String[] args) {
        CLIApplication app = new CLIApplication();
        app.start();
    }

    public void start() {
        this.isRunning = true;
        showWelcome();

        while (isRunning) {
            showMainMenu();
            processInput();
        }

        stop();
    }

    public void stop() {
        this.isRunning = false;
        userSession.endSession();
        console.println("\n👋 Thanks for using Git Training CLI! Keep practicing!");
        console.close();
    }

    private void showWelcome() {
        console.println("╔═══════════════════════════════════════╗");
        console.println("║        🚀 Git Training CLI 🚀        ║");
        console.println("║   Master Git through Interactive     ║");
        console.println("║       Learning Adventures!           ║");
        console.println("╚═══════════════════════════════════════╝");
        console.println("\nWelcome to your Git learning journey!");
        console.println(userSession.getPointsSummary());
        console.println("");
    }

    private void showMainMenu() {
        console.println("\n=== Main Menu ===");
        console.println("Choose your learning path:");
        console.println("1. 📚 Quest - View and start learning modules");
        console.println("2. ▶️  Continue - Resume your current quest");
        console.println("3. 🏆 Badges - View your achievements");
        console.println("4. 📖 Glossary - Browse Git command references");
        console.println("5. 🚪 Quit - Exit the application");
        console.print("\nEnter your choice (1-5): ");
    }

    private void processInput() {
        try {
            String input = console.readLine().trim();

            if (input.isEmpty()) {
                throw InvalidInputException.forEmptyInput();
            }

            switch (input) {
                case "1":
                    executeQuestCommand();
                    break;
                case "2":
                    executeContinueCommand();
                    break;
                case "3":
                    executeBadgeCommand();
                    break;
                case "4":
                    executeGlossaryCommand();
                    break;
                case "5":
                    console.println("Exiting application...");
                    this.isRunning = false;
                    break;
                default:
                    throw InvalidInputException.forInvalidMenuChoice(input);
            }
        } catch (InvalidInputException e) {
            console.println("❌ " + e.getMessage());
        } catch (Exception e) {
            console.println("❌ An error occurred: " + e.getMessage());
            console.println("Please try again.");
        }
    }

    private void executeQuestCommand() {
        QuestListCommand questCommand =
                new QuestListCommand(
                        questService.getQuestCollection(),
                        badgeManager,
                        console,
                        false); // Interactive mode for production
        questCommand.execute(new String[] {});

        // After quest completion, show updated points
        console.println("\n" + userSession.getPointsSummary());
    }

    private void executeContinueCommand() {
        try {
            if (userSession.getCurrentQuestId() == null
                    || userSession.getCurrentQuestId().isEmpty()) {
                throw NoSavedGameException.forNoActiveQuest();
            }
            ContinueCommand continueCommand = new ContinueCommand(questService, userSession);
            continueCommand.execute(new String[] {});
        } catch (NoSavedGameException e) {
            console.println("❌ " + e.getMessage());
        }
    }

    private void executeBadgeCommand() {
        BadgeCommand badgeCommand = new BadgeCommand(badgeService, badgeManager);
        badgeCommand.execute(new String[] {});
    }

    private void executeGlossaryCommand() {
        GlossaryCommand glossaryCommand = new GlossaryCommand(glossaryService);
        glossaryCommand.execute(new String[] {});
    }
}
