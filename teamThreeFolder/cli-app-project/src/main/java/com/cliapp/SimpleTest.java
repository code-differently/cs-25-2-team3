package com.cliapp;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("Starting simple test...");

        try {
            System.out.println("Creating CLIApplication...");
            CLIApplication app = new CLIApplication();
            System.out.println("CLIApplication created successfully!");

            // Don't start the application, just create it
            System.out.println("Test completed successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
