package com.cliapp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLIApplicationTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testShowWelcome() {
        CLIApplication app = new CLIApplication();
        app.start(); // Will wait for input, so simulate exit
        String simulatedInput = "exit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        app.start();
        String output = outContent.toString();
        assertTrue(output.contains("Welcome to the CLI Application!"));
    }

    @Test
    void testExitCommandStopsApp() {
        CLIApplication app = new CLIApplication();
        String simulatedInput = "exit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        app.start();
        String output = outContent.toString();
        assertTrue(output.contains("Exiting CLI Application. Goodbye!"));
    }

    @Test
    void testHelpCommandShowsHelp() {
        CLIApplication app = new CLIApplication();
        String simulatedInput = "help\nexit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        app.start();
        String output = outContent.toString();
        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("help - Show this help message"));
        assertTrue(output.contains("exit - Exit the application"));
    }

    @Test
    void testProcessInputReturnsCorrectValue() {
        CLIApplication app = new CLIApplication();
        String simulatedInput = "testinput\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        String result = app.processInput();
        assertEquals("testinput", result);
    }

    @Test
    void testStopClosesScannerAndPrintsExit() {
        CLIApplication app = new CLIApplication();
        app.stop();
        String output = outContent.toString();
        assertTrue(output.contains("Exiting CLI Application. Goodbye!"));
    }
}
