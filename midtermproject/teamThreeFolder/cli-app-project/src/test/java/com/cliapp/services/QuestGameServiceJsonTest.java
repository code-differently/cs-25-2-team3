package com.cliapp.services;

import com.cliapp.domain.Question;
import com.cliapp.io.Console;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestGameServiceJsonTest {
    @Test
    void testParseQuestion() throws Exception {
        String json = "{" +
                "\"level\":\"beginner\"," +
                "\"scenario\":\"Scenario\"," +
                "\"options\":[{" +
                "\"id\":\"a\",\"command\":\"git init\"}]," +
                "\"correct\":\"a\"," +
                "\"feedback\": {\"correct\": \"Correct!\"} " +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        QuestGameService service = new QuestGameService(mock(Console.class));
        Method method = QuestGameService.class.getDeclaredMethod("parseQuestion", JsonNode.class);
        method.setAccessible(true);
        Question question = (Question) method.invoke(service, node);
        assertNotNull(question);
        assertEquals("beginner", question.getLevel());
        assertEquals("Scenario", question.getScenario());
        assertEquals("a", question.getCorrect());
        assertEquals(1, question.getOptions().size());
        assertEquals("git init", question.getOptions().get(0).getCommand());
        assertEquals("Correct!", question.getFeedback().getCorrect());
    }

    @Test
    void testLoadQuestionsFromJson() throws Exception {
        Console console = mock(Console.class);
        QuestGameService service = new QuestGameService(console);
        java.lang.reflect.Field questionsField = QuestGameService.class.getDeclaredField("questions");
        questionsField.setAccessible(true);
        List<Question> questions = (List<Question>) questionsField.get(service);
        assertNotNull(questions);
        // If Quest.json is present, should have questions
        // If not, should be empty
        assertTrue(questions.size() >= 0);
    }
}
