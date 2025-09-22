package com.cliapp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BadgeBranchesTest {
    @Test
    void testSettersAndGetters() {
        Badge badge = new Badge();
        badge.setId("id1");
        badge.setName("Badge Name");
        badge.setDescription("Desc");
        badge.setPointsEarned(10.0);
        badge.setQuestId("quest1");
        badge.setDateEarned("2025-09-22");
        assertEquals("id1", badge.getId());
        assertEquals("Badge Name", badge.getName());
        assertEquals("Desc", badge.getDescription());
        assertEquals(10.0, badge.getPointsEarned());
        assertEquals("quest1", badge.getQuestId());
        assertEquals("2025-09-22", badge.getDateEarned());
    }

    @Test
    void testConstructorAndAddPoints() {
        Badge badge = new Badge("id2", "Name", "Desc", 5.0, 10.0, "quest2");
        badge.addPoints(3.0);
        assertEquals(8.0, badge.getPointsEarned());
        badge.addPoints(5.0);
        assertEquals(10.0, badge.getPointsEarned()); // Should not exceed maxPoints
    }

    @Test
    void testFormatForDisplayAndToString() {
        Badge badge = new Badge("id3", "Name", "Desc", 7.0, 10.0, "quest3");
        badge.setDateEarned("2025-09-22");
        String display = badge.formatForDisplay();
        assertTrue(display.contains("Name"));
        assertTrue(display.contains("Desc"));
        String str = badge.toString();
        assertTrue(str.contains("id3"));
        assertTrue(str.contains("Name"));
        assertTrue(str.contains("Desc"));
        assertTrue(str.contains("quest3"));
        assertTrue(str.contains("2025-09-22"));
    }

    @Test
    void testEqualsAndHashCode() {
        Badge badge1 = new Badge("id4", "Name", "Desc", 1.0, 10.0, "quest4");
        Badge badge2 = new Badge("id4", "Other", "OtherDesc", 2.0, 10.0, "quest4");
        Badge badge3 = new Badge("id5", "Name", "Desc", 1.0, 10.0, "quest5");
        assertEquals(badge1, badge2);
        assertNotEquals(badge1, badge3);
        assertEquals(badge1.hashCode(), badge2.hashCode());
    }
}
