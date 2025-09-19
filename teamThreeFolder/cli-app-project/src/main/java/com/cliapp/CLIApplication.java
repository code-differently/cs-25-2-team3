package com.cliapp;

import java.util.Scanner;

/**
 * Main CLI Application Entry Point
 */
public class CLIApplication {
    
    private Scanner scanner;
    private boolean isRunning;
    
    public CLIApplication() {
        // Initialize application
    }
    
    public static void main(String[] args) {
        CLIApplication app = new CLIApplication();
        app.start();
    }
    
    /**
     * Starts the CLI application loop.
     * Displays a welcome message and enters a loop to process user input.
     * The loop continues running until the user enters the "exit" command.
     * Recognizes "help" to display help information.
     * Other commands can be processed as needed.
     */
    public void start() {
        showWelcome();
        isRunning = true;
        while (isRunning) {
            String input = processInput();
            if (input.equalsIgnoreCase("exit")) {
                stop();
            } else if (input.equalsIgnoreCase("help")) {
                showHelp();
            } else {
                // Process other commands
            }
        }
    }
    
    /**
     * Stops the CLI application by setting the running flag to false,
     * closing the input scanner if it is open, and printing an exit message.
     */
    public void stop() {
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("Exiting CLI Application. Goodbye!");
    }
    
    // CHANGE LATER WITH OUTPUT CLASS
    private void showWelcome() {
        System.out.println("Welcome to the CLI Application!");
    }
    
    
    /**
     * Prompts the user for input via the console and returns the entered string.
     * <p>
     * This method displays a prompt ("> ") to the user, reads a line of text from standard input,
     * and returns the input as a string.
     *
     * @return the user's input as a {@code String}
     */
    private String processInput() {
        scanner = new Scanner(System.in);
        System.out.print("> ");
        String input = scanner.nextLine();
        return input;
    }
    
    // CHANGE LATER WITH OUTPUT CLASS
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("help - Show this help message");
        System.out.println("exit - Exit the application");
        // List other commands
    }
}
