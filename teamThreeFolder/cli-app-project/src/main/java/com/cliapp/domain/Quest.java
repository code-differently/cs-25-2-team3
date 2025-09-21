package com.cliapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Quest domain object that holds learning modules with difficulty and completion status SOLID:
 * Single Responsibility - represents a quest with learning modules
 */
public class Quest {

    private String id;
    private String name;
    private String description;
    private List<String> learningModules; // List of strings explaining modules to learn
    private int difficultyLevel; // 1, 3, or 5 asterisks
    private boolean isCompleted;

    public Quest() {
        this.learningModules = new ArrayList<>();
        this.isCompleted = false;
    }

    public Quest(
            String id,
            String name,
            String description,
            List<String> learningModules,
            int difficultyLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.learningModules =
                learningModules != null ? new ArrayList<>(learningModules) : new ArrayList<>();
        this.difficultyLevel = difficultyLevel;
        this.isCompleted = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getLearningModules() {
        return new ArrayList<>(learningModules);
    }

    public void setLearningModules(List<String> learningModules) {
        this.learningModules =
                learningModules != null ? new ArrayList<>(learningModules) : new ArrayList<>();
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    /**
     * Get difficulty as asterisks representation
     *
     * @return String with *, ***, or ***** based on difficulty level
     */
    public String getDifficultyAsAsterisks() {
        switch (difficultyLevel) {
            case 1:
                return "*";
            case 3:
                return "***";
            case 5:
                return "*****";
            default:
                return "*"; // Default to easy if invalid level
        }
    }

    /**
     * Get completion status as Y/N
     *
     * @return "Y" if completed, "N" if not
     */
    public String getCompletionStatus() {
        return isCompleted ? "Y" : "N";
    }

    /** Add a learning module to the quest */
    public void addLearningModule(String module) {
        if (module != null && !module.trim().isEmpty()) {
            learningModules.add(module);
        }
    }

    /** Remove a learning module from the quest */
    public boolean removeLearningModule(String module) {
        return learningModules.remove(module);
    }

    // Adapter methods expected by some tests using 'Title' terminology
    public void setTitle(String title) {
        this.name = title;
    }

    public String getTitle() {
        return this.name;
    }
}
