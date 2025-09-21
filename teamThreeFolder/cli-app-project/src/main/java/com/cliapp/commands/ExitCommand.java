package com.cliapp.commands;

import com.cliapp.io.Console;

/**
 * Command to exit the application gracefully.
 */
public class ExitCommand implements Command {
    
    private final Console console;
    
    public ExitCommand(Console console) {
        this.console = console;
    }
    
    @Override
    public void execute(String[] args) {
        console.println("Thank you for using the CLI Learning Application!");
        console.println("Goodbye!");
        System.exit(0);
    }
    
    @Override
    public String getName() {
        return "exit";
    }
    
    @Override
    public String getDescription() {
        return "Exit the application";
    }
    
    @Override
    public String getUsage() {
        return "exit";
    }
    
    @Override
    public boolean validateArgs(String[] args) {
        return true; // No arguments required
    }
}
