/*
package com.cliapp.e2e;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.CLIApplication;
import com.cliapp.io.TestConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-End tests for Badge System Testing achievement and display of badges as learners complete
 * quests.

public class BadgeSystemE2ETest {

    private TestConsole testConsole;
    private CLIApplication app;

    @BeforeEach
    void setUp() {
        testConsole = new TestConsole();
        app = new CLIApplication(testConsole);
    }

    @Test
    void testBadgeMenuAccess() {
        // Arrange: User accesses badge menu
        testConsole.addInput("4"); // Select badge menu (assuming it's option 4)
        testConsole.addInput("5"); // Exit application (updated exit option)

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(
                output.contains("Badge") || output.contains("badge"),
                "Should show badge menu option");
    }

    @Test
    void testInitialBadgeStateEmpty() {
        // Arrange: New user checks badges before completing any quests
        testConsole.addInput("4"); // Select badge menu
        testConsole.addInput("5"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(
                output.contains("Badge") || output.contains("badge"), "Should show badge section");
        assertTrue(
                output.contains("No badges")
                        || output.contains("no badges")
                        || output.contains("empty")
                        || output.contains("0"),
                "Should indicate no badges earned initially");
    }

    @Test
    void testBadgeEarnedAfterQuestCompletion() {
        // Arrange: User completes a quest and checks badges
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("1"); // Select first quest
        // Simulate quest completion through multiple interactions
        testConsole.addInput("1"); // Complete quest steps
        testConsole.addInput("4"); // Check badges
        testConsole.addInput("5"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Quest"), "Should show quest interaction");
        assertTrue(
                output.contains("Badge") || output.contains("badge"), "Should show badge section");
    }

    @Test
    void testMultipleBadgeProgression() {
        // Arrange: User completes multiple quests to earn multiple badges
        testConsole.addInput("1"); // Select quest list
        testConsole.addInput("1"); // Select first quest
        testConsole.addInput("1"); // Complete first quest
        testConsole.addInput("1"); // Go back to quest list
        testConsole.addInput("2"); // Select second quest
        testConsole.addInput("1"); // Complete second quest
        testConsole.addInput("4"); // Check badges
        testConsole.addInput("5"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Quest"), "Should show quest interactions");
        assertTrue(
                output.contains("Badge") || output.contains("badge"), "Should show badge section");
        // Check for progression indicators
        assertTrue(
                output.contains("progress")
                        || output.contains("Progress")
                        || output.contains("earned")
                        || output.contains("Earned"),
                "Should show badge progression");
    }

    @Test
    void testBadgeDetailDisplay() {
        // Arrange: User views detailed badge information
        testConsole.addInput("4"); // Select badge menu
        testConsole.addInput("1"); // View first badge details (if available)
        testConsole.addInput("5"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(
                output.contains("Badge") || output.contains("badge"), "Should show badge section");
        assertTrue(
                output.contains("detail")
                        || output.contains("Detail")
                        || output.contains("description")
                        || output.contains("Description"),
                "Should show badge details");
    }
}
*/
