package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit test to verify that invalid menu input shows an error and redisplays the menu.
 */
public class AppLaunchMenuValidationTest {

    @Test
    public void testInvalidMenuInput_ShouldShowErrorAndRedisplayMenu() {
        // Save the original System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            App app = new App();

            // Simulate user entering an invalid menu option (0)
            app.handleMenuInput(0);

            String output = outContent.toString();

            // Assert: Should show error message for invalid option
            assertTrue(output.contains("Invalid option"),
                "App should show error message when invalid option entered");

            // Assert: Should redisplay the menu after error
            assertTrue(output.contains("1. Quest"), "Menu should redisplay after invalid input");
            assertTrue(output.contains("5. Glossary"), "Menu should redisplay fully after invalid input");
        } finally {
            // Restore System.out to its original state
            System.setOut(originalOut);
        }
    }
}
