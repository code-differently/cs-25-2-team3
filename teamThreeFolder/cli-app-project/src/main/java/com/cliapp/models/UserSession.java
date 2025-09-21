package com.cliapp.models;

import com.cliapp.domain.Badge;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** User session data model */
public class UserSession {

    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime lastActivity;
    private List<String> commandHistory;
    private int totalCommands;
    private boolean isActive;
    private String currentQuestId;
    private int totalPoints;
    private List<Badge> earnedBadges;
    private List<String> completedQuests;

    public UserSession() {
        this.commandHistory = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
        this.isActive = true;
        this.earnedBadges = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
        this.totalPoints = 0;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public List<String> getCommandHistory() {
        return commandHistory;
    }

    public void setCommandHistory(List<String> commandHistory) {
        this.commandHistory = commandHistory;
    }

    public int getTotalCommands() {
        return totalCommands;
    }

    public void setTotalCommands(int totalCommands) {
        this.totalCommands = totalCommands;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Utility methods
    public void addCommand(String command) {
        commandHistory.add(command);
        totalCommands++;
        updateActivity();
    }

    public void updateActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    public void endSession() {
        this.isActive = false;
    }

    // Quest and Badge tracking methods
    public String getCurrentQuestId() {
        return currentQuestId;
    }

    public void setCurrentQuestId(String currentQuestId) {
        this.currentQuestId = currentQuestId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<Badge> getEarnedBadges() {
        return new ArrayList<>(earnedBadges);
    }

    public void addBadge(Badge badge) {
        if (badge != null) {
            this.earnedBadges.add(badge);
            this.totalPoints += badge.getPointsEarned();
        }
    }

    public List<String> getCompletedQuests() {
        return new ArrayList<>(completedQuests);
    }

    public void markQuestCompleted(String questId) {
        if (questId != null && !questId.trim().isEmpty() && !completedQuests.contains(questId)) {
            completedQuests.add(questId);
        }
    }

    public boolean hasCompletedQuest(String questId) {
        return completedQuests.contains(questId);
    }

    /** Get points summary for display */
    public String getPointsSummary() {
        return String.format(
                "Total Points: %d | Badges Earned: %d", totalPoints, earnedBadges.size());
    }

    // Helper methods for BadgeService
    public List<String> getCompletedQuestIds() {
        return getCompletedQuests();
    }

    public int getGlossaryLookupCount() {
        return 0; // Placeholder until glossary tracking implemented
    }
}
