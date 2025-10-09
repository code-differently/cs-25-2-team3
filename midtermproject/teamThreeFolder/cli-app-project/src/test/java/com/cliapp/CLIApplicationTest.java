/* 
package com.cliapp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CLIApplicationTest {

    private CLIApplication cliApplication;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        cliApplication = new CLIApplication();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testCLIApplicationCreation() {
        assertNotNull(cliApplication, "CLI Application should be created");
    }

    @Test
    void testStop() {
        assertDoesNotThrow(() -> cliApplication.stop(), "Stop should not throw exception");
        String output = outputStream.toString();
        assertTrue(
                output.contains("Thanks for using Git Training CLI"),
                "Should display goodbye message");
    }

    @Test
    void testMainMethodExists() {
        // Test that main method exists and can be called without throwing exceptions
        assertDoesNotThrow(
                () -> {
                    // We don't actually call main as it would start the interactive app
                    // Just verify the method exists by accessing it via reflection
                    java.lang.reflect.Method mainMethod =
                            CLIApplication.class.getMethod("main", String[].class);
                    assertNotNull(mainMethod, "Main method should exist");
                    assertTrue(
                            java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()),
                            "Main method should be static");
                    assertTrue(
                            java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()),
                            "Main method should be public");
                },
                "Main method should exist and be accessible");
    }

    @Test
    void testStartMethodExists() {
        // Test that start method exists
        assertDoesNotThrow(
                () -> {
                    java.lang.reflect.Method startMethod = CLIApplication.class.getMethod("start");
                    assertNotNull(startMethod, "Start method should exist");
                    assertTrue(
                            java.lang.reflect.Modifier.isPublic(startMethod.getModifiers()),
                            "Start method should be public");
                },
                "Start method should exist and be accessible");
    }

    @Test
    void testStopMethodExists() {
        // Test that stop method exists
        assertDoesNotThrow(
                () -> {
                    java.lang.reflect.Method stopMethod = CLIApplication.class.getMethod("stop");
                    assertNotNull(stopMethod, "Stop method should exist");
                    assertTrue(
                            java.lang.reflect.Modifier.isPublic(stopMethod.getModifiers()),
                            "Stop method should be public");
                },
                "Stop method should exist and be accessible");
    }

    @Test
    void testConstructorInitializesServices() {
        // Test that constructor completes without throwing exceptions
        assertDoesNotThrow(
                () -> {
                    CLIApplication testApp = new CLIApplication();
                    assertNotNull(testApp, "Constructor should successfully create instance");
                },
                "Constructor should initialize all services without throwing exceptions");
    }

    @Test
    void testStartWithQuitOption() {
        // Simulate user entering "5" to quit
        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        assertDoesNotThrow(
                () -> testApp.start(), "Start should handle quit option without throwing");

        String output = outputStream.toString();
        assertTrue(output.contains("Git Training CLI"), "Should display welcome message");
        assertTrue(output.contains("Main Menu"), "Should display main menu");
        assertTrue(output.contains("Exiting application"), "Should display exit message");
    }

    @Test
    void testStartWithQuestOption() {
        // Simulate user entering "1" for quest then "5" to quit
        String input = "1\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        assertDoesNotThrow(
                () -> testApp.start(), "Start should handle quest option without throwing");

        String output = outputStream.toString();
        assertTrue(output.contains("Available Learning Quests"), "Should display quest list");
    }

    @Test
    void testStartWithContinueOption() {
        // Simulate user entering "2" for continue then "5" to quit
        String input = "2\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        assertDoesNotThrow(
                () -> testApp.start(), "Start should handle continue option without throwing");

        String output = outputStream.toString();
        assertTrue(output.contains("Continue Your Quest"), "Should display continue message");
    }

    @Test
    void testStartWithBadgesOption() {
        // Simulate user entering "3" for badges then "5" to quit
        String input = "3\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        assertDoesNotThrow(
                () -> testApp.start(), "Start should handle badges option without throwing");

        String output = outputStream.toString();
        assertTrue(
                output.contains("Badges") || output.contains("achievements"),
                "Should display badges information");
    }

    @Test
    void testStartWithGlossaryOption() {
        // Simulate user entering "4" for glossary then "5" to quit
        String input = "4\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        assertDoesNotThrow(
                () -> testApp.start(), "Start should handle glossary option without throwing");

        String output = outputStream.toString();
        assertTrue(output.contains("Git Command Glossary"), "Should display glossary");
    }

    @Test
    void testStartWithInvalidOption() {
        // Simulate user entering invalid option "9" then "5" to quit
        String input = "9\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        assertDoesNotThrow(
                () -> testApp.start(), "Start should handle invalid option without throwing");

        String output = outputStream.toString();
        assertTrue(
                output.contains("Invalid choice") || output.contains("❌"),
                "Should display error for invalid choice");
    }

    @Test
    void testWelcomeMessage() {
        // Test welcome message by starting and immediately quitting
        String input = "5\n"; // Quit immediately
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CLIApplication testApp = new CLIApplication();
        testApp.start();

        String fullOutput = outputStream.toString();
        assertTrue(fullOutput.contains("Git Training CLI"), "Should show application name");
        assertTrue(fullOutput.contains("learning journey"), "Should show learning journey message");
    }
}
*/
