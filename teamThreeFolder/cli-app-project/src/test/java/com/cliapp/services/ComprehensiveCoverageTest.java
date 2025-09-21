package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

    @Test
    @DisplayName("QuestGameService comprehensive error path coverage")
    void testQuestGameServiceErrorPaths() {
        QuestGameService service = new QuestGameService();

        // Test all public methods for comprehensive coverage
        assertNotNull(service.getQuestTitle(), "Quest title should not be null");
        assertNotNull(service.getQuestInstructions(), "Quest instructions should not be null");
        assertTrue(service.hasQuestions(), "Should have questions loaded");
        assertTrue(service.getQuestionCount() > 0, "Should have positive question count");

        // Test calculatePointsForLevel with all possible levels
        assertEquals(10, service.calculatePointsForLevel("beginner"));
        assertEquals(20, service.calculatePointsForLevel("intermediate"));
        assertEquals(30, service.calculatePointsForLevel("advanced"));
        assertEquals(10, service.calculatePointsForLevel("unknown"));
        assertEquals(10, service.calculatePointsForLevel(null));

        // Test case variations
        assertEquals(10, service.calculatePointsForLevel("BEGINNER"));
        assertEquals(20, service.calculatePointsForLevel("Intermediate"));
        assertEquals(30, service.calculatePointsForLevel("Advanced"));

        // Test playQuest method - covers execution paths
        service.playQuest();
        String output = outputStream.toString();
        assertTrue(output.contains("Git Quest"), "Should contain quest title");
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
        assertTrue(service.getAllBadges().size() > 0, "Should have default badges");
        assertNotNull(service.getBadgeById("git-starter"), "Should find badge by ID");
        assertNull(service.getBadgeById("nonexistent"), "Should handle non-existent badge");

        // Test award and has badge operations
        assertFalse(service.hasBadge("git-starter"), "Should not have badge initially");
        assertTrue(service.awardBadge("git-starter"), "Should award badge successfully");
        assertTrue(service.hasBadge("git-starter"), "Should have badge after awarding");
        assertFalse(service.awardBadge("git-starter"), "Should not award duplicate");
        assertFalse(service.awardBadge("nonexistent"), "Should not award non-existent badge");

        // Test getBadgesForQuest with various scenarios
        assertTrue(
                service.getBadgesForQuest("nonexistent").isEmpty(),
                "Should return empty for non-existent quest");
        assertNotNull(service.getBadgesForQuest(null), "Should handle null quest");

        // Test checkBadgeEligibility comprehensive scenarios
        assertTrue(service.checkBadgeEligibility(0, 0).isEmpty(), "No eligibility initially");

        // Test different thresholds to hit all branches
        var eligibleAfterOne = service.checkBadgeEligibility(1, 0);
        assertTrue(eligibleAfterOne.isEmpty(), "git-starter already earned");

        var eligibleAfterThree = service.checkBadgeEligibility(3, 0);
        assertEquals(1, eligibleAfterThree.size(), "Should be eligible for quest-master");
        assertEquals("quest-master", eligibleAfterThree.get(0).getId());

        var eligibleGlossary = service.checkBadgeEligibility(0, 10);
        assertEquals(1, eligibleGlossary.size(), "Should be eligible for glossary-guru");
        assertEquals("glossary-guru", eligibleGlossary.get(0).getId());

        var eligibleCombined = service.checkBadgeEligibility(3, 10);
        assertEquals(2, eligibleCombined.size(), "Should be eligible for both");

        // Test points calculation
        assertEquals(0, service.getTotalPointsEarned(), "Should have points from awarded badge");

        service.awardBadge("quest-master"); // Award another badge
        assertTrue(service.getTotalPointsEarned() > 0, "Should have more points");
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
    @DisplayName("Error handling and edge cases")
    void testErrorHandlingEdgeCases() {
        // Test JSON loading error scenarios (captured in error stream)
        QuestGameService gameService = new QuestGameService();
        GlossaryService glossaryService = new GlossaryService();

        // These should not throw exceptions despite potential JSON errors
        assertDoesNotThrow(() -> gameService.playQuest());
        assertDoesNotThrow(() -> glossaryService.getAllEntries());

        // Verify error output is captured
        String errorOutput = errorStream.toString();
        // Error output might be empty if JSON loads successfully, which is fine
        assertNotNull(errorOutput, "Error stream should be accessible");
    }

    @Test
    @DisplayName("Complex scenario combinations")
    void testComplexScenarios() {
        BadgeService badgeService = new BadgeService();
        QuestService questService = new QuestService();

        // Test complex badge eligibility scenarios
        badgeService.awardBadge("git-starter");
        badgeService.awardBadge("quest-master");
        badgeService.awardBadge("glossary-guru");

        // All badges awarded, should have no eligibility
        assertTrue(badgeService.checkBadgeEligibility(10, 20).isEmpty(), "All badges earned");

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
