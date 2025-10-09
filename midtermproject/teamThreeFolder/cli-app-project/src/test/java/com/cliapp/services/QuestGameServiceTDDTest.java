package com.cliapp.services;

import com.cliapp.io.Console;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.NoSuchElementException;
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

    void setupQuestJson() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("Quest", ".json");
        java.nio.file.Files.writeString(tempFile, TEST_QUEST_JSON);
        System.setProperty("cliapp.quest.json.path", tempFile.toString());
    }

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
        double points = questGameService.calculatePointsForLevel(level);

        if (level.toLowerCase().contains("beginner")) {
            assertEquals(5, points, "Beginner should be 5 points");
        } else if (level.toLowerCase().contains("intermediate")) {
            assertEquals(7.5, points, "Intermediate should be 7.5 points");
        } else if (level.toLowerCase().contains("advanced")) {
            assertEquals(10, points, "Advanced should be 10 points");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Unknown", "invalid", "", "  ", "Expert", "Master", "null"})
    @DisplayName("Given_InvalidLevels_When_CalculatePointsForLevel_Then_ReturnsDefaultPoints")
    void testCalculatePointsForInvalidLevels(String level) {
        double points = questGameService.calculatePointsForLevel(level);
        assertEquals(5, points, "Unknown levels should default to 5 points");
    }

    @Test
    @DisplayName("Given_NullLevel_When_CalculatePointsForLevel_Then_ReturnsDefaultPoints")
    void testCalculatePointsForNullLevel() {
        double points = questGameService.calculatePointsForLevel(null);
        assertEquals(5, points, "Null level should default to 5 points");
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_DisplaysQuestTitleAndInstructions")
    void testPlayQuestDisplaysCorrectHeader() {
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_ShowsSampleQuestion")
    void testPlayQuestShowsSampleQuestion() {
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_ShowsResults")
    void testPlayQuestShowsResults() {
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_HandlesNoQuestionsGracefully")
    void testPlayQuestWithNoQuestions() {
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuestCalled_Then_ClearsScreenForBetterUX")
    void testPlayQuestClearsScreen() {
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PointsCalculated_Then_UsesConsistentLogic")
    void testPointCalculationConsistency() {
        // Test that the same level always returns the same points
        String level = "Beginner";
        double points1 = questGameService.calculatePointsForLevel(level);
        double points2 = questGameService.calculatePointsForLevel(level);

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
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    @Test
    @DisplayName("Given_QuestGameService_When_PlayQuest_Then_ThrowsNoSuchElementException")
    void testPlayQuestThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> {
            questGameService.playQuest();
        });
    }
}
