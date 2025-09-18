package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

/**
 * Unit test to verify that the main menu options are displayed on application launch.
 */
public class AppLaunchMenuTest {

    @Test
    void optionsShowOnLaunch_ShouldDisplayMenuOptions() {
        // Capture the original System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // Run the app (update MainApp.launch() to your actual entrypoint if needed)
            MainApp.launch();

            // Grab the output
            String output = outContent.toString();

            // Check that all the menu options show up in the output
            assertTrue(output.contains("Quest"), "Menu missing Quest option");
            assertTrue(output.contains("Continue"), "Menu missing Continue option");
            assertTrue(output.contains("Quit"), "Menu missing Quit option");
            assertTrue(output.contains("Badges"), "Menu missing Badges option");
            assertTrue(output.contains("Glossary"), "Menu missing Glossary option");
        } finally {
            // Reset System.out to its original state
            System.setOut(originalOut);
        }
    }
}

