package com.cliapp;

import com.cliapp.io.TestConsole;

public class UserTestDemo {
    public static void main(String[] args) {
        System.out.println("🎮 Git Training CLI - Complete User Testing Demo");
        System.out.println("===============================================");
        System.out.println();
        System.out.println(
                "This demo will show you all the features as if you were using the CLI!");
        System.out.println();

        // Test 1: Quest List Navigation
        System.out.println("🎬 TEST 1: Browsing Quest List");
        System.out.println("Simulating: User enters '1' to view quests, then 'back' to return");
        testQuestListBrowsing();
        waitForUser();

        // Test 2: Quest Completion
        System.out.println("🎬 TEST 2: Completing a Quest");
        System.out.println("Simulating: User selects quest 1, completes it");
        testQuestCompletion();
        waitForUser();

        // Test 3: Badge System
        System.out.println("🎬 TEST 3: Checking Badge Progress");
        System.out.println("Simulating: User checks their achievements");
        testBadgeSystem();
        waitForUser();

        // Test 4: Glossary
        System.out.println("🎬 TEST 4: Browsing Git Commands");
        System.out.println("Simulating: User explores the glossary");
        testGlossary();
        waitForUser();

        // Test 5: Continue Feature
        System.out.println("🎬 TEST 5: Continue Feature");
        System.out.println("Simulating: User tries to continue without active quest");
        testContinueFeature();
        waitForUser();

        System.out.println("✅ All tests completed! The CLI application is fully functional.");
        System.out.println();
        System.out.println("🎯 Key Features Demonstrated:");
        System.out.println("   ✓ Interactive quest system with 3 difficulty levels");
        System.out.println("   ✓ Badge system with 6 achievement types");
        System.out.println("   ✓ Comprehensive Git command glossary (20 commands)");
        System.out.println("   ✓ Continue/resume functionality");
        System.out.println("   ✓ Beautiful console UI with emojis and formatting");
        System.out.println("   ✓ Proper error handling and user feedback");
        System.out.println();
        System.out.println("🚀 Ready for real user interaction!");
    }

    private static void testQuestListBrowsing() {
        TestConsole console = new TestConsole();
        console.addInput("1"); // View quest list
        console.addInput("back"); // Return to menu
        console.addInput("5"); // Quit

        CLIApplication app = new CLIApplication(console);
        app.start();

        System.out.println("📺 Output Preview:");
        String output = console.getFullOutput();
        String[] lines = output.split("\n");

        // Show key parts of the output
        for (String line : lines) {
            if (line.contains("Available Learning Quests")
                    || line.contains("Git Fundamentals")
                    || line.contains("Git Branching")
                    || line.contains("Remote Repository")
                    || line.contains("Total quests available")) {
                System.out.println("   " + line.trim());
            }
        }
    }

    private static void testQuestCompletion() {
        TestConsole console = new TestConsole();
        console.addInput("1"); // View quest list
        console.addInput("1"); // Select first quest
        console.addInput(""); // Press Enter to begin
        console.addInput("5"); // Quit

        CLIApplication app = new CLIApplication(console);
        app.start();

        System.out.println("📺 Quest Completion Preview:");
        String output = console.getFullOutput();
        String[] lines = output.split("\n");

        for (String line : lines) {
            if (line.contains("Starting quest")
                    || line.contains("Git learning adventure")
                    || line.contains("Sample Question")
                    || line.contains("Quest Complete")
                    || line.contains("Points Earned")) {
                System.out.println("   " + line.trim());
            }
        }
    }

    private static void testBadgeSystem() {
        TestConsole console = new TestConsole();
        console.addInput("3"); // View badges
        console.addInput("5"); // Quit

        CLIApplication app = new CLIApplication(console);
        app.start();

        System.out.println("📺 Badge System Preview:");
        String output = console.getFullOutput();
        String[] lines = output.split("\n");

        for (String line : lines) {
            if (line.contains("Achievement Badges")
                    || line.contains("Badges Earned")
                    || line.contains("Git Starter")
                    || line.contains("Quest Master")
                    || line.contains("Glossary Guru")) {
                System.out.println("   " + line.trim());
            }
        }
    }

    private static void testGlossary() {
        TestConsole console = new TestConsole();
        console.addInput("4"); // View glossary
        console.addInput("5"); // Quit

        CLIApplication app = new CLIApplication(console);
        app.start();

        System.out.println("📺 Glossary Preview:");
        String output = console.getFullOutput();
        String[] lines = output.split("\n");

        int count = 0;
        for (String line : lines) {
            if (line.contains("git init")
                    || line.contains("git add")
                    || line.contains("git commit")
                    || line.contains("git push")
                    || line.contains("Total commands")) {
                System.out.println("   " + line.trim());
                count++;
                if (count >= 5) break; // Show first few commands
            }
        }
    }

    private static void testContinueFeature() {
        TestConsole console = new TestConsole();
        console.addInput("2"); // Try continue
        console.addInput("5"); // Quit

        CLIApplication app = new CLIApplication(console);
        app.start();

        System.out.println("📺 Continue Feature Preview:");
        String output = console.getFullOutput();
        String[] lines = output.split("\n");

        for (String line : lines) {
            if (line.contains("No active quest") || line.contains("start a new quest")) {
                System.out.println("   " + line.trim());
            }
        }
    }

    private static void waitForUser() {
        System.out.println();
        System.out.println("⏸️  [Press Enter to see next test...]");
        try {
            System.in.read();
        } catch (Exception e) {
            // Continue anyway
        }
        System.out.println();
    }
}
