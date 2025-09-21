package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Question;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testAskQuestionCorrectAnswer() throws Exception {
        // Create a question for testing
        Question.Option optionA = new Question.Option("a", "git init");
        Question.Option optionB = new Question.Option("b", "git clone");
        Question.Feedback feedback =
                new Question.Feedback("Correct! Git init initializes a repository.", null);

        Question question =
                new Question(
                        "beginner",
                        "Which command initializes a new Git repository?",
                        Arrays.asList(optionA, optionB),
                        "a",
                        feedback);

        // Mock user input - correct answer
        String input = "a\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Create service with mocked input
        QuestGameService service = new QuestGameService();

        // Use reflection to access private askQuestion method
        Method askQuestionMethod =
                QuestGameService.class.getDeclaredMethod("askQuestion", Question.class);
        askQuestionMethod.setAccessible(true);

        // Call askQuestion and verify result
        boolean result = (boolean) askQuestionMethod.invoke(service, question);

        assertTrue(result, "Should return true for correct answer");

        String output = outputStream.toString();
        assertTrue(
                output.contains("Which command initializes"), "Should display question scenario");
        assertTrue(output.contains("a) git init"), "Should display option A");
        assertTrue(output.contains("b) git clone"), "Should display option B");
        assertTrue(output.contains("✅"), "Should show correct answer feedback");
        assertTrue(output.contains("Correct! Git init"), "Should show correct feedback message");
    }

    @Test
    void testAskQuestionIncorrectAnswerNoRetry() throws Exception {
        // Create incorrect feedback without retry
        Question.IncorrectFeedback incorrectFeedback =
                new Question.IncorrectFeedback(
                        "git init",
                        "Git init initializes a new repository",
                        "Like creating a new folder",
                        "git init my-project",
                        false // no retry
                        );
        Question.Feedback feedback = new Question.Feedback("Correct!", incorrectFeedback);

        Question.Option optionA = new Question.Option("a", "git init");
        Question.Option optionB = new Question.Option("b", "git clone");

        Question question =
                new Question(
                        "beginner",
                        "Test question",
                        Arrays.asList(optionA, optionB),
                        "a",
                        feedback);

        // Mock user input - incorrect answer
        String input = "b\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuestGameService service = new QuestGameService();

        // Use reflection to access private askQuestion method
        Method askQuestionMethod =
                QuestGameService.class.getDeclaredMethod("askQuestion", Question.class);
        askQuestionMethod.setAccessible(true);

        // Call askQuestion and verify result
        boolean result = (boolean) askQuestionMethod.invoke(service, question);

        assertFalse(result, "Should return false for incorrect answer without retry");

        String output = outputStream.toString();
        assertTrue(output.contains("❌ Incorrect!"), "Should show incorrect feedback");
        assertTrue(output.contains("� Correct command: git init"), "Should show correct command");
        assertTrue(output.contains("�💡 Git init initializes"), "Should show definition");
        assertTrue(output.contains("🔍 git init my-project"), "Should show example");
    }

    @Test
    void testAskQuestionIncorrectAnswerWithRetryThenCorrect() throws Exception {
        // Create incorrect feedback with retry enabled
        Question.IncorrectFeedback incorrectFeedback =
                new Question.IncorrectFeedback(
                        null, // no command
                        "Try again - think about initialization",
                        null, // no analogy
                        null, // no example
                        true // retry enabled
                        );
        Question.Feedback feedback = new Question.Feedback("Well done!", incorrectFeedback);

        Question.Option optionA = new Question.Option("a", "git init");
        Question.Option optionB = new Question.Option("b", "git clone");

        Question question =
                new Question(
                        "beginner",
                        "Test retry question",
                        Arrays.asList(optionA, optionB),
                        "a",
                        feedback);

        // Mock user input - incorrect answer first, then correct
        String input = "b\na\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuestGameService service = new QuestGameService();

        // Use reflection to access private askQuestion method
        Method askQuestionMethod =
                QuestGameService.class.getDeclaredMethod("askQuestion", Question.class);
        askQuestionMethod.setAccessible(true);

        // Call askQuestion and verify result
        boolean result = (boolean) askQuestionMethod.invoke(service, question);

        assertTrue(result, "Should return true after retry with correct answer");

        String output = outputStream.toString();
        assertTrue(output.contains("❌ Incorrect!"), "Should show incorrect feedback");
        assertTrue(output.contains("💡 Try again"), "Should show retry definition");
        assertTrue(output.contains("🔄 Try again!"), "Should show retry prompt");
        assertTrue(output.contains("✅"), "Should show final correct feedback");
        assertTrue(output.contains("Well done!"), "Should show correct feedback message");
    }

    @Test
    void testAskQuestionIncorrectFeedbackWithNullFields() throws Exception {
        // Test with null incorrect feedback
        Question.Feedback feedback = new Question.Feedback("Correct!", null);

        Question.Option optionA = new Question.Option("a", "git init");
        Question question =
                new Question(
                        "beginner", "Test null feedback", Arrays.asList(optionA), "a", feedback);

        // Mock user input - incorrect answer
        String input = "b\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuestGameService service = new QuestGameService();

        // Use reflection to access private askQuestion method
        Method askQuestionMethod =
                QuestGameService.class.getDeclaredMethod("askQuestion", Question.class);
        askQuestionMethod.setAccessible(true);

        // Call askQuestion and verify result
        boolean result = (boolean) askQuestionMethod.invoke(service, question);

        assertFalse(result, "Should return false for incorrect answer with null feedback");

        String output = outputStream.toString();
        assertTrue(output.contains("❌ Incorrect!"), "Should show incorrect feedback");
        // Should not crash with null incorrect feedback
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
}
