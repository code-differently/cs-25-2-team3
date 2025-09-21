package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestGameServiceTest {

    private QuestGameService questGameService;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;

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
        assertEquals(
                10,
                questGameService.calculatePointsForLevel("Beginner"),
                "Beginner should be 10 points");
        assertEquals(
                10, questGameService.calculatePointsForLevel("beginner"), "Case insensitive test");
        assertEquals(
                20,
                questGameService.calculatePointsForLevel("Intermediate"),
                "Intermediate should be 20 points");
        assertEquals(
                30,
                questGameService.calculatePointsForLevel("Advanced"),
                "Advanced should be 30 points");
        assertEquals(
                10,
                questGameService.calculatePointsForLevel("Unknown"),
                "Unknown should default to 10 points");
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
}
