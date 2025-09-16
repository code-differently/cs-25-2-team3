package com.gittraining;

import com.gittraining.cli.CommandLineInterface;
import com.gittraining.utils.ApplicationLogger;

/**
 * Git Training CLI Application - Main Entry Point
 * 
 * This is the main application class that bootstraps the Git Training CLI.
 * Follows enterprise application architecture patterns with proper separation
 * of concerns and dependency injection principles.
 * 
 * @author Senior Engineering Team
 * @version 1.0.0
 * @since 2025-09-16
 */
public class GitTrainingApplication {
    
    private static final ApplicationLogger logger = ApplicationLogger.getInstance();
    
    /**
     * Main application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            logger.info("Starting Git Training CLI Application...");
            
            // Bootstrap the application
            CommandLineInterface cli = new CommandLineInterface();
            
            // Register shutdown hook for graceful cleanup
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Application shutdown initiated");
                cli.shutdown();
            }));
            
            // Start the application
            cli.start();
            
        } catch (Exception e) {
            logger.error("Fatal error during application startup", e);
            System.err.println("Application failed to start: " + e.getMessage());
            System.exit(1);
        }
    }
}
