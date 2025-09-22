package com.cliapp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LearningModuleBranchesTest {
    @Test
    void testSettersAndGetters() {
        LearningModule module = new LearningModule();
        module.setId("id1");
        module.setName("Module Name");
        module.setDescription("Desc");
        module.setDifficulty(2);
        module.setCompleted(true);
        module.setProgress(0.5);
        assertEquals("id1", module.getId());
        assertEquals("Module Name", module.getName());
        assertEquals("Desc", module.getDescription());
        assertEquals(2, module.getDifficulty());
        assertTrue(module.isCompleted());
        assertEquals(0.5, module.getProgress());
    }

    @Test
    void testConstructor() {
        LearningModule module = new LearningModule("id2", "Name", "Desc", 3);
        assertEquals("id2", module.getId());
        assertEquals("Name", module.getName());
        assertEquals("Desc", module.getDescription());
        assertEquals(3, module.getDifficulty());
        assertFalse(module.isCompleted());
        assertEquals(0.0, module.getProgress());
    }
}
