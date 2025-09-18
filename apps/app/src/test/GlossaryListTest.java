import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class GlossaryListTest {

    @Test
    public void testGlossaryDisplay_ShouldBeNumberedAndAccessible() {
        // Catch CLI output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Arrange: sample glossary
        GlossaryCommand cmd1 = new GlossaryCommand("git commit", "Saves changes locally", Arrays.asList("git commit -m \"msg\""));
        GlossaryCommand cmd2 = new GlossaryCommand("git push", "Sends changes to remote repo", Arrays.asList("git push origin main"));
        List<GlossaryCommand> glossary = Arrays.asList(cmd1, cmd2);

        GlossaryApp glossaryApp = new GlossaryApp(glossary);

        // Act: show glossary
        glossaryApp.showGlossary();

        // Grab CLI output
        String output = outContent.toString().trim();
        String[] lines = output.split("\n");

        // Assert: numbered display
        assertEquals("1. git commit", lines[0].trim());
        assertEquals("2. git push", lines[1].trim());

        // Simulate selecting a glossary command
        outContent.reset();
        glossaryApp.showCommandDetail(1); // select first command

        String detailOutput = outContent.toString();
        assertTrue(detailOutput.contains("Saves changes locally"), "Definition should display when command selected");
        assertTrue(detailOutput.contains("git commit -m \"msg\""), "Example should display when command selected");

        // Reset System.out
        System.setOut(System.out);
    }
}

