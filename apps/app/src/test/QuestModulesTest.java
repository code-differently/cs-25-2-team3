package org.example;

import org.example.Quest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for verifying that Quest holds all assigned learning modules.
 */
public class QuestModulesTest {

    @Test
    void testLearningModules_ShouldHoldAllModules() {
        // Arrange: Create a list of modules and a Quest
        List<String> modules = Arrays.asList("Variables", "Loops", "Methods", "OOP");
        Quest quest = new Quest("Learn Java", 3, false, modules);

        // Assert: Check if all modules are stored properly
        assertEquals(modules, quest.getLearningModules(), "Modules should be stored in the quest");

        // Assert: Double-check the size of the modules list
        assertEquals(4, quest.getLearningModules().size(), "Modules count should be 4");
    }
}
