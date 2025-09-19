package com.cliapp.commands;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;

/**
 * Command to display the quest list with learning modules, difficulty, and completion status
 * SOLID: Single Responsibility - handles only quest list display
 */
public class QuestListCommand implements Command {
    
    private QuestCollection questCollection;
    
    public QuestListCommand(QuestCollection questCollection) {
        this.questCollection = questCollection;
    }
    
    @Override
    public void execute(String[] args) {
        // TODO: Implement quest list display
        displayQuestList();
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
    
    /**
     * Display all quests with their learning modules, difficulty, and completion status
     */
    private void displayQuestList() {
        // TODO: Implement quest list display logic
        // Should show:
        // - Quest name
        // - Learning modules (strings explaining what to learn)
        // - Difficulty as asterisks (*, ***, *****)
        // - Completion status (Y/N)
    }
    
    /**
     * Format a single quest for display
     */
    private String formatQuestDisplay(Quest quest) {
        // TODO: Implement quest formatting for CLI display
        return null;
    }
}
