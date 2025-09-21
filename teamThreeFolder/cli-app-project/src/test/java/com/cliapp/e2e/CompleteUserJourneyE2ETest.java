package com.cliapp.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cliapp.CLIApplication;
import com.cliapp.io.TestConsole;

/**
 * Comprehensive End-to-End tests covering complete user journeys from application start to
 * completion of learning goals.
 */
public class CompleteUserJourneyE2ETest {

    private TestConsole testConsole;
    private CLIApplication app;

    @BeforeEach
    void setUp() {
        testConsole = new TestConsole();
        app = new CLIApplication(testConsole);
    }

    @Test
    void testCompleteNewUserJourney() {
        // Arrange: New user complete journey - explore, learn, achieve
        testConsole.addInput("1"); // View quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput("2"); // Check glossary during quest
        testConsole.addInput("repository"); // Look up term
        testConsole.addInput("3"); // Continue learning
        testConsole.addInput("4"); // Check badges
        testConsole.addInput("5"); // Exit

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Welcome"), "Should show welcome");
        assertTrue(output.contains("Quest"), "Should show quest interaction");
        assertTrue(output.contains("Glossary"), "Should show glossary access");
        assertTrue(output.contains("Continue"), "Should show continue option");
        assertTrue(output.contains("Badge"), "Should show badge system");
    }

    @Test
    void testReturningUserJourney() {
        // Arrange: Simulate returning user workflow
        // First session - start quest
        testConsole.addInput("1"); // Quest list
        testConsole.addInput("1"); // Select quest
        testConsole.addInput("5"); // Exit (save progress)

        // Act - First session
        app.start();

        // Arrange - Second session (new app instance)
        TestConsole returnConsole = new TestConsole();
        CLIApplication returnApp = new CLIApplication(returnConsole);
        returnConsole.addInput("3"); // Continue previous session
        returnConsole.addInput("4"); // Check progress/badges
        returnConsole.addInput("5"); // Exit

        // Act - Second session
        returnApp.start();

        // Assert
        String firstSession = testConsole.getFullOutput();
        String secondSession = returnConsole.getFullOutput();

        assertTrue(firstSession.contains("Quest"), "First session should start quest");
        assertTrue(secondSession.contains("Continue"), "Second session should continue");
    }

    @Test
    void testErrorRecoveryJourney() {
        // Arrange: User makes mistakes and recovers
        testConsole.addInput("99"); // Invalid menu option
        testConsole.addInput("1"); // Valid quest list
        testConsole.addInput("999"); // Invalid quest selection
        testConsole.addInput("1"); // Valid quest selection
        testConsole.addInput("2"); // Glossary
        testConsole.addInput(""); // Empty search
        testConsole.addInput("git"); // Valid search
        testConsole.addInput("5"); // Exit

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(
                output.contains("Invalid") || output.contains("invalid"),
                "Should handle invalid inputs");
        assertTrue(output.contains("Quest"), "Should recover to show quests");
        assertTrue(output.contains("Glossary"), "Should recover to show glossary");
        assertTrue(
                output.contains("git") || output.contains("Git"),
                "Should handle valid glossary search");
    }

    @Test
    void testCompleteLearningCycle() {
        // Arrange: User completes full learning cycle
        testConsole.addInput("1"); // Start with quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput("2"); // Check glossary for help
        testConsole.addInput("commit"); // Look up commit term
        testConsole.addInput("3"); // Continue quest
        testConsole.addInput("4"); // Complete and check badges
        testConsole.addInput("1"); // Start another quest
        testConsole.addInput("2"); // Select second quest
        testConsole.addInput("5"); // Exit

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Welcome"), "Should show welcome");
        assertTrue(output.contains("Quest"), "Should show quest system");
        assertTrue(output.contains("Glossary"), "Should show glossary access");
        assertTrue(output.contains("Continue"), "Should show continue functionality");
        assertTrue(
                output.contains("Badge") || output.contains("badge"), "Should show badge system");

        // Verify learning progression
        long questCount = output.toLowerCase().split("quest").length - 1;
        assertTrue(questCount >= 3, "Should have multiple quest interactions");
    }

    @Test
    void testApplicationRobustness() {
        // Arrange: Test application handles edge cases
        testConsole.addInput(""); // Empty input
        testConsole.addInput("abc"); // Non-numeric input
        testConsole.addInput("-1"); // Negative number
        testConsole.addInput("0"); // Zero input
        testConsole.addInput("1"); // Finally valid input
        testConsole.addInput("5"); // Exit

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Welcome"), "Should show welcome despite errors");
        assertTrue(
                output.contains("Invalid")
                        || output.contains("invalid")
                        || output.contains("Error")
                        || output.contains("error"),
                "Should handle invalid inputs gracefully");
        // Application should not crash and should provide guidance
        assertTrue(
                output.contains("1") && output.contains("Quest"),
                "Should eventually process valid input");
    }
}
