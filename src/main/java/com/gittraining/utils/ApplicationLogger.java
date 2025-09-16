package com.gittraining.utils;

/**
 * Application Logger - Singleton logging utility
 * 
 * Professional logging implementation following Google's internal standards.
 * Provides structured logging with different levels and proper formatting.
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 */
public class ApplicationLogger {
    
    private static ApplicationLogger instance;
    private boolean debugMode = false;
    
    private ApplicationLogger() {
        // Private constructor for singleton pattern
    }
    
    /**
     * Get the singleton instance of the logger
     * 
     * @return ApplicationLogger instance
     */
    public static synchronized ApplicationLogger getInstance() {
        if (instance == null) {
            instance = new ApplicationLogger();
        }
        return instance;
    }
    
    /**
     * Log an info message
     * 
     * @param message The message to log
     */
    public void info(String message) {
        log("INFO", message, null);
    }
    
    /**
     * Log an error message with exception
     * 
     * @param message The error message
     * @param throwable The exception
     */
    public void error(String message, Throwable throwable) {
        log("ERROR", message, throwable);
    }
    
    /**
     * Log a debug message
     * 
     * @param message The debug message
     */
    public void debug(String message) {
        if (debugMode) {
            log("DEBUG", message, null);
        }
    }
    
    /**
     * Log a warning message
     * 
     * @param message The warning message
     */
    public void warn(String message) {
        log("WARN", message, null);
    }
    
    /**
     * Internal logging method
     * 
     * @param level Log level
     * @param message Message to log
     * @param throwable Optional exception
     */
    private void log(String level, String message, Throwable throwable) {
        String timestamp = java.time.LocalDateTime.now().toString();
        String logMessage = String.format("[%s] %s - %s", timestamp, level, message);
        
        if ("ERROR".equals(level)) {
            System.err.println(logMessage);
            if (throwable != null) {
                throwable.printStackTrace();
            }
        } else {
            System.out.println(logMessage);
        }
    }
    
    /**
     * Enable debug mode
     * 
     * @param debug Whether to enable debug logging
     */
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }
}
