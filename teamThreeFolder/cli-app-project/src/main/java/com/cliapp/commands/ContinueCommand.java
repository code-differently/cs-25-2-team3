package com.cliapp.commands;

import com.cliapp.domain.Quest;
import com.cliapp.models.UserSession;
import com.cliapp.services.QuestService;

/**
 * Command to continue the current quest or resume progress SOLID: Single Responsibility - handles
 * only quest continuation
 */
public class ContinueCommand implements Command {

    private final QuestService questService;
    private final UserSession userSession;

    public ContinueCommand(QuestService questService, UserSession userSession) {
        this.questService = questService;
        this.userSession = userSession;
    }

    @Override
    public void execute(String[] args) {
        try {
            System.out.println("\n=== Continue Your Quest ===");

            // Check if user has a current quest
            String currentQuestId = userSession.getCurrentQuestId();
            if (currentQuestId == null || currentQuestId.isEmpty()) {
                System.out.println(
                        "No quest in progress. Use 'quest' command to start a new quest.");
                return;
            }

            // Get the current quest
            Quest currentQuest = questService.getQuestById(currentQuestId);
            if (currentQuest == null) {
                System.out.println(
                        "Current quest not found. Use 'quest' command to start a new quest.");
                userSession.setCurrentQuestId(null);
                return;
            }

            // Display current quest progress
            System.out.println("Current Quest: " + currentQuest.getName());
            System.out.println("Status: " + currentQuest.getCompletionStatus());
            System.out.println("Use 'quest' command to interact with learning modules.");
        } catch (Exception e) {
            System.err.println("Error continuing quest: " + e.getMessage());
            System.out.println("Unable to continue quest at this time.");
        }
    }

    @Override
    public String getDescription() {
        return "Continue your current quest or resume progress";
    }

    @Override
    public String getName() {
        return "continue";
    }

    @Override
    public String getUsage() {
        return "continue";
    }

    @Override
    public boolean validateArgs(String[] args) {
        return true; // Continue command doesn't require arguments
    }
}
