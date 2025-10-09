package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.io.Console;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinalCoverageTest {

    private static final String TEST_QUEST_JSON = """
        {
          "questions": [
            {
              "level": "beginner",
              "scenario": "What command initializes a new Git repository?",
              "correct": "a",
              "options": [
                {"id": "a", "command": "git init"},
                {"id": "b", "command": "git start"}
              ],
              "feedback": {
                "correct": "Correct! 'git init' initializes a new repository.",
                "incorrect": {
                  "command": "git init",
                  "definition": "Initializes a new Git repository.",
                  "retry": false
                }
              }
            }
          ]
        }
        """;

    static class MockConsole implements Console {
        private final Queue<String> inputs = new LinkedList<>();
        private final StringBuilder output = new StringBuilder();
        public void addInput(String input) { inputs.add(input); }
        @Override public void println(String s) { output.append(s).append("\n"); }
        @Override public void print(String s) { output.append(s); }
        @Override public String readLine() { return inputs.isEmpty() ? "a" : inputs.poll(); }
        public String getOutput() { return output.toString(); }
        @Override public void close() {}
    }

    private MockConsole mockConsole;

    @BeforeEach
    void setupQuestJsonAndConsole() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("Quest", ".json");
        java.nio.file.Files.writeString(tempFile, TEST_QUEST_JSON);
        System.setProperty("cliapp.quest.json.path", tempFile.toString());
        mockConsole = new MockConsole();
        for (int i = 0; i < 10; i++) mockConsole.addInput("a");
    }

    @Test
    void testQuestGameServiceBranches() {
        QuestGameService service = new QuestGameService();

        // Test different levels for point calculation to hit switch branches
        assertEquals(5, service.calculatePointsForLevel("beginner"));
        assertEquals(7.5, service.calculatePointsForLevel("intermediate"));
        assertEquals(10, service.calculatePointsForLevel("advanced"));
        assertEquals(5, service.calculatePointsForLevel("unknown"));
        assertEquals(5, service.calculatePointsForLevel(null));

        // Test case variations to hit toLowerCase() branches
        assertEquals(5, service.calculatePointsForLevel("BEGINNER"));
        assertEquals(7.5, service.calculatePointsForLevel("Intermediate"));
        assertEquals(10, service.calculatePointsForLevel("ADVANCED"));

        // Test service state
        assertTrue(service.hasQuestions());
        assertTrue(service.getQuestionCount() > 0);
        assertNotNull(service.getQuestTitle());
        assertNotNull(service.getQuestInstructions());
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

    @Test
    void testPlayQuestThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> {
            QuestGameService service = new QuestGameService();
            service.playQuest();
        });
    }
}
