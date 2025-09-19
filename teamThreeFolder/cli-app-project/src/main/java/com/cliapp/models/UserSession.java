package com.cliapp.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * User session data model
 */
public class UserSession {
    
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime lastActivity;
    private List<String> commandHistory;
    private int totalCommands;
    private boolean isActive;
    
    public UserSession() {
        this.commandHistory = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
        this.isActive = true;
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
        // Add command to history
    }
    
    public void updateActivity() {
        // Update last activity time
    }
    
    public void endSession() {
        // End user session
    }
}
