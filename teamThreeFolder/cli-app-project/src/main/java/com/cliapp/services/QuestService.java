package com.cliapp.services;

import java.util.List;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;

/**
 * Service class for managing quest operations
 * SOLID: Single Responsibility - handles quest business logic
 */
public class QuestService {
    
    private final QuestCollection questCollection;
    
    public QuestService() {
        this.questCollection = new QuestCollection();
        initializeDefaultQuests();
    }
    
    public QuestService(QuestCollection questCollection) {
        this.questCollection = questCollection;
    }
    
    /**
     * Initialize default quests for the CLI application
     */
    public void initializeDefaultQuests() {
        // TODO: Implement - create some default quests with learning modules
        // Example quest with Git learning modules:
        // - "Learn Git basics: init, add, commit"
        // - "Understand branching and merging"
        // - "Work with remote repositories"
    }
    
    /**
     * Get all quests
     */
    public List<Quest> getAllQuests() {
        // TODO: Implement - return all quests from collection
        return null;
    }
    
    /**
     * Get quest by ID
     */
    public Quest getQuestById(String id) {
        // TODO: Implement - get specific quest by ID
        return null;
    }
    
    /**
     * Mark quest as completed
     */
    public boolean markQuestAsCompleted(String questId) {
        // TODO: Implement - find quest and mark as completed
        return false;
    }
    
    /**
     * Get quests by difficulty
     */
    public List<Quest> getQuestsByDifficulty(int difficulty) {
        // TODO: Implement - filter quests by difficulty level
        return null;
    }
    
    /**
     * Get completed quests
     */
    public List<Quest> getCompletedQuests() {
        // TODO: Implement - return only completed quests
        return null;
    }
    
    /**
     * Get incomplete quests
     */
    public List<Quest> getIncompleteQuests() {
        // TODO: Implement - return only incomplete quests
        return null;
    }
}
