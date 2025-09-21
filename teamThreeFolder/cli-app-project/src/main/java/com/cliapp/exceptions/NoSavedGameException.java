package com.cliapp.exceptions;

/**
 * Custom exception for when no saved game or progress is found Requirement: Implement at least two
 * custom exceptions
 */
public class NoSavedGameException extends Exception {

    public NoSavedGameException(String message) {
        super(message);
    }

    public NoSavedGameException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Convenience method for no active quest */
    public static NoSavedGameException forNoActiveQuest() {
        return new NoSavedGameException(
                "No active quest found. Please start a new quest from the main menu.");
    }

    /** Convenience method for no saved progress */
    public static NoSavedGameException forNoSavedProgress() {
        return new NoSavedGameException(
                "No saved progress found. Your learning journey starts fresh!");
    }

    /** Convenience method for missing user session */
    public static NoSavedGameException forMissingSession() {
        return new NoSavedGameException("User session not found. Please restart the application.");
    }
}
