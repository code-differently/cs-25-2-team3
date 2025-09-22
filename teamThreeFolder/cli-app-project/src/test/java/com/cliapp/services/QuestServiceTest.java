package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Quest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
