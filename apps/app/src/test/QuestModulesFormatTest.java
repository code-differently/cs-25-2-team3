import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class QuestModulesFormatTest {

    @Test
    public void testQuestModules_ShouldDisplayStructuredListFormat() {
        // Catch CLI output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Arrange: build a quest with modules
        Quest quest = new Quest("Dev Quest", 3, false);
        quest.addLearningModule(new LearningModule("Intro to Git", "Beginner", "Incomplete"));
        quest.addLearningModule(new LearningModule("Java Basics", "Intermediate", "Complete"));
        quest.addLearningModule(new LearningModule("Advanced Algorithms", "Advanced", "Incomplete"));

        // Act: simulate showing the module list
        quest.showLearningModules();

        // Grab the CLI text
        String output = outContent.toString().trim();

        // Split lines to check order + numbering
        String[] lines = output.split("\n");

        assertEquals("1. Intro to Git - Beginner - Incomplete", lines[0].trim());
        assertEquals("2. Java Basics - Intermediate - Complete", lines[1].trim());
        assertEquals("3. Advanced Algorithms - Advanced - Incomplete", lines[2].trim());

        // Restore System.out
        System.setOut(System.out);
    }
}
