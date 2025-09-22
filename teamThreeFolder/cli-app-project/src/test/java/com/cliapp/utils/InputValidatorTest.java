package com.cliapp.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InputValidatorTest {
    @Test
    void testIsValidInput() {
        assertTrue(InputValidator.isValidInput("abc"));
        assertFalse(InputValidator.isValidInput("   "));
        assertFalse(InputValidator.isValidInput(null));
        assertFalse(InputValidator.isValidInput(""));
    }

    @Test
    void testIsValidInteger() {
        assertTrue(InputValidator.isValidInteger("123"));
        assertFalse(InputValidator.isValidInteger("abc"));
        assertFalse(InputValidator.isValidInteger(""));
        assertFalse(InputValidator.isValidInteger(null));
        assertTrue(InputValidator.isValidInteger(" 42 "));
    }

    @Test
    void testIsInRange() {
        assertTrue(InputValidator.isInRange(5, 1, 10));
        assertFalse(InputValidator.isInRange(0, 1, 10));
        assertTrue(InputValidator.isInRange(1, 1, 10));
        assertTrue(InputValidator.isInRange(10, 1, 10));
        assertFalse(InputValidator.isInRange(11, 1, 10));
    }

    @Test
    void testIsValidChoice() {
        assertTrue(InputValidator.isValidChoice("1", 3));
        assertTrue(InputValidator.isValidChoice("3", 3));
        assertFalse(InputValidator.isValidChoice("0", 3));
        assertFalse(InputValidator.isValidChoice("4", 3));
        assertFalse(InputValidator.isValidChoice("abc", 3));
        assertFalse(InputValidator.isValidChoice("", 3));
    }

    @Test
    void testSanitizeInput() {
        assertEquals("abc", InputValidator.sanitizeInput("  abc  "));
        assertEquals("", InputValidator.sanitizeInput("   "));
        assertEquals("", InputValidator.sanitizeInput(null));
        assertEquals("hello", InputValidator.sanitizeInput("hello"));
    }
}
