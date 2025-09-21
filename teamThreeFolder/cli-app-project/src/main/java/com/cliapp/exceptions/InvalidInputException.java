package com.cliapp.exceptions;

/** Custom exception for invalid user input Requirement: Implement at least two custom exceptions */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Convenience method for input validation errors */
    public static InvalidInputException forInvalidMenuChoice(String input) {
        return new InvalidInputException(
                "Invalid menu choice: '" + input + "'. Please enter a number between 1-5.");
    }

    /** Convenience method for quest selection errors */
    public static InvalidInputException forInvalidQuestNumber(String input, int maxQuests) {
        return new InvalidInputException(
                "Invalid quest number: '"
                        + input
                        + "'. Please enter a number between 1-"
                        + maxQuests
                        + ".");
    }

    /** Convenience method for empty input errors */
    public static InvalidInputException forEmptyInput() {
        return new InvalidInputException("Input cannot be empty. Please provide a valid response.");
    }
}
