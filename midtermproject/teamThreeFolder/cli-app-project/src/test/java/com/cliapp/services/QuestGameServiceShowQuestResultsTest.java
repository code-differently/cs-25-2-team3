package com.cliapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class QuestGameServiceShowQuestResultsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    void invokeShowQuestResults(int correct, int total, int points) throws Exception {
        QuestGameService service = new QuestGameService();
        Method method = QuestGameService.class.getDeclaredMethod("showQuestResults", int.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(service, correct, total, points);
    }

    @Test
    void testShowQuestResultsExcellent() throws Exception {
        invokeShowQuestResults(8, 10, 100);
        String output = outContent.toString();
        assertTrue(output.contains("🏆 === Quest Complete ==="));
        assertTrue(output.contains("✅ Correct Answers: 8/10"));
        assertTrue(output.contains("⭐ Points Earned: 100"));
        assertTrue(output.contains("🎉 Excellent work! You're a Git master!"));
        assertTrue(output.contains("Returning to main menu..."));
    }

    @Test
    void testShowQuestResultsGood() throws Exception {
        invokeShowQuestResults(7, 10, 80);
        String output = outContent.toString();
        assertTrue(output.contains("👍 Good job! Keep practicing!"));
    }

    @Test
    void testShowQuestResultsKeepStudying() throws Exception {
        invokeShowQuestResults(5, 10, 50);
        String output = outContent.toString();
        assertTrue(output.contains("📚 Keep studying! Practice makes perfect!"));
    }
}
