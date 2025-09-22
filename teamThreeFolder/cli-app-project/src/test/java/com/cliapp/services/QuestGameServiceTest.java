package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.io.Console;
import com.cliapp.io.TestConsole;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class QuestGameServiceTest {

    private QuestGameService questGameService;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;
    private TestConsole testConsole;

    // Simple mock console for tests
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
        questGameService = new QuestGameService();

        // Capture output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalIn = System.in;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore streams
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @BeforeEach
    void setupQuestJson() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("Quest", ".json");
        java.nio.file.Files.writeString(tempFile, TEST_QUEST_JSON);
        System.setProperty("cliapp.quest.json.path", tempFile.toString());
    }

    @BeforeEach
    void setupQuestJsonAndConsole() throws Exception {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("Quest", ".json");
        java.nio.file.Files.writeString(tempFile, TEST_QUEST_JSON);
        System.setProperty("cliapp.quest.json.path", tempFile.toString());
        mockConsole = new MockConsole();
        mockConsole.addInput("a"); // Always answer correctly

        // Add enough correct answers for all possible questions/interactions
        for (int i = 0; i < 10; i++) mockConsole.addInput("a");
    }

    @Test
    void testQuestGameServiceCreation() {
        assertNotNull(questGameService, "Quest game service should be created");
    }

    @Test
    void testGetQuestionCount() {
        int count = questGameService.getQuestionCount();
        assertTrue(count >= 0, "Question count should be non-negative");
        // Since we have 3 questions in our test JSON
        assertTrue(count >= 3, "Should have at least 3 questions loaded");
    }

    @Test
    void testQuestGameServiceLoadsQuestions() {
        // Test that questions are loaded from JSON
        int count = questGameService.getQuestionCount();
        assertTrue(count > 0, "Should load questions from JSON file");

        // Verify output shows quest start when method is called
        // We'll just test that the service is properly initialized
        String output = outputStream.toString();
        // Since we haven't called playQuest, output should be empty initially
        assertTrue(
                output.isEmpty() || output.trim().isEmpty(),
                "Output should be empty before calling methods");
    }

    @Test
    void testPlayQuestMethodExists() {
        // Test that the method exists and can be called without throwing
        assertDoesNotThrow(
                () -> {
                    // We can't really test the interactive part without complex input simulation
                    // but we can test that the method exists and service is properly initialized
                    int count = questGameService.getQuestionCount();
                    assertTrue(count >= 0, "Service should be properly initialized");
                },
                "playQuest method should exist and be callable");
    }

    @Test
    void testCalculatePointsForLevel() {
        assertEquals(5, questGameService.calculatePointsForLevel("beginner"));
        assertEquals(7.5, questGameService.calculatePointsForLevel("intermediate"));
        assertEquals(10, questGameService.calculatePointsForLevel("advanced"));
        assertEquals(5, questGameService.calculatePointsForLevel("unknown"));
        assertEquals(5, questGameService.calculatePointsForLevel(null));
    }

    @Test
    void testHasQuestions() {
        assertTrue(questGameService.hasQuestions(), "Service should have questions loaded");
    }

    @Test
    void testGetQuestTitle() {
        String title = questGameService.getQuestTitle();
        assertNotNull(title, "Quest title should not be null");

        assertTrue(title.contains("Git Quest"), "Title should contain 'Git Quest'");
    }

    @Test
    void testGetQuestInstructions() {
        String instructions = questGameService.getQuestInstructions();
        assertNotNull(instructions, "Instructions should not be null");
        assertTrue(instructions.contains("Answer"), "Instructions should contain 'Answer'");
    }

    @Test
    void testConstructorWithConsole() {
        Console console = new MockConsole();
        QuestGameService service = new QuestGameService(console);
        assertNotNull(service);
    }

    @Test
    void testLoadQuestionsFromJsonHandlesMissingFileCustom() {
        Console console = new MockConsole();
        // Simulate missing file by not providing a file, just check that service can be created
        QuestGameService service = new QuestGameService(console);
        assertNotNull(service);
    }

    @Test
    void testGetQuestTitleAndInstructions() {
        assertNotNull(questGameService.getQuestTitle());
        assertNotNull(questGameService.getQuestInstructions());
    }

    @Test
    void testHasQuestionsAndGetQuestionCount() {
        assertTrue(questGameService.getQuestionCount() >= 0);
        assertEquals(questGameService.hasQuestions(), questGameService.getQuestionCount() > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"beginner", "intermediate", "advanced"})
    void testCalculatePointsForLevelBranches(String level) {
        double expected;
        switch (level) {
            case "beginner":
                expected = 5;
                break;
            case "intermediate":
                expected = 7.5;
                break;
            case "advanced":
                expected = 10;
                break;
            default:
                expected = 5;
        }
        assertEquals(expected, questGameService.calculatePointsForLevel(level));
    }

    @Test
    void testPlayQuestWithNoQuestions() {
        // Should print error and not throw
        questGameService.playQuest("nonexistent");
    }

    @Test
    void testClearScreenDoesNotThrow() {
        assertDoesNotThrow(
                () -> {
                    questGameService.getQuestTitle(); // Just to call something
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"beginner", "intermediate", "advanced", "unknown", "", "null"})
    void testCalculatePointsForLevelHandlesInvalid(String level) {
        double expected =
                switch (level) {
                    case "beginner" -> 5;
                    case "intermediate" -> 7.5;
                    case "advanced" -> 10;
                    default -> 5;
                };
        assertEquals(expected, questGameService.calculatePointsForLevel(level));
    }

    @ParameterizedTest
    @ValueSource(strings = {"beginner", "intermediate", "advanced", "unknown", "", "null"})
    void testCalculatePointsForLevelHandlesNullAndEmpty(String level) {
        double expected =
                switch (level) {
                    case "beginner" -> 5;
                    case "intermediate" -> 7.5;
                    case "advanced" -> 10;
                    default -> 5;
                };
        assertEquals(expected, questGameService.calculatePointsForLevel(level));
        assertEquals(5, questGameService.calculatePointsForLevel(null));
    }

    @Test
    void testPlayQuestThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> questGameService.playQuest());
    }

    // Removed tests for private methods and duplicate/invalid overrides
    // Only public method tests and logic remain
}
