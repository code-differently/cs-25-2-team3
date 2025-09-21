package com.cliapp.utils;

/**
 * Utility class for input validation.
 */
public class InputValidator {
    
    /**
     * Check if input is not null or empty.
     */
    public static boolean isValidInput(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Check if input is a valid integer.
     */
    public static boolean isValidInteger(String input) {
        if (!isValidInput(input)) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Check if input is within valid range.
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Check if input is a valid choice from available options.
     */
    public static boolean isValidChoice(String input, int maxOptions) {
        if (!isValidInteger(input)) {
            return false;
        }
        int choice = Integer.parseInt(input.trim());
        return isInRange(choice, 1, maxOptions);
    }
    
    /**
     * Sanitize user input by trimming whitespace.
     */
    public static String sanitizeInput(String input) {
        return input != null ? input.trim() : "";
    }
}
