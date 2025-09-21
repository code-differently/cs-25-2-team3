package com.cliapp.e2e;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.CLIApplication;
import com.cliapp.io.TestConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-End tests for User Story 1: Quest List Display "As a new learner, I want to see a list of
 * available quests so that I can choose which Git/GitHub concept to learn."
 */
public class UserStory1QuestListE2ETest {

    private TestConsole testConsole;
    private CLIApplication app;

    @BeforeEach
    void setUp() {
        testConsole = new TestConsole();
        app = new CLIApplication(testConsole);
    }

    @Test
    void testQuestListDisplayHappyPath() {
        // Arrange: User selects quest list option then exits
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Welcome"), "Should show welcome message");
        assertTrue(output.contains("Quest List"), "Should show quest list menu option");
        assertTrue(output.contains("Quest"), "Should display available quests");
        assertTrue(output.contains("GitHub"), "Should mention GitHub content");
    }

    @Test
    void testQuestListWithQuestSelection() {
        // Arrange: User selects quest list, then selects a quest, then exits
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Quest List"), "Should show quest list");
        assertTrue(output.contains("selected"), "Should indicate quest selection");
    }

    @Test
    void testQuestListInvalidSelection() {
        // Arrange: User makes invalid selection in quest list
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("999"); // Invalid quest number
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Quest List"), "Should show quest list");
        assertTrue(
                output.contains("Invalid") || output.contains("invalid"),
                "Should show error for invalid selection");
    }

    @Test
    void testReturnToMainMenuFromQuestList() {
        // Arrange: User goes to quest list and returns to main menu
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("0"); // Return to main menu (if supported)
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Quest List"), "Should show quest list");
        assertTrue(
                output.contains("Main Menu") || output.contains("Welcome"),
                "Should return to main menu");
    }
}
