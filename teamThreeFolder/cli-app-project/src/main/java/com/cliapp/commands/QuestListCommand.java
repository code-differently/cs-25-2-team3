package com.cliapp.commands;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;
import com.cliapp.exceptions.InvalidInputException;
import com.cliapp.io.Console;
import com.cliapp.services.QuestGameService;
import java.util.List;

/**
 * Command to display the quest list with learning modules, difficulty, and completion status SOLID:
 * Single Responsibility - handles only quest list display and selection
 */
public class QuestListCommand implements Command {

    private QuestCollection questCollection;
    private QuestGameService questGameService;
    private Console console;
    private boolean isTestMode;

    public QuestListCommand(QuestCollection questCollection) {
        this.questCollection = questCollection;
        this.console = new com.cliapp.io.SystemConsole();
        this.questGameService = new QuestGameService(console);
        this.isTestMode = false;
    }

    // Constructor for testing
    public QuestListCommand(QuestCollection questCollection, boolean testMode) {
        this.questCollection = questCollection;
        this.console = new com.cliapp.io.SystemConsole();
        this.questGameService = new QuestGameService(console);
        this.isTestMode = testMode;
    }

    // Constructor for dependency injection
    public QuestListCommand(QuestCollection questCollection, Console console, boolean testMode) {
        this.questCollection = questCollection;
        this.console = console;
        this.questGameService = new QuestGameService(console);
        this.isTestMode = testMode;
    }

    @Override
    public void execute(String[] args) {
        try {
            displayQuestList();
            if (!isTestMode) {
                handleQuestSelection();
            }
        } catch (Exception e) {
            System.err.println("Error executing quest command: " + e.getMessage());
            console.println("Unable to display quests at this time.");
        }
    }

    @Override
    public String getDescription() {
        return "Display all available quests with their learning modules, difficulty, and completion status";
    }

    @Override
    public String getName() {
        return "quest";
    }

    @Override
    public String getUsage() {
        return "quest";
    }

    @Override
    public boolean validateArgs(String[] args) {
        // Quest command doesn't require any arguments
        return true;
    }

    /** Display all quests with their learning modules, difficulty, and completion status */
    private void displayQuestList() {
        // Clear screen and show only quest menu (skip in test mode)
        if (!isTestMode) {
            console.print("\033[2J\033[H");
        }

        console.println("🎮 === Available Learning Quests ===");
        console.println("Select a quest to begin your Git learning journey!");
        console.println("─".repeat(60));

        List<Quest> allQuests = questCollection.getAllQuests();

        if (allQuests == null || allQuests.isEmpty()) {
            console.println("No quests available. Please contact your instructor.");
            return;
        }

        for (int i = 0; i < allQuests.size(); i++) {
            Quest quest = allQuests.get(i);
            if (quest != null) {
                console.println(String.format("%d. %s", i + 1, formatQuestDisplay(quest)));
                console.println("");
            }
        }

        console.println(String.format("Total quests available: %d", allQuests.size()));
        console.println("─".repeat(60));
        console.println("Legend: * = Easy, *** = Medium, ***** = Hard");
        console.println("Status: Y = Completed, N = Not Completed");
        console.println("");
    }

    /** Handle quest selection from user input */
    private void handleQuestSelection() {
        List<Quest> allQuests = questCollection.getAllQuests();

        while (true) {
            console.print(
                    String.format(
                            "Enter quest number (1-%d) or 'back' to return to main menu: ",
                            allQuests.size()));
            String input = console.readLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return;
            }

            try {
                if (input.isEmpty()) {
                    throw InvalidInputException.forEmptyInput();
                }

                int questNumber = Integer.parseInt(input);
                if (questNumber >= 1 && questNumber <= allQuests.size()) {
                    Quest selectedQuest = allQuests.get(questNumber - 1);
                    startQuest(selectedQuest);
                    return;
                } else {
                    throw InvalidInputException.forInvalidQuestNumber(input, allQuests.size());
                }
            } catch (NumberFormatException e) {
                console.println("❌ Please enter a valid number or 'back'.");
            } catch (InvalidInputException e) {
                console.println("❌ " + e.getMessage());
            }
        }
    }

    /** Start the selected quest */
    private void startQuest(Quest quest) {
        console.println(String.format("🚀 Starting quest: %s", quest.getName()));
        console.println("Get ready for your Git learning adventure!");
        console.println("\nPress Enter to begin...");
        console.readLine();

        // Map quest difficulty to question level
        String questionLevel = mapDifficultyToLevel(quest.getDifficultyLevel());
        
        // Start the interactive quest game
        questGameService.playQuest(questionLevel);

        // Mark quest as completed (simplified for now)
        quest.setCompleted(true);
    }
    
    /** Map quest difficulty number to question level string */
    private String mapDifficultyToLevel(int difficultyLevel) {
        switch (difficultyLevel) {
            case 1:
                return "Beginner";
            case 3:
                return "Intermediate";
            case 5:
                return "Advanced";
            default:
                return "Beginner"; // Default fallback
        }
    }

    /** Format a single quest for display */
    private String formatQuestDisplay(Quest quest) {
        StringBuilder sb = new StringBuilder();

        // Quest title with difficulty and completion status
        sb.append(
                String.format(
                        "📚 %s [%s] - Status: %s",
                        quest.getName(),
                        quest.getDifficultyAsAsterisks(),
                        quest.getCompletionStatus()));

        sb.append("\n   Description: ").append(quest.getDescription());

        // Learning modules
        sb.append("\n   📖 Learning Modules:");
        List<String> modules = quest.getLearningModules();
        for (int i = 0; i < modules.size(); i++) {
            sb.append(String.format("\n      %d. %s", i + 1, modules.get(i)));
        }

        return sb.toString();
    }
}
