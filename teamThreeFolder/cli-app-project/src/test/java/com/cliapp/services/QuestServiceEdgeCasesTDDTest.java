package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;
import com.cliapp.models.UserSession;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestService Edge Cases TDD Test Suite")
public class QuestServiceEdgeCasesTDDTest {

    @Mock private QuestCollection mockQuestCollection;

    @Mock private UserSession mockUserSession;

    private QuestService questService;

    @BeforeEach
    void setUp() {
        questService = new QuestService();
    }

    @Test
    @DisplayName("Given_NullQuestCollection_When_GetAllQuests_Then_HandlesGracefully")
    void testGetAllQuestsWithNullCollection() {
        List<Quest> result = questService.getAllQuests();
        assertNotNull(result, "Should not return null");
        assertTrue(result.size() >= 3, "Default quests should be initialized");
    }

    @Test
    @DisplayName("Given_QuestService_When_GetCurrentQuest_Then_ReturnsNullInitially")
    void testGetCurrentQuestInitially() {
        Quest result = questService.getCurrentQuest();

        assertNull(result, "Should return null when no current quest");
    }

    @Test
    @DisplayName("Given_QuestWithNullId_When_StartQuest_Then_HandlesGracefully")
    void testStartQuestWithNullId() {
        Quest questWithNullId = new Quest();
        questWithNullId.setId(null);
        questWithNullId.setTitle("Test Quest");

        boolean result = questService.startQuest(questWithNullId);

        assertFalse(result, "Should not start quest with null ID");
        assertNull(questService.getCurrentQuest(), "Current quest should remain null");
    }

    @Test
    @DisplayName("Given_NullQuest_When_StartQuest_Then_ReturnsFalse")
    void testStartQuestWithNull() {
        boolean result = questService.startQuest((Quest) null);

        assertFalse(result, "Should not start null quest");
        assertNull(questService.getCurrentQuest(), "Current quest should remain null");
    }

    @Test
    @DisplayName("Given_ValidQuest_When_StartQuest_Then_SetsCurrentQuest")
    void testStartQuestWithValidQuest() {
        Quest quest = new Quest();
        quest.setId("quest-1");
        quest.setTitle("Test Quest");

        boolean result = questService.startQuest(quest);

        assertTrue(result, "Should successfully start valid quest");
        assertEquals(quest, questService.getCurrentQuest(), "Should set as current quest");
    }

    @Test
    @DisplayName("Given_QuestAlreadyStarted_When_StartAnotherQuest_Then_ReplacesCurrentQuest")
    void testStartQuestReplacesExisting() {
        Quest firstQuest = new Quest();
        firstQuest.setId("quest-1");
        firstQuest.setTitle("First Quest");

        Quest secondQuest = new Quest();
        secondQuest.setId("quest-2");
        secondQuest.setTitle("Second Quest");

        questService.startQuest(firstQuest);
        boolean result = questService.startQuest(secondQuest);

        assertTrue(result, "Should successfully start second quest");
        assertEquals(
                secondQuest, questService.getCurrentQuest(), "Should replace with second quest");
        assertNotEquals(
                firstQuest, questService.getCurrentQuest(), "Should not be first quest anymore");
    }

    @Test
    @DisplayName("Given_NoCurrentQuest_When_CompleteCurrentQuest_Then_ReturnsFalse")
    void testCompleteCurrentQuestWithNoQuest() {
        boolean result = questService.completeCurrentQuest();

        assertFalse(result, "Should not complete when no current quest");
    }

    @Test
    @DisplayName("Given_CurrentQuest_When_CompleteCurrentQuest_Then_ClearsCurrentQuest")
    void testCompleteCurrentQuestWithValidQuest() {
        Quest quest = new Quest();
        quest.setId("quest-1");
        quest.setTitle("Test Quest");

        questService.startQuest(quest);
        boolean result = questService.completeCurrentQuest();

        assertTrue(result, "Should successfully complete current quest");
        assertNull(questService.getCurrentQuest(), "Should clear current quest after completion");
    }

    @Test
    @DisplayName("Given_QuestService_When_GetQuestProgress_Then_ReturnsZeroInitially")
    void testGetQuestProgressInitially() {
        double progress = questService.getQuestProgress();

        assertEquals(0.0, progress, 0.001, "Should return 0 progress initially");
    }

