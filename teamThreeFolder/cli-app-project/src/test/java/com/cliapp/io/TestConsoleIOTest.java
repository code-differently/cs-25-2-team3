package com.cliapp.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestConsoleIOTest {
    private TestConsoleIO console;

    @BeforeEach
    void setUp() {
        console = new TestConsoleIO();
    }

    @Test
    void testAddInputAndReadLine() {
        console.addInput("foo");
        assertEquals("foo", console.readLine());
        assertFalse(console.hasMoreInputs());
    }

    @Test
    void testReadLineDefault() {
        assertEquals("a", console.readLine());
    }

    @Test
    void testPrintAndOutputs() {
        console.print("bar");
        List<String> outputs = console.getOutputs();
        assertEquals(1, outputs.size());
        assertEquals("bar", outputs.get(0));
    }

    @Test
    void testPrintln() {
        console.println("baz");
        assertTrue(console.getLastOutput().contains("baz"));
    }

    @Test
    void testReadLineWithPrompt() {
        console.addInput("input");
        String result = console.readLine("Prompt: ");
        assertEquals("input", result);
        assertTrue(console.getOutputs().get(0).contains("Prompt: "));
    }

    @Test
    void testPrintf() {
        console.printf("Hello %s!", "world");
        assertEquals("Hello world!", console.getLastOutput());
    }

    @Test
    void testClearOutputs() {
        console.print("foo");
        console.clearOutputs();
        assertTrue(console.getOutputs().isEmpty());
    }
}
