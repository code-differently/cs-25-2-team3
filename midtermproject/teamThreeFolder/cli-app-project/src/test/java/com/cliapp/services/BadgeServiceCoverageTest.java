package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Badge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Additional coverage for BadgeService branches")
class BadgeServiceCoverageTest {

    @Test
    void testCoverageScenarios() {
        BadgeService service = new BadgeService();

        // 1. Invalid badge id for addPointsToBadge
        service.addPointsToBadge("invalid-id", 10.0); // Should do nothing
        Badge invalidBadge = service.getBadgeById("invalid-id");
        assertNull(invalidBadge, "Invalid badge should not exist");

        // 2. Add points to valid badge and cap at maxPoints
        service.addPointsToBadge("git-basics", 10.0);
        Badge gitBasics = service.getBadgeById("git-basics");
        assertEquals(10.0, gitBasics.getPointsEarned(), "Should add points correctly");
        service.addPointsToBadge("git-basics", 15.0); // Exceeds maxPoints
        assertEquals(20.0, gitBasics.getPointsEarned(), "Should cap points at maxPoints");

        // 3. Add points with negative value (should do nothing)
        service.addPointsToBadge("git-basics", -5.0);
        assertEquals(
                20.0,
                gitBasics.getPointsEarned(),
                "Points should remain unchanged for negative value");
    }
}
