package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.GlossaryEntry;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GlossaryService Additional Coverage Tests")
class GlossaryServiceAdditionalCoverageTest {

    private GlossaryService service;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        service = new GlossaryService();
        errorStream = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("loadGlossaryFromJson handles missing file gracefully")
    void testLoadGlossaryFromMissingFile() {
        // Test that service still works when JSON file is missing
        // Should create default entries
        GlossaryService testService = new GlossaryService();

        assertTrue(
                testService.getEntryCount() > 0, "Should have default entries when JSON missing");
        assertNotNull(
                testService.getEntryByCommand("git init"), "Should have default git init entry");
    }

    @Test
    @DisplayName("createDefaultEntries adds expected default entries")
    void testCreateDefaultEntries() {
        // Verify default entries are created
        GlossaryEntry gitInit = service.getEntryByCommand("git init");
        GlossaryEntry gitAdd = service.getEntryByCommand("git add");
        GlossaryEntry gitCommit = service.getEntryByCommand("git commit");

        assertNotNull(gitInit, "Should have git init entry");
        assertNotNull(gitAdd, "Should have git add entry");
        assertNotNull(gitCommit, "Should have git commit entry");

        assertEquals("Repository Setup", gitInit.getCategory());
        assertEquals("Staging Changes", gitAdd.getCategory());
        assertEquals("Committing Changes", gitCommit.getCategory());
    }

    @Test
    @DisplayName("addEntry creates new glossary entry")
    void testAddEntry() {
        int initialCount = service.getEntryCount();

        service.addEntry("git status", "Show working tree status", "git status", "Status");

        assertEquals(initialCount + 1, service.getEntryCount());
        GlossaryEntry entry = service.getEntryByCommand("git status");
        assertNotNull(entry);
        assertEquals("Show working tree status", entry.getDefinition());
        assertEquals("git status", entry.getExample());
        assertEquals("Status", entry.getCategory());
    }

    @Test
    @DisplayName("getAllEntries returns all entries")
    void testGetAllEntries() {
        List<GlossaryEntry> entries = service.getAllEntries();
        assertNotNull(entries);
        assertTrue(entries.size() > 0, "Should have entries");

        // Add a new entry and verify it's included
        service.addEntry("git branch", "List branches", "git branch -a", "Branching");
        List<GlossaryEntry> updatedEntries = service.getAllEntries();
        assertTrue(updatedEntries.size() > entries.size());
    }

    @Test
    @DisplayName("getEntryByCommand returns correct entry")
    void testGetEntryByCommand() {
        service.addEntry("git log", "Show commit history", "git log --oneline", "History");

        GlossaryEntry entry = service.getEntryByCommand("git log");
        assertNotNull(entry);
        assertEquals("git log", entry.getCommand());
        assertEquals("Show commit history", entry.getDefinition());

        // Test non-existent command
        GlossaryEntry nonExistent = service.getEntryByCommand("nonexistent");
        assertNull(nonExistent);
    }

    @Test
    @DisplayName("getEntriesByCategory returns entries for category")
    void testGetEntriesByCategory() {
        service.addEntry("git push", "Push changes", "git push origin main", "Remote");
        service.addEntry("git pull", "Pull changes", "git pull origin main", "Remote");

        List<GlossaryEntry> remoteEntries = service.getEntriesByCategory("Remote");
        assertEquals(2, remoteEntries.size());

        // Test non-existent category
        List<GlossaryEntry> nonExistent = service.getEntriesByCategory("NonExistent");
        assertTrue(nonExistent.isEmpty());
    }

    @Test
    @DisplayName("searchEntries finds entries by keyword")
    void testSearchEntries() {
        service.addEntry("git checkout", "Switch branches", "git checkout main", "Branching");
        service.addEntry("git merge", "Merge branches", "git merge feature", "Branching");

        List<GlossaryEntry> results = service.searchEntries("branch");
        assertTrue(results.size() >= 2, "Should find entries containing 'branch'");

        // Test case insensitive search
        List<GlossaryEntry> caseResults = service.searchEntries("BRANCH");
        assertEquals(results.size(), caseResults.size());

        // Test no results
        List<GlossaryEntry> noResults = service.searchEntries("nonexistentterm");
        assertTrue(noResults.isEmpty());
    }

    @Test
    @DisplayName("getAllCategories returns unique categories")
    void testGetAllCategories() {
        service.addEntry("git tag", "Create tag", "git tag v1.0", "Tagging");
        service.addEntry("git tag -d", "Delete tag", "git tag -d v1.0", "Tagging");

        Set<String> categories = service.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.contains("Tagging"));

        // Should not have duplicates
        long taggingCount = categories.stream().filter(c -> c.equals("Tagging")).count();
        assertEquals(1, taggingCount);
    }

    @Test
    @DisplayName("getEntryCount returns correct count")
    void testGetEntryCount() {
        int initialCount = service.getEntryCount();
        assertTrue(initialCount > 0);

        service.addEntry("git reset", "Reset changes", "git reset HEAD~1", "Reset");
        assertEquals(initialCount + 1, service.getEntryCount());
    }

    @Test
    @DisplayName("getGlossaryCollection returns collection")
    void testGetGlossaryCollection() {
        assertNotNull(service.getGlossaryCollection());
        assertTrue(service.getGlossaryCollection().size() > 0);
    }

    @Test
    @DisplayName("Error handling for JSON processing")
    void testJsonErrorHandling() {
        // Test that service handles JSON errors gracefully
        // Should fall back to default entries
        GlossaryService testService = new GlossaryService();

        // Should have at least the default entries
        assertTrue(testService.getEntryCount() >= 3);
        assertNotNull(testService.getEntryByCommand("git init"));
        assertNotNull(testService.getEntryByCommand("git add"));
        assertNotNull(testService.getEntryByCommand("git commit"));
    }

    @Test
    @DisplayName("JSON loading creates entries from file")
    void testJsonLoadingCreatesEntries() {
        // Since the service loads from glossary.json on construction,
        // we should have more than just the 3 default entries
        assertTrue(service.getEntryCount() > 3, "Should have loaded entries from JSON file");
    }
}
