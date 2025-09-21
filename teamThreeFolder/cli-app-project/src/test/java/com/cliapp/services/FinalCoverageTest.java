package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class FinalCoverageTest {

    @Test
    void testQuestGameServiceBranches() {
        QuestGameService service = new QuestGameService();

        // Test different levels for point calculation to hit switch branches
        assertEquals(10, service.calculatePointsForLevel("beginner"));
        assertEquals(20, service.calculatePointsForLevel("intermediate"));
        assertEquals(30, service.calculatePointsForLevel("advanced"));
        assertEquals(10, service.calculatePointsForLevel("unknown"));
        assertEquals(10, service.calculatePointsForLevel(null));

        // Test case variations to hit toLowerCase() branches
        assertEquals(10, service.calculatePointsForLevel("BEGINNER"));
        assertEquals(20, service.calculatePointsForLevel("Intermediate"));
        assertEquals(30, service.calculatePointsForLevel("ADVANCED"));

        // Test service state
        assertTrue(service.hasQuestions());
        assertTrue(service.getQuestionCount() > 0);
        assertNotNull(service.getQuestTitle());
        assertNotNull(service.getQuestInstructions());
    }

    @Test
    void testQuestGameServiceShowResultsBranches() {
        QuestGameService service = new QuestGameService();

        // Capture output to test showQuestResults branches
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Call playQuest which internally calls showQuestResults
            // This will test the showQuestResults method and its percentage-based branches
            service.playQuest();

            String output = outputStream.toString();
            assertTrue(output.length() > 0, "Should produce output");
            assertTrue(output.contains("Quest"), "Should contain quest-related text");

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testGlossaryServiceAdditionalPaths() {
        GlossaryService service = new GlossaryService();

        // Add entry to test creation path
        int initialCount = service.getEntryCount();
        service.addEntry("git status", "Show working tree status", "git status", "Status");
        assertEquals(initialCount + 1, service.getEntryCount());

        // Test retrieval paths
        assertNotNull(service.getEntryByCommand("git status"));
        assertNull(service.getEntryByCommand("nonexistent"));

        // Test search with empty/no results
        assertTrue(service.searchEntries("nonexistentterm").isEmpty());
        assertNotNull(service.searchEntries("git"));

        // Test category operations
        assertNotNull(service.getEntriesByCategory("Status"));
        assertTrue(service.getEntriesByCategory("NonExistent").isEmpty());

        // Test collections
        assertNotNull(service.getAllEntries());
        assertNotNull(service.getAllCategories());
    }

    @Test
    void testServiceErrorHandling() {
        // Test that services handle potential JSON errors gracefully
        // These constructors load JSON and may hit error paths
        assertDoesNotThrow(
                () -> {
                    QuestGameService gameService = new QuestGameService();
                    assertNotNull(gameService);
                });

        assertDoesNotThrow(
                () -> {
                    GlossaryService glossaryService = new GlossaryService();
                    assertNotNull(glossaryService);
                });
    }
}
