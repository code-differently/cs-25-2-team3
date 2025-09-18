package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit test to verify that the main menu displays a structured, numbered list.
 */
public class AppLaunchMenuFormatTest {

    @Test
    public void testMenuShowsStructuredListFormat_ShouldBeNumberedAndClean() {
        // Save the original System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // Act: Launch the app menu
            App app = new App();
            app.showMenu(); // assuming this prints the menu to CLI

            // Grab the printed output
            String output = outContent.toString();

            // Check each option shows up in order, numbered properly
            assertTrue(output.contains("1. Quest"), "Menu should show '1. Quest'");
            assertTrue(output.contains("2. Continue"), "Menu should show '2. Continue'");
            assertTrue(output.contains("3. Quit"), "Menu should show '3. Quit'");
            assertTrue(output.contains("4. Badges"), "Menu should show '4. Badges'");
            assertTrue(output.contains("5. Glossary"), "Menu should show '5. Glossary'");

            // Double check the layout has the numbers in order
            String[] lines = output.trim().split("\n");
            assertEquals("1. Quest", lines[0].trim());
            assertEquals("2. Continue", lines[1].trim());
            assertEquals("3. Quit", lines[2].trim());
            assertEquals("4. Badges", lines[3].trim());
            assertEquals("5. Glossary", lines[4].trim());
        } finally {
            // Restore System.out to its original state
            System.setOut(originalOut);
        }
    }
}

