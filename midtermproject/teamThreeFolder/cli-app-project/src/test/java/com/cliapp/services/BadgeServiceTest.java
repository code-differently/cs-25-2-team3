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
        assertTrue(badges.size() >= 3, "Should have at least 3 default badges");

        boolean hasGitBasics = badges.stream().anyMatch(b -> b.getId().equals("git-basics"));
        boolean hasGitBranching = badges.stream().anyMatch(b -> b.getId().equals("git-branching"));
        boolean hasGitRemote = badges.stream().anyMatch(b -> b.getId().equals("git-remote"));
        assertTrue(hasGitBasics, "Should have git-basics badge");
        assertTrue(hasGitBranching, "Should have git-branching badge");
        assertTrue(hasGitRemote, "Should have git-remote badge");
    }

    @Test
    void testGetBadgeById() {
        Badge found = badgeService.getBadgeById("git-basics");
        assertNotNull(found, "Should find badge by ID");
        assertEquals("git-basics", found.getId(), "Should return correct badge");
        assertEquals("Git Fundamentals", found.getName(), "Should have correct name");

        Badge notFound = badgeService.getBadgeById("nonexistent");
        assertNull(notFound, "Should return null for nonexistent badge");
    }

    @Test
    void testAddPointsToBadge() {
        Badge badge = badgeService.getBadgeById("git-basics");
        assertEquals(0.0, badge.getPointsEarned(), "Initial points should be zero");
        badgeService.addPointsToBadge("git-basics", 10.0);
        assertEquals(10.0, badge.getPointsEarned(), "Should add points correctly");
        badgeService.addPointsToBadge("git-basics", 15.0); // Exceeds maxPoints
        assertEquals(20.0, badge.getPointsEarned(), "Should cap points at maxPoints");
    }

    @Test
    void testAddCustomBadge() {
        int initialCount = badgeService.getAllBadges().size();
        badgeService.addBadge(
                "custom-badge", "Custom Badge", "Custom description", 5, 25, "custom-quest");
        List<Badge> badges = badgeService.getAllBadges();
        assertEquals(initialCount + 1, badges.size(), "Should have one more badge");
        Badge custom = badgeService.getBadgeById("custom-badge");
        assertNotNull(custom, "Should find custom badge");
        assertEquals("Custom Badge", custom.getName(), "Should have correct name");
        assertEquals(5, custom.getPointsEarned(), "Should have correct initial points");
    }

    @Test
    void testAddPointsToBadgeInvalidCases() {
        List<Badge> badges = badgeService.getAllBadges();
        Badge badge = badges.get(0);
        double originalPoints = badge.getPointsEarned();
        badgeService.addPointsToBadge(null, 10.0); // Should do nothing
        badgeService.addPointsToBadge("nonexistent", 10.0); // Should do nothing
        badgeService.addPointsToBadge(badge.getId(), -5.0); // Should do nothing
        assertEquals(
                originalPoints,
                badge.getPointsEarned(),
                "Points should remain unchanged for invalid cases");
    }

    @Test
    void testPointsCapping() {
        Badge badge = badgeService.getAllBadges().get(0);
        badgeService.addPointsToBadge(badge.getId(), 100.0); // Exceeds maxPoints
        assertTrue(badge.getPointsEarned() <= badge.getMaxPoints());
    }

    @Test
    void testGetBadgeByIdNullAndNonexistent() {
        assertNull(badgeService.getBadgeById(null));
        assertNull(badgeService.getBadgeById("does-not-exist"));
    }
}
