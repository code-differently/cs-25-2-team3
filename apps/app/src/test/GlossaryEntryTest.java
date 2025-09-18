package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test to verify that the glossary displays a numbered, accessible list and shows details for a selected command.
 */
public class GlossaryListTest {

    @Test
    public void testGlossaryDisplay_ShouldBeNumberedAndAccessible() {
        // Save the original System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
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
        } finally {
            // Restore System.out to its original state
            System.setOut(originalOut);
        }
    }
}
