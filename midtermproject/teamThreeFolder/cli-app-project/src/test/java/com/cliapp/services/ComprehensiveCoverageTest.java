package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Badge;
import com.cliapp.io.Console;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Comprehensive coverage tests to reach 90%")
class ComprehensiveCoverageTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalErr;

    private static final String TEST_QUEST_JSON =
            """
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

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        errorStream = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    static class MockConsole implements Console {
        private final Queue<String> inputs = new LinkedList<>();
        private final StringBuilder output = new StringBuilder();

        public void addInput(String input) {
            inputs.add(input);
        }

        @Override
        public void println(String s) {
            output.append(s).append("\n");
        }

        @Override
        public void print(String s) {
            output.append(s);
        }

        @Override
        public String readLine() {
            return inputs.isEmpty() ? "a" : inputs.poll();
        }

        public String getOutput() {
            return output.toString();
        }

        @Override
        public void close() {}
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
    void testPlayQuestThrowsNoSuchElementException() {
        assertThrows(
                NoSuchElementException.class,
                () -> {
                    QuestGameService service = new QuestGameService();
                    service.playQuest();
                });
    }

    @Test
    @DisplayName("GlossaryService comprehensive coverage")
    void testGlossaryServiceComprehensive() {
        GlossaryService service = new GlossaryService();

        // Test all service methods for comprehensive coverage
        assertNotNull(service.getGlossaryCollection(), "Collection should not be null");
        assertTrue(service.getEntryCount() > 0, "Should have entries");
        assertNotNull(service.getAllEntries(), "All entries should not be null");
        assertNotNull(service.getAllCategories(), "Categories should not be null");

        // Test specific entry operations
        service.addEntry(
                "git remote", "Manage remote repositories", "git remote -v", "Remote Operations");
        assertTrue(service.getEntryCount() > 3, "Should have added entry");

        // Test search functionality comprehensively
        assertNotNull(service.searchEntries("git"), "Search should return results");
        assertNotNull(service.searchEntries("nonexistent"), "Search should handle no results");
        assertNotNull(service.searchEntries(""), "Search should handle empty query");

        // Test category operations
        assertNotNull(service.getEntriesByCategory("Remote Operations"), "Should find category");
        assertTrue(
                service.getEntriesByCategory("NonExistent").isEmpty(),
                "Should handle non-existent category");

        // Test entry retrieval
        assertNotNull(service.getEntryByCommand("git remote"), "Should find added command");
        assertNull(
                service.getEntryByCommand("nonexistent-command"),
                "Should handle non-existent command");
    }

    @Test
    @DisplayName("BadgeService comprehensive branch coverage")
    void testBadgeServiceBranches() {
        BadgeService service = new BadgeService();

        // Test all badge operations to hit branches
        List<Badge> badges = service.getAllBadges();
        assertTrue(badges.size() > 0, "Should have default badges");
        assertNotNull(service.getBadgeById("git-basics"), "Should find badge by ID");
        assertNull(service.getBadgeById("nonexistent"), "Should handle non-existent badge");

        // Test addPointsToBadge and ensure points are capped at maxPoints
        Badge gitBasics = service.getBadgeById("git-basics");
        assertEquals(0.0, gitBasics.getPointsEarned(), "Initial points should be zero");
        service.addPointsToBadge("git-basics", 10.0);
        assertEquals(10.0, gitBasics.getPointsEarned(), "Should add points correctly");
        service.addPointsToBadge("git-basics", 15.0); // Exceeds maxPoints
        assertEquals(20.0, gitBasics.getPointsEarned(), "Should cap points at maxPoints");

        // Test addPointsToBadge with invalid badgeId and points
        service.addPointsToBadge(null, 10.0); // Should do nothing
        service.addPointsToBadge("nonexistent", 10.0); // Should do nothing
        service.addPointsToBadge("git-basics", -5.0); // Should do nothing
        assertEquals(20.0, gitBasics.getPointsEarned(), "Points should remain capped");
    }

    @Test
    @DisplayName("Cover remaining service error paths")
    void testServiceErrorPaths() {
        // Test QuestService additional paths
        QuestService questService = new QuestService();
        assertNotNull(questService.getAllQuests(), "Should return quests");
        assertNotNull(questService.getQuestById("git-basics"), "Should find quest by ID");
        assertNull(questService.getQuestById("nonexistent"), "Should handle non-existent quest");

        // Test quest state operations
        assertFalse(questService.isQuestActive(), "Should not be active initially");
        questService.startQuest("git-basics");
        assertTrue(questService.isQuestActive(), "Should be active after start");
        assertNotNull(questService.getCurrentQuest(), "Should have current quest");

        questService.completeCurrentQuest();
        assertFalse(questService.isQuestActive(), "Should not be active after completion");

        // Test progress operations
        questService.setQuestProgress(50);
        assertEquals(50, questService.getQuestProgress());
        questService.resetProgress();
        assertEquals(0, questService.getQuestProgress());
    }

    @Test
    @DisplayName("Complex scenario combinations")
    void testComplexScenarios() {
        BadgeService badgeService = new BadgeService();
        QuestService questService = new QuestService();

        // Test complex badge eligibility scenarios
        badgeService.addPointsToBadge("git-basics", 20.0);
        assertEquals(20.0, badgeService.getBadgeById("git-basics").getPointsEarned());
        badgeService.addPointsToBadge("git-branching", 30.0);
        assertEquals(30.0, badgeService.getBadgeById("git-branching").getPointsEarned());
        badgeService.addPointsToBadge("git-remote", 50.0);
        assertEquals(50.0, badgeService.getBadgeById("git-remote").getPointsEarned());

        // Test quest progression
        questService.startQuest("git-basics");
        questService.setQuestProgress(25);
        assertEquals(25, questService.getQuestProgress());

        questService.setQuestProgress(75);
        assertEquals(75, questService.getQuestProgress());

        questService.completeCurrentQuest();
        assertEquals(0, questService.getQuestProgress(), "Progress should reset on completion");
    }
}
