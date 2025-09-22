package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cliapp.domain.GlossaryEntry;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GlossaryServiceTest {

    private GlossaryService glossaryService;

    @BeforeEach
    void setUp() {
        glossaryService = new GlossaryService();
    }

    @Test
    void testGlossaryServiceCreation() {
        assertNotNull(glossaryService, "Glossary service should be created");
        List<GlossaryEntry> entries = glossaryService.getAllEntries();
        assertFalse(entries.isEmpty(), "Should have glossary entries loaded");
    }

    @Test
    void testGetAllEntries() {
        List<GlossaryEntry> entries = glossaryService.getAllEntries();
        assertTrue(entries.size() >= 10, "Should have at least 10 glossary entries");

        boolean hasGitInit =
                entries.stream().anyMatch(e -> e.getCommand().toLowerCase().contains("init"));
        assertTrue(hasGitInit, "Should have git init command");

        boolean hasGitAdd =
                entries.stream().anyMatch(e -> e.getCommand().toLowerCase().contains("add"));
        assertTrue(hasGitAdd, "Should have git add command");

        boolean hasGitCommit =
                entries.stream().anyMatch(e -> e.getCommand().toLowerCase().contains("commit"));
        assertTrue(hasGitCommit, "Should have git commit command");
    }

    @Test
    void testSearchEntries() {
        List<GlossaryEntry> commitEntries = glossaryService.searchEntries("commit");
        assertFalse(commitEntries.isEmpty(), "Should find commit-related entries");

        boolean allContainCommit =
                commitEntries.stream()
                        .allMatch(
                                entry ->
                                        entry.getCommand().toLowerCase().contains("commit")
                                                || entry.getDefinition()
                                                        .toLowerCase()
                                                        .contains("commit"));
        assertTrue(allContainCommit, "All results should contain 'commit'");

        List<GlossaryEntry> nonExistent = glossaryService.searchEntries("xyznoacommand");
        assertTrue(nonExistent.isEmpty(), "Should return empty list for non-existent commands");
    }

    @Test
    void testSearchEntriesIgnoreCase() {
        List<GlossaryEntry> upperCase = glossaryService.searchEntries("COMMIT");
        List<GlossaryEntry> lowerCase = glossaryService.searchEntries("commit");
        List<GlossaryEntry> mixedCase = glossaryService.searchEntries("CoMmIt");

        assertEquals(upperCase.size(), lowerCase.size(), "Case should not matter for search");
        assertEquals(lowerCase.size(), mixedCase.size(), "Case should not matter for search");
    }

    @Test
    void testGetEntriesByCategory() {
        List<GlossaryEntry> repoEntries = glossaryService.getEntriesByCategory("Repository Setup");
        assertFalse(repoEntries.isEmpty(), "Should have Repository Setup entries");

        boolean allCorrectCategory =
                repoEntries.stream()
                        .allMatch(entry -> "Repository Setup".equals(entry.getCategory()));
        assertTrue(allCorrectCategory, "All entries should be in Repository Setup category");

        List<GlossaryEntry> nonExistentCategory =
                glossaryService.getEntriesByCategory("Nonexistent Category");
        assertTrue(
                nonExistentCategory.isEmpty(), "Should return empty list for nonexistent category");
    }

    @Test
    void testGetAllCategories() {
        Set<String> categories = glossaryService.getAllCategories();
        assertFalse(categories.isEmpty(), "Should have categories");
        assertTrue(
                categories.contains("Repository Setup"), "Should have Repository Setup category");

        // Check for no duplicates (Set inherently has no duplicates)
        assertTrue(categories.size() > 0, "Should have at least one category");
    }

    @Test
    void testAddEntry() {
        int initialSize = glossaryService.getAllEntries().size();

        glossaryService.addEntry("git test", "Test command", "git test example", "Testing");

        List<GlossaryEntry> entries = glossaryService.getAllEntries();
        assertEquals(initialSize + 1, entries.size(), "Should have one more entry");

        List<GlossaryEntry> testEntries = glossaryService.searchEntries("git test");
        assertFalse(testEntries.isEmpty(), "Should find the new test entry");
        assertEquals(
                "Test command",
                testEntries.get(0).getDefinition(),
                "Should have correct definition");
    }

    @Test
    void testSearchByPartialCommand() {
        List<GlossaryEntry> gitEntries = glossaryService.searchEntries("git");
        assertFalse(gitEntries.isEmpty(), "Should find entries containing 'git'");

        boolean allContainGit =
                gitEntries.stream()
                        .anyMatch(entry -> entry.getCommand().toLowerCase().contains("git"));
        assertTrue(allContainGit, "At least one result should contain 'git'");
    }

    @Test
    void testEntryStructure() {
        List<GlossaryEntry> entries = glossaryService.getAllEntries();

        for (GlossaryEntry entry : entries) {
            assertNotNull(entry.getCommand(), "Command should not be null");
            assertNotNull(entry.getDefinition(), "Definition should not be null");
            assertNotNull(entry.getCategory(), "Category should not be null");

            assertFalse(entry.getCommand().trim().isEmpty(), "Command should not be empty");
            assertFalse(entry.getDefinition().trim().isEmpty(), "Definition should not be empty");
            assertFalse(entry.getCategory().trim().isEmpty(), "Category should not be empty");
        }
    }

    @Test
    void testFormatForDisplay() {
        List<GlossaryEntry> entries = glossaryService.getAllEntries();
        assertFalse(entries.isEmpty(), "Should have entries to test");

        GlossaryEntry entry = entries.get(0);
        String formatted = entry.formatForDisplay();

        assertNotNull(formatted, "Formatted display should not be null");
        assertTrue(formatted.contains(entry.getCommand()), "Should contain command");
        assertTrue(formatted.contains(entry.getDefinition()), "Should contain definition");
    }

    @Test
    void testGetEntryByCommand() {
        // Add a specific entry we can test
        glossaryService.addEntry(
                "git status", "Show working tree status", "git status", "Status Check");

        // Test exact command match
        GlossaryEntry found = glossaryService.getEntryByCommand("git status");
        assertNotNull(found, "Should find entry by exact command");
        assertEquals("git status", found.getCommand(), "Should return correct entry");
        assertEquals(
                "Show working tree status",
                found.getDefinition(),
                "Should have correct definition");

        // Test case insensitive search
        GlossaryEntry foundCase = glossaryService.getEntryByCommand("GIT STATUS");
        assertNotNull(foundCase, "Should find entry with different case");
        assertEquals(
                "git status",
                foundCase.getCommand(),
                "Should return same entry regardless of case");

        // Test non-existent command
        GlossaryEntry notFound = glossaryService.getEntryByCommand("nonexistent command");
        assertNull(notFound, "Should return null for non-existent command");

        // Test empty/null command
        GlossaryEntry nullResult = glossaryService.getEntryByCommand("");
        assertNull(nullResult, "Should return null for empty command");
    }

    @Test
    void testGetEntryCount() {
        int initialCount = glossaryService.getEntryCount();
        assertTrue(initialCount > 0, "Should have initial entries");

        // Add an entry and verify count increases
        glossaryService.addEntry("git log", "Show commit history", "git log --oneline", "History");
        int afterAdd = glossaryService.getEntryCount();
        assertEquals(initialCount + 1, afterAdd, "Count should increase by 1");

        // Add another entry
        glossaryService.addEntry("git diff", "Show changes", "git diff HEAD", "Comparison");
        int afterSecondAdd = glossaryService.getEntryCount();
        assertEquals(initialCount + 2, afterSecondAdd, "Count should increase by 2");

        // Verify count matches actual entries
        List<GlossaryEntry> allEntries = glossaryService.getAllEntries();
        assertEquals(
                allEntries.size(),
                glossaryService.getEntryCount(),
                "Count should match actual entries size");
    }

    @Test
    void testSearchEntriesWithNullAndEmpty() {
        assertNotNull(glossaryService.searchEntries(null));
        assertNotNull(glossaryService.searchEntries(""));
    }

    @Test
    void testGetEntriesByCategoryNonexistent() {
        List<GlossaryEntry> entries = glossaryService.getEntriesByCategory("no-such-category");
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }

    @Test
    void testGetEntryByCommandNullAndNonexistent() {
        assertNull(glossaryService.getEntryByCommand(null));
        assertNull(glossaryService.getEntryByCommand("does-not-exist"));
    }
}
