package com.cliapp.services;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Service class for managing quest operations SOLID: Single Responsibility - handles quest business
 * logic
 */
public class QuestService {

    private final QuestCollection questCollection;

    private Quest currentQuest; // Added for test expectations
    private double questProgress = 0.0; // Added for test expectations

    public QuestService() {
        this.questCollection = new QuestCollection();
        initializeDefaultQuests();
    }

    public QuestService(QuestCollection questCollection) {
        this.questCollection = questCollection;
    }

    /** Initialize default quests for the CLI application */
    public void initializeDefaultQuests() {
        // Create Git Basics Quest
        List<String> gitBasicsModules =
                Arrays.asList(
                        "Learn Git init command to create a new repository",
                        "Understand git add to stage files for commit",
                        "Master git commit with meaningful messages",
                        "Practice git status to check repository state");
        Quest gitBasics =
                new Quest(
                        "git-basics",
                        "Git Fundamentals",
                        "Learn the essential Git commands to get started",
                        gitBasicsModules,
                        1);
        questCollection.add(gitBasics);

        // Create Branching Quest
        List<String> branchingModules =
                Arrays.asList(
                        "Create new branches with git branch",
                        "Switch between branches using git checkout",
                        "Merge branches with git merge",
                        "Resolve merge conflicts when they occur");
        Quest branching =
                new Quest(
                        "git-branching",
                        "Git Branching & Merging",
                        "Master branching and merging workflows",
                        branchingModules,
                        3);
        questCollection.add(branching);

        // Create Remote Quest
        List<String> remoteModules =
                Arrays.asList(
                        "Clone repositories with git clone",
                        "Push changes to remote with git push",
                        "Pull updates from remote with git pull",
                        "Manage remotes with git remote",
                        "Understand fetch vs pull operations");
        Quest remote =
                new Quest(
                        "git-remote",
                        "Remote Repository Operations",
                        "Work with remote repositories and collaboration",
                        remoteModules,
                        5);
        questCollection.add(remote);
    }

    /** Get all quests */
    public List<Quest> getAllQuests() {
        List<Quest> quests = questCollection.getAllQuests();
        return quests == null ? Collections.emptyList() : quests;
    }

    /** Get quest by ID */
    public Quest getQuestById(String id) {
        return questCollection.getById(id);
    }

    /** Mark quest as completed */
    public boolean markQuestAsCompleted(String questId) {
        Quest quest = questCollection.getById(questId);
        if (quest != null) {
            quest.setCompleted(true);
            return true;
        }
        return false;
    }

    /** Get quests by difficulty */
    public List<Quest> getQuestsByDifficulty(int difficulty) {
        return questCollection.getQuestsByDifficulty(difficulty);
    }

    /** Get completed quests */
    public List<Quest> getCompletedQuests() {
        return questCollection.getQuestsByCompletionStatus(true);
    }

    /** Get the quest collection for direct access */
    public QuestCollection getQuestCollection() {
        return questCollection;
    }

    /** Get incomplete quests */
    public List<Quest> getIncompleteQuests() {
        return questCollection.getQuestsByCompletionStatus(false);
    }

    public boolean startQuest(Quest quest) {
        if (quest == null || quest.getId() == null) {
            return false;
        }
        this.currentQuest = quest;
        // Reset progress when starting a new quest
        this.questProgress = 0.0;
        return true;
    }

    public boolean startQuest(String questId) {
        Quest quest = requireQuest(questId);
        return startQuest(quest);
    }

    private Quest requireQuest(String id) {
        Quest quest = questCollection.getById(id);
        if (quest == null) {
            throw new IllegalArgumentException("Unknown quest: " + id);
        }
        return quest;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public boolean completeCurrentQuest() {
        if (currentQuest == null) {
            return false;
        }
        currentQuest.setCompleted(true);
        currentQuest = null;
        questProgress = 0.0;
        return true;
    }

    public boolean recieveBadgeScore(int score) {
        if (currentQuest == null || !currentQuest.isCompleted()) {
            return false;
        }
        // Logic to award badge/score can be implemented here
        return true;
    }

    public boolean isQuestActive() {
        return currentQuest != null;
    }

    public double getQuestProgress() {
        return questProgress;
    }

    public void setQuestProgress(double progress) {
        this.questProgress = progress;
    }

    public void resetProgress() {
        this.questProgress = 0.0;
    }
}
