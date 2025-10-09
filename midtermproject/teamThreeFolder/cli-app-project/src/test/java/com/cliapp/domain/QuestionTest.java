package com.cliapp.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionTest {
    @Test
    void testQuestionCreationAndFields() {
        List<Question.Option> options =
                Arrays.asList(new Question.Option("A", "cmdA"), new Question.Option("B", "cmdB"));
        Question.Feedback feedback = new Question.Feedback("Correct!", null);
        Question question = new Question("beginner", "Scenario", options, "A", feedback);
        assertEquals("beginner", question.getLevel());
        assertEquals("Scenario", question.getScenario());
        assertEquals(options, question.getOptions());
        assertEquals("A", question.getCorrect());
        assertEquals(feedback, question.getFeedback());
    }

    @Test
    void testOptionAndFeedback() {
        Question.Option option = new Question.Option("id", "cmd");
        assertEquals("id", option.getId());
        assertEquals("cmd", option.getCommand());
        Question.IncorrectFeedback incorrect =
                new Question.IncorrectFeedback("cmd", "def", "ana", "ex", true);
        assertEquals("cmd", incorrect.getCommand());
        assertEquals("def", incorrect.getDefinition());
        assertEquals("ana", incorrect.getAnalogy());
        assertEquals("ex", incorrect.getExample());
        assertTrue(incorrect.isRetry());
    }
}
