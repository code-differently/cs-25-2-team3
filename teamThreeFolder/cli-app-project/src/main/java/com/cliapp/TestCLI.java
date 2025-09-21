package com.cliapp;

import com.cliapp.io.SystemConsole;

public class TestCLI {
    public static void main(String[] args) {
        System.out.println("Testing CLI Application...");

        // Test basic console functionality
        SystemConsole console = new SystemConsole();
        console.println("Hello from console!");

        // Test CLIApplication constructor
        CLIApplication app = new CLIApplication(console);
        System.out.println("CLIApplication created successfully");

        // Don't call start() to avoid the infinite loop
        System.out.println("Test completed successfully");
    }
}
