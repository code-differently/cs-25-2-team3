import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class GlossaryIntegrationTest {

    @Test
    public void testWrongQuestAnswer_ShouldPullGlossaryInfo() {
        // Catch CLI output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Arrange: user, quest, module, glossary
        User user = new User("Jayden");

        LearningModule mod = new LearningModule(
            "Git Basics",
            "Beginner",
            "Incomplete"
        );
        mod.setScenario("Which command saves changes locally?");
        mod.setCorrectAnswer("git commit");

        GlossaryCommand gitCommit = new GlossaryCommand(
            "git commit",
            "Saves changes locally to the repository",
            Arrays.asList("git commit -m \"message\"")
        );

        GlossaryApp glossary = new GlossaryApp(Arrays.asList(gitCommit));

        Quest quest = new Quest("Dev Quest", 2, false);
        quest.addLearningModule(mod);
        quest.setGlossary(glossary);

        // Act: user selects module and gives wrong answer
        quest.selectLearningModule(1);
        quest.submitAnswer(user, "git push"); // wrong

        // Grab CLI output
        String output = outContent.toString();

        // Assert: glossary pulled the correct info
        assertTrue(output.contains("Saves changes locally to the repository"), "Glossary should display the definition of the correct command");
        assertTrue(output.contains("git commit -m \"message\""), "Glossary should display the example of the correct command");

        // Reset System.out
        System.setOut(System.out);
    }
}

