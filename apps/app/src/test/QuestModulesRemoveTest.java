package org.example;

import org.example.Quest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for removing learning modules from a Quest.
 */
public class QuestModulesRemoveTest {

    @Test
    void testRemoveLearningModule_ShouldRemoveModuleFromQuest() {
        // Arrange: Create a Quest with initial modules
        List<String> modules = Arrays.asList("Variables", "Loops", "Methods");
        Quest quest = new Quest("Learn Java", 3, false, modules);

        // Act: Remove the "Loops" module
        quest.removeLearningModule("Loops");

        // Assert: Check if the module was removed
        List<String> expectedModules = Arrays.asList("Variables", "Methods");
        assertEquals(expectedModules, quest.getLearningModules(), "The module should be removed from the quest");
    }
}
