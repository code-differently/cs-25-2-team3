package org.example;

import org.example.Quest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Quest creation and field assignment.
 */
public class QuestCreationTest {

    @Test
    void testQuestCreation_ShouldAssignFieldsCorrectly() {
        // Arrange: Set up quest data
        String questName = "Learn Java";
        int difficulty = 3;
        boolean completed = false;
        List<String> modules = Arrays.asList("Variables", "Loops", "Methods");

        // Act: Create a new Quest
        Quest quest = new Quest(questName, difficulty, completed, modules);

        // Assert: Check that all fields are set correctly
        assertEquals(questName, quest.getName(), "Quest name should match input");
        assertEquals(difficulty, quest.getDifficulty(), "Difficulty should match input");
        assertEquals(completed, quest.isCompleted(), "Completion status should match input");
        assertEquals(modules, quest.getLearningModules(), "Modules list should match input");
        assertEquals(3, quest.getLearningModules().size(), "Modules count should be 3");
    }
}
