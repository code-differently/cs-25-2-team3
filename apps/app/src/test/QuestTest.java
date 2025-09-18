package org.example;

import org.example.Quest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Quest class.
 * Tests the conversion of difficulty level to asterisks.
 */
public class QuestTest {

    @Test
    void testDifficultyAsAsterisks_ShouldShowProperStars() {
        // Arrange: Create quests with varying difficulty levels
        Quest easyQuest = new Quest("Easy Quest", 1, false, Collections.emptyList());
        Quest medQuest = new Quest("Medium Quest", 3, false, Collections.emptyList());
        Quest hardQuest = new Quest("Hard Quest", 5, false, Collections.emptyList());

        // Assert: Check that the difficulty is represented by the correct number of asterisks
        assertEquals("*", easyQuest.getDifficultyAsStars(), "Difficulty 1 should be shown as *");
        assertEquals("***", medQuest.getDifficultyAsStars(), "Difficulty 3 should be shown as ***");
        assertEquals("*****", hardQuest.getDifficultyAsStars(), "Difficulty 5 should be shown as *****");
    }
}
