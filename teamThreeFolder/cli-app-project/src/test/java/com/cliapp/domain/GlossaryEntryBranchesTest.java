package com.cliapp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GlossaryEntryBranchesTest {
    @Test
    void testSettersAndGetters() {
        GlossaryEntry entry = new GlossaryEntry();
        entry.setCommand("git status");
        entry.setDefinition("Shows status");
        entry.setExample("git status");
        entry.setCategory("info");
        assertEquals("git status", entry.getCommand());
        assertEquals("Shows status", entry.getDefinition());
        assertEquals("git status", entry.getExample());
        assertEquals("info", entry.getCategory());
    }

    @Test
    void testToStringEqualsAndHashCode() {
        GlossaryEntry entry1 = new GlossaryEntry("git commit", "Commit", "git commit -m", "basic");
        GlossaryEntry entry2 = new GlossaryEntry("git commit", "Other", "Other", "other");
        GlossaryEntry entry3 = new GlossaryEntry("git push", "Push", "git push", "remote");
        assertEquals(entry1, entry2);
        assertNotEquals(entry1, entry3);
        assertEquals(entry1.hashCode(), entry2.hashCode());
        assertNotEquals(entry1.hashCode(), entry3.hashCode());
    }
}
