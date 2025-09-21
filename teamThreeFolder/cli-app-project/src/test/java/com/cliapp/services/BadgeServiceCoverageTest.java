package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Badge;
import com.cliapp.models.UserSession;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Additional coverage for BadgeService branches")
class BadgeServiceCoverageTest {

    @Test
    void testCoverageScenarios() {
        BadgeService service = new BadgeService();

        // 1. Invalid badge id false path
        assertFalse(service.awardBadge("invalid-id"), "Invalid badge should not be awarded");

        // 2. Valid award then duplicate (second false)
        assertTrue(service.awardBadge("git-starter"), "First valid award should succeed");
        assertFalse(service.awardBadge("git-starter"), "Duplicate award should fail");

        // 3. Non-existent quest badges list (empty path in stream)
        assertTrue(
                service.getBadgesForQuest("non-existent").isEmpty(), "No badges for unknown quest");

        // 4. Eligibility checks after already earning git-starter
        List<Badge> afterOneQuest = service.checkBadgeEligibility(1, 0);
        assertTrue(afterOneQuest.isEmpty(), "git-starter already earned so no eligibility");

        // 5. Three quests completed (quest-master eligible)
        List<Badge> afterThreeQuests = service.checkBadgeEligibility(3, 0);
        assertEquals(1, afterThreeQuests.size(), "Only quest-master should be eligible now");
        assertEquals("quest-master", afterThreeQuests.get(0).getId());

        // 6. Glossary lookups threshold
        List<Badge> glossaryEligible = service.checkBadgeEligibility(0, 10);
        assertEquals(
                1,
                glossaryEligible.size(),
                "Only glossary-guru should be eligible at glossary threshold");
        assertEquals("glossary-guru", glossaryEligible.get(0).getId());

        // 7. Combined thresholds (quest-master + glossary-guru)
        List<Badge> combined = service.checkBadgeEligibility(3, 10);
        assertEquals(2, combined.size(), "quest-master and glossary-guru should be eligible");
        assertTrue(combined.stream().anyMatch(b -> b.getId().equals("quest-master")));
        assertTrue(combined.stream().anyMatch(b -> b.getId().equals("glossary-guru")));
    }

    @Test
    void testAdditionalBranchCoverage() {
        BadgeService service = new BadgeService();
        UserSession session = new UserSession();

        // Test checkForNewBadges with different session states
        session.markQuestCompleted("test-quest-1");
        session.markQuestCompleted("test-quest-2");
        session.markQuestCompleted("test-quest-3");

        List<Badge> newBadges = service.checkForNewBadges(session, "test-quest-3");
        assertNotNull(newBadges, "Should return list even if empty");

        // Test overload methods with valid session
        List<Badge> earnedBadges = service.getEarnedBadges(session);
        assertNotNull(earnedBadges, "Should return session badges");

        List<Badge> availableBadges = service.getAvailableBadges(session);
        assertNotNull(availableBadges, "Should return available badges");
        assertTrue(availableBadges.size() > 0, "Should have available badges");

        // Test edge case branches
        assertTrue(
                service.checkForNewBadges(session, "").isEmpty(),
                "Empty quest ID should return empty");
        assertTrue(
                service.checkForNewBadges(null, "quest").isEmpty(),
                "Null session should return empty");
    }

    @Test
    void testBadgeServiceSessionInteraction() {
        BadgeService service = new BadgeService();
        UserSession session = new UserSession();

        // Add badge to session and test retrieval
        Badge testBadge = new Badge("test-badge", "Test Badge", "A test badge", 25, "test-quest");
        session.addBadge(testBadge);

        List<Badge> sessionBadges = service.getEarnedBadges(session);
        assertEquals(1, sessionBadges.size(), "Should return session's earned badges");
        assertEquals("test-badge", sessionBadges.get(0).getId());

        // Test with more completed quests to trigger different eligibility branches
        for (int i = 1; i <= 5; i++) {
            session.markQuestCompleted("quest-" + i);
        }

        // Test checkForNewBadges with eligibility conditions met
        List<Badge> newlyAwarded = service.checkForNewBadges(session, "quest-5");
        assertNotNull(newlyAwarded, "Should return newly awarded badges");
    }
}
