package com.cliapp.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {
    @Test
    void testSettersAndGetters() {
        UserSession session = new UserSession();
        session.setUserId("user1");
        session.setStartTime(LocalDateTime.of(2025, 9, 22, 10, 0));
        session.setLastActivity(LocalDateTime.of(2025, 9, 22, 10, 5));
        session.setCommandHistory(Arrays.asList("cmd1", "cmd2"));
        session.setTotalCommands(2);
        session.setActive(false);
        session.setCurrentQuestId("quest1");
        session.setTotalPoints(10.5);
        assertEquals("user1", session.getUserId());
        assertEquals(LocalDateTime.of(2025, 9, 22, 10, 0), session.getStartTime());
        assertEquals(LocalDateTime.of(2025, 9, 22, 10, 5), session.getLastActivity());
        assertEquals(Arrays.asList("cmd1", "cmd2"), session.getCommandHistory());
        assertEquals(2, session.getTotalCommands());
        assertFalse(session.isActive());
        assertEquals("quest1", session.getCurrentQuestId());
        assertEquals(10.5, session.getTotalPoints());
    }

    @Test
    void testAddCommandAndUpdateActivity() {
        UserSession session = new UserSession();
        session.addCommand("cmd1");
        assertTrue(session.getCommandHistory().contains("cmd1"));
        assertEquals(1, session.getTotalCommands());
        LocalDateTime before = session.getLastActivity();
        session.updateActivity();
        assertTrue(session.getLastActivity().isAfter(before) || session.getLastActivity().isEqual(before));
    }

    @Test
    void testEndSession() {
        UserSession session = new UserSession();
        session.endSession();
        assertFalse(session.isActive());
    }

    @Test
    void testQuestAndPointsMethods() {
        UserSession session = new UserSession();
        session.setCurrentQuestId("quest1");
        session.setTotalPoints(5.0);
        session.addPoints(2.5);
        assertEquals(7.5, session.getTotalPoints());
        session.markQuestCompleted("quest1");
        assertTrue(session.hasCompletedQuest("quest1"));
        assertEquals(1, session.getCompletedQuests().size());
        assertEquals("Total Points: 7.5 | Badges Earned: 1", session.getPointsSummary());
    }

    @Test
    void testCompletedQuestIdsAndGlossaryLookupCount() {
        UserSession session = new UserSession();
        session.markQuestCompleted("q1");
        assertEquals(Arrays.asList("q1"), session.getCompletedQuestIds());
        assertEquals(0, session.getGlossaryLookupCount());
    }
}

