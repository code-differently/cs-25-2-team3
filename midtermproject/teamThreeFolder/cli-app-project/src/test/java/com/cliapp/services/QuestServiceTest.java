package com.cliapp.services;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class QuestServiceTest {
    private QuestService service;

    @BeforeEach
    void setUp() {
        service = new QuestService();
    }

    @Test
    void testGetAllQuestsAndStartComplete() {
        List<Quest> quests = service.getAllQuests();
        assertNotNull(quests);
        assertTrue(quests.size() >= 0);
        Quest quest = quests.get(0);
        assertFalse(service.isQuestActive());
        service.startQuest(quest.getId());
        assertTrue(service.isQuestActive());
        service.completeCurrentQuest();
        assertFalse(service.isQuestActive());
    }

    @Test
    void testQuestProgress() {
        service.setQuestProgress(50);
        assertEquals(50, service.getQuestProgress());
        service.resetProgress();
        assertEquals(0, service.getQuestProgress());
    }

    @Test
    void testGetQuestByIdNullAndNonexistent() {
        assertNull(service.getQuestById(null));
        assertNull(service.getQuestById("does-not-exist"));
    }

    @Test
    void testCompletedAndIncompleteQuests() {
        List<Quest> quests = service.getAllQuests();
        if (!quests.isEmpty()) {
            Quest quest = quests.get(0);
            service.startQuest(quest.getId());
            service.completeCurrentQuest();
            assertTrue(service.getCompletedQuests().contains(quest));
            assertFalse(service.getIncompleteQuests().contains(quest));
        }
    }

    @Test
    void testSetQuestProgressNegative() {
        service.setQuestProgress(-10);
        assertEquals(-10, service.getQuestProgress());
    }

    @Test
    void testMarkQuestAsCompleted() {
        QuestCollection collection = new QuestCollection();
        Quest quest = new Quest("q1", "Test Quest", "desc", java.util.Arrays.asList("module1"), 1);
        collection.add(quest);
        QuestService service = new QuestService(collection);
        assertFalse(quest.isCompleted());
        assertTrue(service.markQuestAsCompleted("q1"));
        assertTrue(quest.isCompleted());
        assertFalse(service.markQuestAsCompleted("invalid"));
    }

    @Test
    void testRecieveBadgeScore() {
        QuestCollection collection = new QuestCollection();
        Quest quest = new Quest("q2", "Badge Quest", "desc", java.util.Arrays.asList("module1"), 1);
        collection.add(quest);
        QuestService service = new QuestService(collection);
        // Set currentQuest and mark as completed
        try {
            java.lang.reflect.Field currentQuestField = QuestService.class.getDeclaredField("currentQuest");
            currentQuestField.setAccessible(true);
            currentQuestField.set(service, quest);
            quest.setCompleted(true);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
        }
        assertTrue(service.recieveBadgeScore(100));
        // Not completed
        Quest quest2 = new Quest("q3", "Not Completed", "desc", java.util.Arrays.asList("module1"), 1);
        try {
            java.lang.reflect.Field currentQuestField = QuestService.class.getDeclaredField("currentQuest");
            currentQuestField.setAccessible(true);
            currentQuestField.set(service, quest2);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
        }
        assertFalse(service.recieveBadgeScore(50));
    }

    @Test
    void testGetQuestCollection() {
        QuestCollection collection = new QuestCollection();
        QuestService service = new QuestService(collection);
        assertSame(collection, service.getQuestCollection());
    }

    @Test
    void testGetQuestsByDifficulty() {
        QuestCollection collection = new QuestCollection();
        Quest quest1 = new Quest("q1", "Easy Quest", "desc", java.util.Arrays.asList("module1"), 1);
        Quest quest2 = new Quest("q2", "Hard Quest", "desc", java.util.Arrays.asList("module1"), 2);
        collection.add(quest1);
        collection.add(quest2);
        QuestService service = new QuestService(collection);
        List<Quest> easyQuests = service.getQuestsByDifficulty(1);
        List<Quest> hardQuests = service.getQuestsByDifficulty(2);
        assertEquals(1, easyQuests.size());
        assertEquals("Easy Quest", easyQuests.get(0).getName());
        assertEquals(1, hardQuests.size());
        assertEquals("Hard Quest", hardQuests.get(0).getName());
    }
}
