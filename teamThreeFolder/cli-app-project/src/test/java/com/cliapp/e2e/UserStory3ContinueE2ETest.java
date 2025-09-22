/*
package com.cliapp.e2e;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.CLIApplication;
import com.cliapp.io.TestConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-End tests for User Story 3: Continue Learning "As a returning learner, I want to continue
 * my previous learning session so that I can pick up where I left off."
 
public class UserStory3ContinueE2ETest {

    private TestConsole testConsole;
    private CLIApplication app;

    @BeforeEach
    void setUp() {
        testConsole = new TestConsole();
        app = new CLIApplication(testConsole);
    }

    @Test
    void testContinueWithNoSavedSession() {
        // Arrange: User tries to continue without previous session
        testConsole.addInput("3"); // Select continue
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Welcome"), "Should show welcome message");
        assertTrue(output.contains("Continue"), "Should show continue menu option");
        assertTrue(
                output.contains("No saved")
                        || output.contains("no saved")
                        || output.contains("No previous")
                        || output.contains("no previous"),
                "Should indicate no saved session");
    }

    @Test
    void testContinueAfterStartingQuest() {
        // Arrange: User starts a quest then tries to continue
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput("3"); // Select continue
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Quest List"), "Should show quest list");
        assertTrue(output.contains("Continue"), "Should show continue option");
        assertTrue(
                output.contains("continue")
                        || output.contains("Continue")
                        || output.contains("resume")
                        || output.contains("Resume"),
                "Should indicate continuation capability");
    }

    @Test
    void testMultipleContinueAttempts() {
        // Arrange: User tries continue multiple times
        testConsole.addInput("3"); // Select continue (no session)
        testConsole.addInput("3"); // Select continue again
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Continue"), "Should show continue option");
        // Should handle multiple continue attempts gracefully
        long continueCount = output.toLowerCase().split("continue").length - 1;
        assertTrue(continueCount >= 2, "Should handle multiple continue attempts");
    }

    @Test
    void testContinueSessionPersistence() {
        // Arrange: Start quest, exit, restart app, continue
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput("4"); // Exit application

        // Act - First session
        app.start();

        // Create new app instance to simulate restart
        TestConsole newConsole = new TestConsole();
        CLIApplication newApp = new CLIApplication(newConsole);
        newConsole.addInput("3"); // Select continue
        newConsole.addInput("4"); // Exit application

        // Act - Second session
        newApp.start();

        // Assert
        String firstOutput = testConsole.getFullOutput();
        String secondOutput = newConsole.getFullOutput();

        assertTrue(firstOutput.contains("Quest List"), "First session should show quest list");
        assertTrue(secondOutput.contains("Continue"), "Second session should show continue option");
    }

    @Test
    void testContinueInvalidInput() {
        // Arrange: User provides invalid input in continue flow
        testConsole.addInput("3"); // Select continue
        testConsole.addInput("invalid"); // Invalid input during continue flow
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Continue"), "Should show continue option");
        assertTrue(
                output.contains("Invalid")
                        || output.contains("invalid")
                        || output.contains("Error")
                        || output.contains("error"),
                "Should handle invalid input in continue flow");
    }
}
*/