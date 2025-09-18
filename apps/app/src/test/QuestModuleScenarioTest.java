package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit test to verify that selecting a module displays its scenario prompt.
 */
public class QuestModuleScenarioTest {

    @Test
    public void testSelectingModule_ShouldDisplayScenarioPrompt() {
        // Save the original System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // Arrange: make a module with a sample scenario
            LearningModule mod = new LearningModule(
                "Git Basics",
                "Beginner",
                "Incomplete"
            );
            mod.setScenario("You made changes in git, which command saves them locally?");

            Quest quest = new Quest("Dev Quest", 2, false);
            quest.addLearningModule(mod);

            // Act: simulate selecting the first module
            quest.selectLearningModule(1);

            // Grab CLI text
            String output = outContent.toString();

            // Assert: scenario shows up when selected
            assertTrue(
                output.contains("You made changes in git, which command saves them locally?"),
                "Scenario prompt should display when module selected"
            );
        } finally {
            // Restore System.out to its original state
            System.setOut(originalOut);
        }
    }
}

