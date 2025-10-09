package com.cliapp.services;

import com.cliapp.domain.Question;
import com.cliapp.io.Console;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Test specifically targeting the askQuestion() method to push coverage over 90% */
class QuestGameServiceAskQuestionTest {

    private InputStream originalSystemIn;
    private PrintStream originalSystemOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        originalSystemIn = System.in;
        originalSystemOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    void testAskQuestionCaseInsensitiveAnswer() throws Exception {
        Question.Option optionA = new Question.Option("A", "git init");
        Question.Feedback feedback = new Question.Feedback("Correct!", null);

        Question question =
                new Question(
                        "beginner",
                        "Case test",
                        Arrays.asList(optionA),
                        "a", // correct answer is lowercase
                        feedback);

        // Mock user input - uppercase answer should work (gets converted to lowercase)
        String input = "A\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuestGameService service = new QuestGameService();

        // Use reflection to access private askQuestion method
        Method askQuestionMethod =
                QuestGameService.class.getDeclaredMethod("askQuestion", Question.class);
        askQuestionMethod.setAccessible(true);

        // Call askQuestion and verify result
        boolean result = (boolean) askQuestionMethod.invoke(service, question);

        assertTrue(result, "Should handle case-insensitive input correctly");
    }

    @Test
    void testAskQuestionWrongThenRightWithHintAndRetry() throws Exception {
        Console console = mock(Console.class);
        // Simulate wrong answer then correct answer
        when(console.readLine()).thenReturn("b", "a");
        // Use ArgumentCaptor to capture printed output
        java.util.List<String> printedLines = new java.util.ArrayList<>();
        doAnswer(invocation -> { printedLines.add(invocation.getArgument(0)); return null; }).when(console).println(anyString());
        doAnswer(invocation -> { printedLines.add(invocation.getArgument(0)); return null; }).when(console).print(anyString());

        Question.Option optionA = new Question.Option("a", "git init");
        Question.Option optionB = new Question.Option("b", "git status");
        Question.IncorrectFeedback incorrect = new Question.IncorrectFeedback(
                "git init", "This initializes a repo.", "analogy", "example", true);
        Question.Feedback feedback = new Question.Feedback("Correct!", incorrect);
        Question question = new Question("beginner", "Initialize a repo", java.util.Arrays.asList(optionA, optionB), "a", feedback);

        QuestGameService service = new QuestGameService(console);
        Method method = QuestGameService.class.getDeclaredMethod("askQuestion", Question.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(service, question);
        String output = String.join("\n", printedLines);
        assertTrue(output.contains("❌ Incorrect!"));
        assertTrue(output.contains("💡 Correct command: git init"));
        assertTrue(output.contains("This initializes a repo."));
        assertTrue(output.contains("🔄 Try again!"));
        assertTrue(result);
    }
}
