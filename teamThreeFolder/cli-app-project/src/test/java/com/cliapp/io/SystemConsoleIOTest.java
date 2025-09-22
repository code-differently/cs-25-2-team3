package com.cliapp.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SystemConsoleIOTest {
    @Test
    void testPrintAndPrintln() {
        SystemConsoleIO console = new SystemConsoleIO();
        assertDoesNotThrow(() -> console.print("Hello"));
        assertDoesNotThrow(() -> console.println("World"));
    }

    @Test
    void testPrintf() {
        SystemConsoleIO console = new SystemConsoleIO();
        assertDoesNotThrow(() -> console.printf("%s %d", "Test", 123));
    }
}