    @Test
    @DisplayName("Given_QuestService_When_SetProgress_Then_UpdatesProgress")
    void testSetQuestProgress() {
        questService.setQuestProgress(0.5);

        assertEquals(0.5, questService.getQuestProgress(), 0.001, "Should update progress");
    }

    @Test
    @DisplayName("Given_NegativeProgress_When_SetProgress_Then_AcceptsNegativeValue")
    void testSetNegativeProgress() {
        questService.setQuestProgress(-0.1);

        assertEquals(
                -0.1, questService.getQuestProgress(), 0.001, "Should accept negative progress");
    }

    @Test
    @DisplayName("Given_ProgressGreaterThanOne_When_SetProgress_Then_AcceptsValue")
    void testSetProgressGreaterThanOne() {
        questService.setQuestProgress(1.5);

        assertEquals(1.5, questService.getQuestProgress(), 0.001, "Should accept progress > 1");
    }

    @Test
    @DisplayName("Given_QuestService_When_ResetProgress_Then_SetsProgressToZero")
    void testResetProgress() {
        questService.setQuestProgress(0.8);
        questService.resetProgress();

        assertEquals(0.0, questService.getQuestProgress(), 0.001, "Should reset progress to 0");
    }

    @Test
    @DisplayName("Given_QuestService_When_IsQuestActive_Then_ReturnsFalseInitially")
    void testIsQuestActiveInitially() {
        boolean active = questService.isQuestActive();

        assertFalse(active, "Should return false when no quest active");
    }

    @Test
    @DisplayName("Given_StartedQuest_When_IsQuestActive_Then_ReturnsTrue")
    void testIsQuestActiveWithStartedQuest() {
        Quest quest = new Quest();
        quest.setId("quest-1");

        questService.startQuest(quest);
        boolean active = questService.isQuestActive();

        assertTrue(active, "Should return true when quest is active");
    }

    @Test
    @DisplayName("Given_CompletedQuest_When_IsQuestActive_Then_ReturnsFalse")
    void testIsQuestActiveAfterCompletion() {
        Quest quest = new Quest();
        quest.setId("quest-1");

        questService.startQuest(quest);
        questService.completeCurrentQuest();
        boolean active = questService.isQuestActive();

        assertFalse(active, "Should return false after quest completion");
    }

    @Test
    @DisplayName("Given_MultipleQuestOperations_When_Performed_Then_MaintainsConsistentState")
    void testMultipleOperationsConsistency() {
        Quest quest1 = new Quest();
        quest1.setId("quest-1");
        quest1.setTitle("First Quest");

        Quest quest2 = new Quest();
        quest2.setId("quest-2");
        quest2.setTitle("Second Quest");

        // Start first quest
        assertTrue(questService.startQuest(quest1));
        assertTrue(questService.isQuestActive());
        assertEquals(quest1, questService.getCurrentQuest());

        // Set progress
        questService.setQuestProgress(0.5);
        assertEquals(0.5, questService.getQuestProgress(), 0.001);

        // Start second quest (should replace first)
        assertTrue(questService.startQuest(quest2));
        assertEquals(quest2, questService.getCurrentQuest());

        // Progress should be reset when new quest starts
        questService.resetProgress();
        assertEquals(0.0, questService.getQuestProgress(), 0.001);

        // Complete quest
        assertTrue(questService.completeCurrentQuest());
        assertFalse(questService.isQuestActive());
        assertNull(questService.getCurrentQuest());
    }

    @Test
    @DisplayName("startQuest with unknown quest ID should throw IllegalArgumentException")
    void testStartQuestWithUnknownIdThrowsException() {
        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            questService.startQuest("unknown-quest-id");
                        });

        assertEquals("Unknown quest: unknown-quest-id", exception.getMessage());
    }

    @Test
    @DisplayName("startQuest with valid quest ID should work correctly")
    void testStartQuestWithValidIdWorksCorrectly() {
        // Act & Assert - using a quest ID that should be initialized by default
        assertTrue(questService.startQuest("git-basics"));
        assertTrue(questService.isQuestActive());
        assertNotNull(questService.getCurrentQuest());
        assertEquals("git-basics", questService.getCurrentQuest().getId());
    }
}
