/*
package com.cliapp.e2e;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.CLIApplication;
import com.cliapp.io.TestConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-End tests for User Story 2: Glossary Access "As a learner, I want to access a glossary of
 * Git/GitHub terms so that I can understand unfamiliar concepts."
 
public class UserStory2GlossaryE2ETest {

    private TestConsole testConsole;
    private CLIApplication app;

    @BeforeEach
    void setUp() {
        testConsole = new TestConsole();
        app = new CLIApplication(testConsole);
    }

    @Test
    void testGlossaryAccessHappyPath() {
        // Arrange: User selects glossary option then exits
        testConsole.addInput("2"); // Select glossary
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Welcome"), "Should show welcome message");
        assertTrue(output.contains("Glossary"), "Should show glossary menu option");
        assertTrue(
                output.contains("Git") || output.contains("GitHub"),
                "Should contain Git/GitHub terms");
    }

    @Test
    void testGlossaryTermSearch() {
        // Arrange: User searches for a specific term
        testConsole.addInput("2"); // Select glossary
        testConsole.addInput("repository"); // Search for repository term
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Glossary"), "Should show glossary");
        assertTrue(
                output.contains("repository") || output.contains("Repository"),
                "Should find repository term");
    }

    @Test
    void testGlossaryTermNotFound() {
        // Arrange: User searches for non-existent term
        testConsole.addInput("2"); // Select glossary
        testConsole.addInput("nonexistentterm123"); // Search for invalid term
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Glossary"), "Should show glossary");
        assertTrue(
                output.contains("not found")
                        || output.contains("Not found")
                        || output.contains("invalid")
                        || output.contains("Invalid"),
                "Should indicate term not found");
    }

    @Test
    void testGlossaryListAllTerms() {
        // Arrange: User requests to see all glossary terms
        testConsole.addInput("2"); // Select glossary
        testConsole.addInput("list"); // List all terms (if supported)
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Glossary"), "Should show glossary");
        // Should contain multiple Git/GitHub terms
        long gitTermCount =
                output.toLowerCase().split("git|repository|commit|branch|merge").length - 1;
        assertTrue(gitTermCount >= 2, "Should contain multiple Git terms");
    }

    @Test
    void testGlossaryEmptySearch() {
        // Arrange: User enters empty search
        testConsole.addInput("2"); // Select glossary
        testConsole.addInput(""); // Empty input
        testConsole.addInput("4"); // Exit application

        // Act
        app.start();

        // Assert
        String output = testConsole.getFullOutput();
        assertTrue(output.contains("Glossary"), "Should show glossary");
        assertTrue(
                output.contains("empty")
                        || output.contains("Empty")
                        || output.contains("invalid")
                        || output.contains("Invalid"),
                "Should handle empty input appropriately");
    }
}
*/