package com.cliapp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BadgeTest {

    private Badge badge;

    @BeforeEach
    void setUp() {
        badge =
                new Badge(
                        "git-starter",
                        "Git Starter",
                        "Complete your first quest",
                        10,
                        "git-basics");
    }

    @Test
    void testBadgeCreation_ShouldWorkLikeItsSupposedTo() {
        assertNotNull(badge, "Badge shouldn't be null, that's wack");
        assertEquals("git-starter", badge.getId(), "Badge ID should match what we set");
        assertEquals("Git Starter", badge.getName(), "Badge name gotta be right");
        assertEquals(
                "Complete your first quest",
                badge.getDescription(),
                "Description should be what we put");
        assertEquals(
                10,
                badge.getPointsEarned(),
                "Points should be exactly what we set, no more no less");
        assertEquals("git-basics", badge.getQuestId(), "Quest ID better match");
    }

    @Test
    void testFormatForDisplay_ShouldShowEverything() {
        String formatted = badge.formatForDisplay();

        assertNotNull(formatted, "Formatted string shouldn't be null");
        assertTrue(formatted.contains("Git Starter"), "Should show the badge name, that's basic");
        assertTrue(formatted.contains("10 points"), "Points gotta be displayed");
        assertTrue(
                formatted.contains("Complete your first quest"),
                "Description should be in there too");
    }

    @Test
    void testDateEarned_ShouldBeNullInitially() {
        assertNull(badge.getDateEarned(), "New badge shouldn't have earned date, that makes sense");
    }

    @Test
    void testSetDateEarned_ShouldWorkRight() {
        String testDate = "2025-09-19";
        badge.setDateEarned(testDate);

        assertEquals(testDate, badge.getDateEarned(), "Date should be what we just set");
    }
}
