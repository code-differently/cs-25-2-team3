package org.example;

import org.example.Quest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for adding learning modules to a Quest.
 */
public class QuestModulesAddTest {

    @Test
    void testAddLearningModule_ShouldAddModuleToQuest() {
        // Arrange: Create a Quest with initial modules
        List<String> modules = Arrays.asList("Variables", "Loops");
        Quest quest = new Quest("Learn Java", 3, false, modules);

        // Act: Add a new module
        quest.addLearningModule("Methods");

        // Assert: Check if the new module is added to the quest
        List<String> expectedModules = Arrays.asList("Variables", "Loops", "Methods");
        assertEquals(expectedModules, quest.getLearningModules(), "The new module should be added to the quest");
    }
}
