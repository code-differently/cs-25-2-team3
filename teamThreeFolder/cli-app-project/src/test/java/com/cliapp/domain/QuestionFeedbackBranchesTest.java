package com.cliapp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cliapp.domain.Question.Feedback;
import com.cliapp.domain.Question.IncorrectFeedback;

class QuestionFeedbackBranchesTest {
    @Test
    void testFeedbackSettersAndGetters() {
        IncorrectFeedback incorrect = new IncorrectFeedback("cmd", "def", "analogy", "ex", true);
        Feedback feedback = new Feedback("correct", incorrect);
        assertEquals("correct", feedback.getCorrect());
        assertEquals(incorrect, feedback.getIncorrect());
        feedback.setCorrect("newCorrect");
        assertEquals("newCorrect", feedback.getCorrect());
        IncorrectFeedback newIncorrect = new IncorrectFeedback();
        feedback.setIncorrect(newIncorrect);
        assertEquals(newIncorrect, feedback.getIncorrect());
    }

    @Test
    void testAdapterMethods() {
        Feedback feedback = new Feedback();
        feedback.setCorrectFeedback("adapterCorrect");
        assertEquals("adapterCorrect", feedback.getCorrectFeedback());
        feedback.setIncorrectFeedback("adapterIncorrect");
        assertEquals("adapterIncorrect", feedback.getIncorrectFeedback());
    }

    @Test
    void testIncorrectFeedbackSettersAndGetters() {
        IncorrectFeedback incorrect = new IncorrectFeedback();
        incorrect.setCommand("cmd");
        incorrect.setDefinition("def");
        incorrect.setAnalogy("analogy");
        incorrect.setExample("ex");
        incorrect.setRetry(true);
        assertEquals("cmd", incorrect.getCommand());
        assertEquals("def", incorrect.getDefinition());
        assertEquals("analogy", incorrect.getAnalogy());
        assertEquals("ex", incorrect.getExample());
        assertTrue(incorrect.isRetry());
    }
}
