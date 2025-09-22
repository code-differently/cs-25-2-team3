package com.cliapp.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GlossaryEntryTest {

    private GlossaryEntry entry;

    @BeforeEach
    void setUp() {
        entry =
                new GlossaryEntry(
                        "git init",
                        "Initialize a new Git repository",
                        "git init my-project",
                        "Repository Setup");
    }

    @Test
    void testGlossaryEntryCreation_ShouldWorkRight() {
        assertNotNull(entry, "Entry shouldn't be null, that ain't it");
        assertEquals("git init", entry.getCommand(), "Command should match what we set");
        assertEquals(
                "Initialize a new Git repository",
                entry.getDefinition(),
                "Definition gotta be right");
        assertEquals("git init my-project", entry.getExample(), "Example should be what we put in");
        assertEquals("Repository Setup", entry.getCategory(), "Category better match");
    }

    @Test
    void testFormatForDisplay_ShouldShowProper() {
        String formatted = entry.formatForDisplay();

        assertNotNull(formatted, "Formatted string shouldn't be null");
        assertTrue(formatted.contains("git init"), "Should show the command, that's essential");
        assertTrue(
                formatted.contains("Initialize a new Git repository"),
                "Definition gotta be in there");
        assertTrue(formatted.contains("git init my-project"), "Example should be displayed too");
    }

    @Test
    void testEmptyExample_ShouldHandleGracefully() {
        GlossaryEntry noExample = new GlossaryEntry("git status", "Show status", "", "Info");
        String formatted = noExample.formatForDisplay();

        assertNotNull(formatted, "Should handle empty example without crashing");
        assertTrue(formatted.contains("git status"), "Command should still show");
        assertTrue(formatted.contains("Show status"), "Definition should still be there");
    }

    @Test
    void testEqualsAndHashCode() {
        GlossaryEntry entry1 = new GlossaryEntry("cmd", "def", "ex", "cat");
        GlossaryEntry entry2 = new GlossaryEntry("cmd", "def2", "ex2", "cat2");
        assertEquals(entry1, entry2);
        assertEquals(entry1.hashCode(), entry2.hashCode());
        GlossaryEntry entry3 = new GlossaryEntry("other", "def", "ex", "cat");
        assertNotEquals(entry1, entry3);
    }

    @Test
    void testToStringNotNull() {
        GlossaryEntry entry = new GlossaryEntry("cmd", "def", "ex", "cat");
        assertNotNull(entry.toString());
        assertTrue(entry.toString().contains("cmd"));
    }

    @Test
    void testNullAndEmptyFields() {
        GlossaryEntry entry = new GlossaryEntry();
        assertNull(entry.getCommand());
        assertNull(entry.getDefinition());
        assertNull(entry.getExample());
        assertNull(entry.getCategory());
    }
}
