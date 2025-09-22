package com.cliapp.utils;

import com.cliapp.io.Console;

/** Utility class for console operations. */
public class ConsoleUtils {

    /** Display a formatted header. */
    public static void displayHeader(Console console, String title) {
        console.println("");
        console.println("=".repeat(title.length() + 4));
        console.println("  " + title + "  ");
        console.println("=".repeat(title.length() + 4));
        console.println("");
    }

    /** Display a separator line. */
    public static void displaySeparator(Console console) {
        console.println("-".repeat(50));
    }

    /** Display a formatted message with padding. */
    public static void displayFormattedMessage(Console console, String message) {
        console.println("");
        console.println("  " + message);
        console.println("");
    }

    /** Prompt user for input with a message. */
    public static String promptForInput(Console console, String prompt) {
        console.print(prompt + ": ");
        return console.readLine();
    }

    /** Display an error message. */
    public static void displayError(Console console, String error) {
        console.println("");
        console.println("ERROR: " + error);
        console.println("");
    }

    /** Display a success message. */
    public static void displaySuccess(Console console, String message) {
        console.println("");
        console.println("SUCCESS: " + message);
        console.println("");
    }
}
