package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit test to verify that answering a module scenario correctly adds a badge and returns to the main menu.
 */
public class QuestModuleAnswerTest {

    @Test
    public void testCorrectAnswer_ShouldAddPointsAndReturnToMenu() {
        // Save the original System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // Arrange: user starts with 0 badges
            User user = new User("Jayden");
            assertEquals(0, user.getBadges(), "User should start with 0 badges");

            LearningModule mod = new LearningModule(
                "Git Basics",
                "Beginner",
                "Incomplete"
            );
            mod.setScenario("You made changes in git, which command saves them locally?");
            mod.setCorrectAnswer("git commit");

            Quest quest = new Quest("Dev Quest", 2, false);
            quest.addLearningModule(mod);

            // Act: simulate selecting module and answering correctly
            quest.selectLearningModule(1);
            quest.submitAnswer(user, "git commit");

            // Grab CLI output
            String output = outContent.toString();

            // Assert: user got a badge for being correct
            assertEquals(1, user.getBadges(), "Correct answer should add 1 badge");

            // Assert: user sent back to main menu
            assertTrue(output.contains("Main Menu"), "App should return to main menu after correct answer");

            // Assert: updated points show
            assertTrue(output.contains("Badges: 1"), "Menu should show updated badge count");
        } finally {
            // Restore System.out to its original state
            System.setOut(originalOut);
        }
    }
}
