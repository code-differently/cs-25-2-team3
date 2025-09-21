package com.cliapp;

import com.cliapp.io.TestConsole;

public class TestCLIWithInput {
    public static void main(String[] args) {
        System.out.println("🎮 Git Training CLI - Full Demo");
        System.out.println("================================");

        TestConsole testConsole = new TestConsole();

        // Simulate a complete user journey
        testConsole.addInput("1"); // View quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput(""); // Press Enter to begin quest
        testConsole.addInput("2"); // View continue option
        testConsole.addInput("3"); // View badges
        testConsole.addInput("4"); // View glossary
        testConsole.addInput("5"); // Quit

        CLIApplication app = new CLIApplication(testConsole);

        System.out.println("🚀 Starting CLI application demo...\n");
        app.start();

        System.out.println("\n🎉 Demo completed successfully!");
        System.out.println("\n📋 Application Output:");
        System.out.println("=====================");
        for (String output : testConsole.getOutputs()) {
            System.out.print(output);
        }
    }
}
