package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cliapp.domain.Badge;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BadgeServiceTest {

    private BadgeService badgeService;

    @BeforeEach
    void setUp() {
        badgeService = new BadgeService();
    }

    @Test
    void testBadgeServiceCreation() {
        assertNotNull(badgeService, "Badge service should be created");
        List<Badge> badges = badgeService.getAllBadges();
        assertFalse(badges.isEmpty(), "Should have default badges initialized");
    }

    @Test
    void testGetAllBadges() {
        List<Badge> badges = badgeService.getAllBadges();
        assertTrue(badges.size() >= 6, "Should have at least 6 default badges");

        boolean hasGitStarter = badges.stream().anyMatch(b -> b.getId().equals("git-starter"));
        assertTrue(hasGitStarter, "Should have git-starter badge");
    }

    @Test
    void testAwardBadge() {
        boolean result = badgeService.awardBadge("git-starter");
        assertTrue(result, "Should successfully award existing badge");

        List<Badge> earned = badgeService.getEarnedBadges();
        assertEquals(1, earned.size(), "Should have 1 earned badge");
        assertEquals("git-starter", earned.get(0).getId(), "Should be the git-starter badge");
        assertNotNull(earned.get(0).getDateEarned(), "Badge should have earned date");

        boolean failResult = badgeService.awardBadge("nonexistent-badge");
        assertFalse(failResult, "Should return false for nonexistent badge");
    }

    @Test
    void testGetEarnedBadges() {
        List<Badge> initialEarned = badgeService.getEarnedBadges();
        assertTrue(initialEarned.isEmpty(), "Should start with no earned badges");

        badgeService.awardBadge("git-starter");
        badgeService.awardBadge("quest-master");

        List<Badge> earned = badgeService.getEarnedBadges();
        assertEquals(2, earned.size(), "Should have 2 earned badges");

        boolean hasGitStarter = earned.stream().anyMatch(b -> b.getId().equals("git-starter"));
        boolean hasQuestMaster = earned.stream().anyMatch(b -> b.getId().equals("quest-master"));
        assertTrue(hasGitStarter, "Should have git-starter badge");
        assertTrue(hasQuestMaster, "Should have quest-master badge");
    }

    @Test
    void testGetBadgeById() {
        Badge found = badgeService.getBadgeById("git-starter");
        assertNotNull(found, "Should find badge by ID");
        assertEquals("git-starter", found.getId(), "Should return correct badge");
        assertEquals("Git Starter", found.getName(), "Should have correct name");

        Badge notFound = badgeService.getBadgeById("nonexistent");
        assertNull(notFound, "Should return null for nonexistent badge");
    }

    @Test
    void testHasBadge() {
        assertFalse(badgeService.hasBadge("git-starter"), "Badge should not be earned initially");

        badgeService.awardBadge("git-starter");
        assertTrue(badgeService.hasBadge("git-starter"), "Badge should be earned after awarding");

        assertFalse(badgeService.hasBadge("quest-master"), "Other badges should not be earned");
    }

    @Test
    void testGetTotalPointsEarned() {
        assertEquals(0, badgeService.getTotalPointsEarned(), "Should start with 0 points");

        badgeService.awardBadge("git-starter"); // 10 points
        assertEquals(
                10, badgeService.getTotalPointsEarned(), "Should have 10 points after first badge");

        badgeService.awardBadge("quest-master"); // 50 points
        assertEquals(60, badgeService.getTotalPointsEarned(), "Should have 60 points total");
    }

    @Test
    void testAddCustomBadge() {
        int initialCount = badgeService.getAllBadges().size();

        badgeService.addBadge(
                "custom-badge", "Custom Badge", "Custom description", 25, "custom-quest");

        List<Badge> badges = badgeService.getAllBadges();
        assertEquals(initialCount + 1, badges.size(), "Should have one more badge");

        Badge custom = badgeService.getBadgeById("custom-badge");
        assertNotNull(custom, "Should find custom badge");
        assertEquals("Custom Badge", custom.getName(), "Should have correct name");
        assertEquals(25, custom.getPointsEarned(), "Should have correct points");
    }

    @Test
    void testDisplayEarnedBadges() {
        badgeService.awardBadge("git-starter");
        badgeService.awardBadge("quest-master");

        List<Badge> earned = badgeService.getEarnedBadges();
        assertNotNull(earned, "Earned badges should not be null");
        assertEquals(2, earned.size(), "Should have 2 earned badges");

        Badge gitStarter =
                earned.stream()
                        .filter(b -> b.getId().equals("git-starter"))
                        .findFirst()
                        .orElse(null);
        assertNotNull(gitStarter, "Should contain git starter badge");
        assertEquals(10, gitStarter.getPointsEarned(), "Should have correct points");
    }

    @Test
    void testDuplicateBadgeAward() {
        assertTrue(badgeService.awardBadge("git-starter"), "First award should succeed");
        assertFalse(badgeService.awardBadge("git-starter"), "Duplicate award should fail");

        List<Badge> earned = badgeService.getEarnedBadges();
        assertEquals(1, earned.size(), "Should only have one instance of the badge");
    }

    @Test
    void testGetBadgesForQuest() {
        // Test with quest that has associated badges
        List<Badge> questBadges = badgeService.getBadgesForQuest("quest-1");
        assertNotNull(questBadges, "Should return a list");

        // Award a badge and verify it's filtered out from available
        badgeService.awardBadge("git-starter");
        List<Badge> availableAfterAward = badgeService.getBadgesForQuest("quest-1");

        // Should not contain already earned badges
        boolean hasEarnedBadge =
                availableAfterAward.stream().anyMatch(b -> b.getId().equals("git-starter"));
        assertFalse(hasEarnedBadge, "Should not include already earned badges");

        // Test with nonexistent quest
        List<Badge> nonexistentQuest = badgeService.getBadgesForQuest("nonexistent-quest");
        assertTrue(nonexistentQuest.isEmpty(), "Should return empty list for nonexistent quest");
    }

    @Test
    void testCheckBadgeEligibility() {
        // Test no eligibility initially
        List<Badge> eligible = badgeService.checkBadgeEligibility(0, 0);
        assertTrue(eligible.isEmpty(), "Should have no eligible badges initially");

        // Test git-starter eligibility (1 completed quest)
        List<Badge> starterEligible = badgeService.checkBadgeEligibility(1, 0);
        assertEquals(1, starterEligible.size(), "Should be eligible for git-starter");
        assertEquals("git-starter", starterEligible.get(0).getId(), "Should be git-starter badge");

        // Award git-starter and test it's no longer eligible
        badgeService.awardBadge("git-starter");
        List<Badge> afterStarter = badgeService.checkBadgeEligibility(1, 0);
        assertTrue(afterStarter.isEmpty(), "Should not be eligible for already earned badge");

        // Test quest-master eligibility (3 completed quests)
        List<Badge> masterEligible = badgeService.checkBadgeEligibility(3, 0);
        assertEquals(1, masterEligible.size(), "Should be eligible for quest-master");
        assertEquals("quest-master", masterEligible.get(0).getId(), "Should be quest-master badge");

        // Test glossary-guru eligibility (10 lookups)
        List<Badge> guruEligible = badgeService.checkBadgeEligibility(0, 10);
        assertEquals(1, guruEligible.size(), "Should be eligible for glossary-guru");
        assertEquals("glossary-guru", guruEligible.get(0).getId(), "Should be glossary-guru badge");

        // Test multiple eligibilities
        List<Badge> multipleEligible = badgeService.checkBadgeEligibility(3, 10);
        assertEquals(2, multipleEligible.size(), "Should be eligible for multiple badges");

        // Test edge cases - exactly at thresholds
        List<Badge> exactThresholds = badgeService.checkBadgeEligibility(3, 10);
        assertTrue(exactThresholds.size() >= 2, "Should include badges at exact thresholds");
    }
}
