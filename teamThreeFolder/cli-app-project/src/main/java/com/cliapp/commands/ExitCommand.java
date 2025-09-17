package com.cliapp.commands;

/**
 * Exit command implementation
 */
public class ExitCommand implements Command {
    
    private CLIApplication app;
    
    public ExitCommand(CLIApplication app) {
        // Initialize exit command
    }
    
    @Override
    public void execute(String[] args) {
        // Execute exit command
    }
    
    @Override
    public String getName() {
        return "exit";
    }
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public String getUsage() {
        return "";
    }
    
    @Override
    public boolean validateArgs(String[] args) {
        return true;
    }
}
