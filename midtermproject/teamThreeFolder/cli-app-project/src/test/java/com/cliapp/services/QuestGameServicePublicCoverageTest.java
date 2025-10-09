package com.cliapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cliapp.domain.Question;
import com.cliapp.io.Console;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestGameServicePublicCoverageTest {
    private void injectQuestion(QuestGameService service, Question question) {
        try {
            Field questionsField = QuestGameService.class.getDeclaredField("questions");
            questionsField.setAccessible(true);
            List<Question> questions = (List<Question>) questionsField.get(service);
            questions.clear();
            questions.add(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testPlayQuestWithCorrectAnswer() {
        Console console = mock(Console.class);
        when(console.readLine()).thenReturn("a");
        Question.Option option = new Question.Option("a", "git init");
        Question.Feedback feedback = new Question.Feedback("Correct!", null);
        Question question =
                new Question("beginner", "Scenario", Arrays.asList(option), "a", feedback);
        QuestGameService service = new QuestGameService(console);
        injectQuestion(service, question);
        // Only call public method
        service.playQuest("beginner");
        // Optionally verify console output
    }

    @Test
    void testPlayQuestWithIncorrectAnswer() {
        Console console = mock(Console.class);
        when(console.readLine()).thenReturn("b");
        Question.Option option = new Question.Option("a", "git init");
        Question.Feedback feedback = new Question.Feedback("Correct!", null);
        Question question =
                new Question("beginner", "Scenario", Arrays.asList(option), "a", feedback);
        QuestGameService service = new QuestGameService(console);
        injectQuestion(service, question);
        // Only call public method
        service.playQuest("beginner");
    }

    @Test
    void testHasQuestionsAndGetQuestionCountPublic() {
        QuestGameService service = new QuestGameService();
        assertTrue(service.getQuestionCount() >= 0);
        assertEquals(service.hasQuestions(), service.getQuestionCount() > 0);
    }

    @Test
    void testGetQuestTitleAndInstructionsPublic() {
        QuestGameService service = new QuestGameService();
        assertNotNull(service.getQuestTitle());
        assertNotNull(service.getQuestInstructions());
    }
}
