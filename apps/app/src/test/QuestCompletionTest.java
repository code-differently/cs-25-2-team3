package org.example;

import org.example.Quest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Quest completion status display.
 */
public class QuestCompletionTest {

    @Test
    void testCompletionStatus_ShouldShowYOrN() {
        // Arrange: Create quests with different completion statuses
        Quest incompleteQuest = new Quest("Incomplete Quest", 2, false, Collections.emptyList());
        Quest completedQuest = new Quest("Completed Quest", 4, true, Collections.emptyList());

        // Assert: Incomplete quest should show "N"
        assertEquals("N", incompleteQuest.getCompletionStatus(), "Incomplete quest should show 'N'");

        // Assert: Completed quest should show "Y"
        assertEquals("Y", completedQuest.getCompletionStatus(), "Completed quest should show 'Y'");
    }
}
