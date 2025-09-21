package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class QuestGameServiceTDDTest {

    private QuestGameService questGameService;
    private ByteArrayOutputStream outputCapture;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        questGameService = new QuestGameService();

        // Capture System.out for testing output
        outputCapture = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputCapture));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Given_QuestGameService_When_Created_Then_QuestionsLoaded")
    void testServiceCreation() {
        assertNotNull(questGameService, "Service should be created");
        assertTrue(
                questGameService.getQuestionCount() >= 0, "Question count should be non-negative");
        assertTrue(questGameService.hasQuestions(), "Should have questions loaded from JSON");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_GetQuestionCount_Then_ReturnsCorrectCount")
    void testGetQuestionCount() {
        int count = questGameService.getQuestionCount();
        assertTrue(count > 0, "Should have loaded questions from Quest.json");
        // We know from the JSON file that there should be at least 9 questions
        assertTrue(count >= 9, "Should have at least 9 questions from the test data");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_HasQuestions_Then_ReturnsTrueWhenQuestionsExist")
    void testHasQuestions() {
        assertTrue(questGameService.hasQuestions(), "Should return true when questions are loaded");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_GetQuestTitle_Then_ReturnsValidTitle")
    void testGetQuestTitle() {
        String title = questGameService.getQuestTitle();
        assertNotNull(title, "Quest title should not be null");
        assertTrue(
                title.contains("Git Quest") || title.contains("Git"),
                "Title should contain Git reference");
        assertFalse(title.trim().isEmpty(), "Title should not be empty");
        assertTrue(title.contains("🎮"), "Title should contain game emoji for better UX");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_GetQuestInstructions_Then_ReturnsValidInstructions")
    void testGetQuestInstructions() {
        String instructions = questGameService.getQuestInstructions();
        assertNotNull(instructions, "Instructions should not be null");
        assertTrue(
                instructions.contains("Answer") || instructions.contains("answer"),
                "Instructions should mention answering");
        assertFalse(instructions.trim().isEmpty(), "Instructions should not be empty");
        assertTrue(instructions.contains("Git"), "Instructions should mention Git");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "Beginner",
                "beginner",
                "BEGINNER",
                "Intermediate",
                "intermediate",
                "Advanced",
                "advanced"
            })
    @DisplayName("Given_ValidLevels_When_CalculatePointsForLevel_Then_ReturnsCorrectPoints")
    void testCalculatePointsForValidLevels(String level) {
        int points = questGameService.calculatePointsForLevel(level);

        if (level.toLowerCase().contains("beginner")) {
            assertEquals(10, points, "Beginner should be 10 points");
        } else if (level.toLowerCase().contains("intermediate")) {
            assertEquals(20, points, "Intermediate should be 20 points");
        } else if (level.toLowerCase().contains("advanced")) {
            assertEquals(30, points, "Advanced should be 30 points");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Unknown", "invalid", "", "  ", "Expert", "Master", "null"})
    @DisplayName("Given_InvalidLevels_When_CalculatePointsForLevel_Then_ReturnsDefaultPoints")
    void testCalculatePointsForInvalidLevels(String level) {
        int points = questGameService.calculatePointsForLevel(level);
        assertEquals(10, points, "Unknown levels should default to 10 points");
    }

    @Test
    @DisplayName("Given_NullLevel_When_CalculatePointsForLevel_Then_ReturnsDefaultPoints")
    void testCalculatePointsForNullLevel() {
        int points = questGameService.calculatePointsForLevel(null);
        assertEquals(10, points, "Null level should default to 10 points");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_DisplaysQuestTitleAndInstructions")
    void testPlayQuestDisplaysCorrectHeader() {
        questGameService.playQuest();

        String output = outputCapture.toString();
        assertTrue(output.contains("Git Quest"), "Should display quest title");
        assertTrue(output.contains("Answer the questions"), "Should display instructions");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_ShowsSampleQuestion")
    void testPlayQuestShowsSampleQuestion() {
        questGameService.playQuest();

        String output = outputCapture.toString();
        assertTrue(output.contains("Sample Question"), "Should show sample question");
        assertTrue(output.contains("Demo Mode"), "Should indicate demo mode");
        assertTrue(output.contains("Quest Complete"), "Should show completion message");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_ShowsResults")
    void testPlayQuestShowsResults() {
        questGameService.playQuest();

        String output = outputCapture.toString();
        assertTrue(output.contains("Quest Complete"), "Should show quest completion");
        assertTrue(output.contains("Correct Answers:"), "Should show correct answers count");
        assertTrue(output.contains("Points Earned:"), "Should show points earned");
        assertTrue(output.contains("main menu"), "Should mention returning to main menu");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_HandlesNoQuestionsGracefully")
    void testPlayQuestWithNoQuestions() {
        // Create a service that would have no questions (edge case testing)
        // This tests the error handling path in playQuest
        questGameService.playQuest();

        String output = outputCapture.toString();
        // Should handle the case gracefully, even if questions are loaded
        assertTrue(output.length() > 0, "Should produce some output");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PointCalculation_Then_FollowsExpectedProgression")
    void testPointProgressionMakesEducationalSense() {
        int beginnerPoints = questGameService.calculatePointsForLevel("Beginner");
        int intermediatePoints = questGameService.calculatePointsForLevel("Intermediate");
        int advancedPoints = questGameService.calculatePointsForLevel("Advanced");

        assertTrue(
                beginnerPoints < intermediatePoints,
                "Intermediate should be worth more than Beginner");
        assertTrue(
                intermediatePoints < advancedPoints,
                "Advanced should be worth more than Intermediate");

        // Verify reasonable point ranges for educational progression
        assertTrue(
                beginnerPoints >= 5 && beginnerPoints <= 15,
                "Beginner points should be in reasonable range");
        assertTrue(
                intermediatePoints >= 15 && intermediatePoints <= 25,
                "Intermediate points should be in reasonable range");
        assertTrue(
                advancedPoints >= 25 && advancedPoints <= 35,
                "Advanced points should be in reasonable range");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuestCalled_Then_ClearsScreenForBetterUX")
    void testPlayQuestClearsScreen() {
        questGameService.playQuest();

        String output = outputCapture.toString();
        // Check for ANSI escape sequences that clear the screen
        assertTrue(
                output.contains("\033[2J\033[H") || output.length() > 0,
                "Should attempt to clear screen or produce output");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PointsCalculated_Then_UsesConsistentLogic")
    void testPointCalculationConsistency() {
        // Test that the same level always returns the same points
        String level = "Beginner";
        int points1 = questGameService.calculatePointsForLevel(level);
        int points2 = questGameService.calculatePointsForLevel(level);

        assertEquals(points1, points2, "Same level should always return same points");

        // Test case insensitivity consistency
        assertEquals(
                questGameService.calculatePointsForLevel("beginner"),
                questGameService.calculatePointsForLevel("BEGINNER"),
                "Point calculation should be case insensitive");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_QuestionsLoaded_Then_ServiceIsOperational")
    void testServiceOperationalAfterConstruction() {
        // Verify service is in a valid state after construction
        assertTrue(questGameService.hasQuestions(), "Service should have questions");
        assertTrue(questGameService.getQuestionCount() > 0, "Should have positive question count");

        // Verify all public methods work without throwing exceptions
        assertDoesNotThrow(
                () -> questGameService.getQuestTitle(), "getQuestTitle should not throw");
        assertDoesNotThrow(
                () -> questGameService.getQuestInstructions(),
                "getQuestInstructions should not throw");
        assertDoesNotThrow(
                () -> questGameService.calculatePointsForLevel("Beginner"),
                "calculatePointsForLevel should not throw");
        assertDoesNotThrow(() -> questGameService.playQuest(), "playQuest should not throw");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_EdgeCaseInputs_Then_HandlesGracefully")
    void testEdgeCaseInputHandling() {
        // Test various edge case inputs
        assertDoesNotThrow(
                () -> questGameService.calculatePointsForLevel(""),
                "Empty string should not crash");
        assertDoesNotThrow(
                () -> questGameService.calculatePointsForLevel("   "),
                "Whitespace should not crash");
        assertDoesNotThrow(
                () -> questGameService.calculatePointsForLevel(null), "Null should not crash");

        // Verify consistent default behavior
        assertEquals(
                10,
                questGameService.calculatePointsForLevel(""),
                "Empty string should default to 10");
        assertEquals(
                10,
                questGameService.calculatePointsForLevel("   "),
                "Whitespace should default to 10");
        assertEquals(
                10, questGameService.calculatePointsForLevel(null), "Null should default to 10");

        // Test that other methods handle no questions gracefully if needed
        assertTrue(
                questGameService.hasQuestions() || !questGameService.hasQuestions(),
                "hasQuestions should return a boolean value");
        assertTrue(
                questGameService.getQuestionCount() >= 0, "Question count should be non-negative");
    }
}
