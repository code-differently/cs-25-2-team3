package com.cliapp.exceptions;

/**
 * Custom exception for invalid CLI commands Requirement: Implement at least two custom exceptions
 */
public class InvalidCommandException extends Exception {

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Convenience method for unknown command errors */
    public static InvalidCommandException forUnknownCommand(String command) {
        return new InvalidCommandException(
                "Unknown command: '" + command + "'. Type 'help' to see available commands.");
    }

    /** Convenience method for invalid arguments */
    public static InvalidCommandException forInvalidArguments(
            String command, String expectedUsage) {
        return new InvalidCommandException(
                "Invalid arguments for command '" + command + "'. Usage: " + expectedUsage);
    }

    /** Convenience method for missing required arguments */
    public static InvalidCommandException forMissingArguments(String command, String missingArgs) {
        return new InvalidCommandException(
                "Missing required arguments for command '" + command + "': " + missingArgs);
    }
}
