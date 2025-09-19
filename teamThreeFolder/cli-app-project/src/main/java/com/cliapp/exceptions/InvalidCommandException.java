package com.cliapp.exceptions;

/**
 * Custom exception for invalid CLI commands
 * Requirement: Implement at least two custom exceptions
 */
public class InvalidCommandException extends Exception {
    
    public InvalidCommandException(String message) {
        super(message);
    }
    
    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
