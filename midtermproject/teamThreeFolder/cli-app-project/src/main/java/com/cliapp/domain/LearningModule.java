package com.cliapp.domain;

/** Learning Module domain object SOLID: Single Responsibility - represents a learning module */
public class LearningModule {

    private String id;
    private String name;
    private String description;
    private int difficulty;
    private boolean completed;
    private double progress;

    public LearningModule() {
        // Default constructor
    }

    public LearningModule(String id, String name, String description, int difficulty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.completed = false;
        this.progress = 0.0;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
