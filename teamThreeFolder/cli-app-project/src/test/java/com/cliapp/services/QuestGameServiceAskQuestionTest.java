package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Question;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;
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
