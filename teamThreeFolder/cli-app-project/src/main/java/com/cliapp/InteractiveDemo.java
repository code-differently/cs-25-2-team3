package com.cliapp;

import com.cliapp.io.TestConsole;
import java.util.Scanner;

public class InteractiveDemo {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        TestConsole testConsole = new TestConsole();

        System.out.println("🎮 Git Training CLI - Interactive User Testing");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("Welcome! I'll walk you through the CLI application.");
        System.out.println("You'll see the menus and can choose what to test.");
        System.out.println();

        CLIApplication app = new CLIApplication(testConsole);

        while (true) {
            System.out.println("📋 What would you like to test?");
            System.out.println("1. 📚 View Quest List");
            System.out.println("2. 🎯 Complete a Quest");
            System.out.println("3. 🏆 Check Badges");
            System.out.println("4. 📖 Browse Glossary");
            System.out.println("5. ⚡ Test Continue Feature");
            System.out.println("6. 🚪 Exit Demo");
            System.out.print("\nChoose an option (1-6): ");

            String choice = userInput.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1":
                    demonstrateQuestList(testConsole, app);
                    break;
                case "2":
                    demonstrateQuestCompletion(testConsole, app);
                    break;
                case "3":
                    demonstrateBadges(testConsole, app);
                    break;
                case "4":
                    demonstrateGlossary(testConsole, app);
                    break;
                case "5":
                    demonstrateContinue(testConsole, app);
                    break;
                case "6":
                    System.out.println("👋 Thanks for testing the Git Training CLI!");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.\n");
                    continue;
            }

            System.out.println("\n" + "=".repeat(50));
            System.out.println("Press Enter to continue...");
            userInput.nextLine();
            System.out.println();
        }
    }

    private static void demonstrateQuestList(TestConsole console, CLIApplication app) {
        System.out.println("🎬 Demonstrating: Quest List View");
        System.out.println("Action: User selects option 1 (Quest List) then 'back'");

        console.clearOutputs();
        console.addInput("1"); // View quest list
        console.addInput("back"); // Return to main menu
        console.addInput("5"); // Quit

        app.start();

        System.out.println("\n📺 Application Output:");
        System.out.println("-".repeat(40));
        for (String output : console.getOutputs()) {
            System.out.print(output);
        }
    }

    private static void demonstrateQuestCompletion(TestConsole console, CLIApplication app) {
        System.out.println("🎬 Demonstrating: Quest Completion");
        System.out.println("Action: User selects quest 1, completes it");

        console.clearOutputs();
        console.addInput("1"); // View quest list
        console.addInput("1"); // Select first quest
        console.addInput(""); // Press Enter to begin
        console.addInput("5"); // Quit

        app.start();

        System.out.println("\n📺 Application Output:");
        System.out.println("-".repeat(40));
        for (String output : console.getOutputs()) {
            System.out.print(output);
        }
    }

    private static void demonstrateBadges(TestConsole console, CLIApplication app) {
        System.out.println("🎬 Demonstrating: Badge System");
        System.out.println("Action: User checks their badges and achievements");

        console.clearOutputs();
        console.addInput("3"); // View badges
        console.addInput("5"); // Quit

        app.start();

        System.out.println("\n📺 Application Output:");
        System.out.println("-".repeat(40));
        for (String output : console.getOutputs()) {
            System.out.print(output);
        }
    }

    private static void demonstrateGlossary(TestConsole console, CLIApplication app) {
        System.out.println("🎬 Demonstrating: Git Command Glossary");
        System.out.println("Action: User browses the Git command reference");

        console.clearOutputs();
        console.addInput("4"); // View glossary
        console.addInput("5"); // Quit

        app.start();

        System.out.println("\n📺 Application Output:");
        System.out.println("-".repeat(40));
        for (String output : console.getOutputs()) {
            System.out.print(output);
        }
    }

    private static void demonstrateContinue(TestConsole console, CLIApplication app) {
        System.out.println("🎬 Demonstrating: Continue Feature");
        System.out.println("Action: User tries to continue without an active quest");

        console.clearOutputs();
        console.addInput("2"); // Try continue
        console.addInput("5"); // Quit

        app.start();

        System.out.println("\n📺 Application Output:");
        System.out.println("-".repeat(40));
        for (String output : console.getOutputs()) {
            System.out.print(output);
        }
    }
}
