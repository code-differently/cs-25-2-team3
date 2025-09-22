package com.cliapp.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestConsoleTest {
    private TestConsole console;

    @BeforeEach
    void setUp() {
        console = new TestConsole();
    }

    @Test
    void testAddInputAndReadLine() {
        console.addInput("foo");
        assertEquals("foo", console.readLine());
        assertFalse(console.hasMoreInputs());
    }

    @Test
    void testReadLineDefault() {
        assertEquals("5", console.readLine());
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
    void testClearOutputs() {
        console.print("foo");
        console.clearOutputs();
        assertTrue(console.getOutputs().isEmpty());
    }

    @Test
    void testGetFullOutput() {
        console.print("a");
        console.print("b");
        assertEquals("ab", console.getFullOutput());
    }

    @Test
    void testClose() {
        assertDoesNotThrow(() -> console.close());
    }
}
