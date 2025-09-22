package com.cliapp.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConsoleTest {
    static class DummyConsole implements Console {
        String lastMessage = null;
        boolean closed = false;

        @Override
        public void println(String message) {
            lastMessage = message + "\n";
        }

        @Override
        public void print(String message) {
            lastMessage = message;
        }

        @Override
        public String readLine() {
            return "input";
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    @Test
    void testPrintln() {
        DummyConsole c = new DummyConsole();
        c.println("hello");
        assertEquals("hello\n", c.lastMessage);
    }

    @Test
    void testPrint() {
        DummyConsole c = new DummyConsole();
        c.print("world");
        assertEquals("world", c.lastMessage);
    }

    @Test
    void testReadLine() {
        DummyConsole c = new DummyConsole();
        assertEquals("input", c.readLine());
    }

    @Test
    void testClose() {
        DummyConsole c = new DummyConsole();
        c.close();
        assertTrue(c.closed);
    }
}
