package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit test to verify that Quest holds and displays all assigned learning modules with correct fields.
 */
public class QuestModulesListTest {

    @Test
    public void testQuestModules_ShouldShowTitleDifficultyAndStatus() {
        // Arrange: Create some sample learning modules
        LearningModule mod1 = new LearningModule("Intro to Git", "Beginner", "Incomplete");
        LearningModule mod2 = new LearningModule("Java Basics", "Intermediate", "Complete");
        LearningModule mod3 = new LearningModule("Advanced Algorithms", "Advanced", "Incomplete");

        Quest quest = new Quest("Dev Quest", 3, false);
        quest.addLearningModule(mod1);
        quest.addLearningModule(mod2);
        quest.addLearningModule(mod3);

        // Act: Retrieve modules from the quest
        List<LearningModule> modules = quest.getLearningModules();

        // Assert: Each module has correct title, difficulty, and status
        assertEquals(3, modules.size(), "Quest should hold all modules");

        assertEquals("Intro to Git", modules.get(0).getTitle());
        assertEquals("Beginner", modules.get(0).getDifficulty());
        assertEquals("Incomplete", modules.get(0).getCompletionStatus());

        assertEquals("Java Basics", modules.get(1).getTitle());
        assertEquals("Intermediate", modules.get(1).getDifficulty());
        assertEquals("Complete", modules.get(1).getCompletionStatus());

        assertEquals("Advanced Algorithms", modules.get(2).getTitle());
        assertEquals("Advanced", modules.get(2).getDifficulty());
        assertEquals("Incomplete", modules.get(2).getCompletionStatus());
    }
}
