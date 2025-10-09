package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cliapp.domain.Badge;
import com.cliapp.domain.Quest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BadgeManagerTest {
    @Test
    void testOnQuestCompletedAddsPoints() {
        BadgeService badgeService = mock(BadgeService.class);
        QuestService questService = mock(QuestService.class);
        Badge badge1 = new Badge("b1", "Badge1", "desc", 0.0, 10.0, "q1");
        Badge badge2 = new Badge("b2", "Badge2", "desc", 0.0, 10.0, "q2");
        List<Badge> badges = Arrays.asList(badge1, badge2);
        when(badgeService.getAllBadges()).thenReturn(badges);
        when(questService.markQuestAsCompleted("q1")).thenReturn(true);
        Quest quest = mock(Quest.class);
        when(questService.getQuestById("q1")).thenReturn(quest);
        when(quest.getDifficultyLevel()).thenReturn(1);
        BadgeManager manager = new BadgeManager(badgeService, questService);
        manager.onQuestCompleted("q1");
        verify(badgeService).addPointsToBadge("b1", 5.0);
        verify(badgeService, never()).addPointsToBadge(eq("b2"), anyDouble());
    }

    @Test
    void testOnQuestCompletedNoQuestCompleted() {
        BadgeService badgeService = mock(BadgeService.class);
        QuestService questService = mock(QuestService.class);
        when(questService.markQuestAsCompleted("q1")).thenReturn(false);
        BadgeManager manager = new BadgeManager(badgeService, questService);
        manager.onQuestCompleted("q1");
        verify(badgeService, never()).addPointsToBadge(anyString(), anyDouble());
    }
}
