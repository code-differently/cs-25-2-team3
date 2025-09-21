package com.cliapp.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.exceptions.InvalidCommandException;
import com.cliapp.exceptions.InvalidInputException;
import com.cliapp.exceptions.NoSavedGameException;
import org.junit.jupiter.api.Test;

/** Integration test for custom exceptions usage throughout the application */
public class ExceptionIntegrationTest {

    @Test
    void testInvalidInputException_CreationAndUsage() {
        // Test basic exception creation
        InvalidInputException basicException = new InvalidInputException("Test message");
        assertEquals("Test message", basicException.getMessage());

        // Test convenience methods
        InvalidInputException menuException = InvalidInputException.forInvalidMenuChoice("9");
        assertTrue(menuException.getMessage().contains("Invalid menu choice"));
        assertTrue(menuException.getMessage().contains("1-5"));

        InvalidInputException questException = InvalidInputException.forInvalidQuestNumber("10", 5);
        assertTrue(questException.getMessage().contains("Invalid quest number"));
        assertTrue(questException.getMessage().contains("1-5"));

        InvalidInputException emptyException = InvalidInputException.forEmptyInput();
        assertTrue(emptyException.getMessage().contains("cannot be empty"));
    }

    @Test
    void testInvalidCommandException_CreationAndUsage() {
        // Test basic exception creation
        InvalidCommandException basicException = new InvalidCommandException("Test command error");
        assertEquals("Test command error", basicException.getMessage());

        // Test convenience methods
        InvalidCommandException unknownCommand =
                InvalidCommandException.forUnknownCommand("invalid-cmd");
        assertTrue(unknownCommand.getMessage().contains("Unknown command"));
        assertTrue(unknownCommand.getMessage().contains("invalid-cmd"));

        InvalidCommandException invalidArgs =
                InvalidCommandException.forInvalidArguments("quest", "quest [id]");
        assertTrue(invalidArgs.getMessage().contains("Invalid arguments"));
        assertTrue(invalidArgs.getMessage().contains("quest [id]"));

        InvalidCommandException missingArgs =
                InvalidCommandException.forMissingArguments("start", "quest-id");
        assertTrue(missingArgs.getMessage().contains("Missing required arguments"));
        assertTrue(missingArgs.getMessage().contains("quest-id"));
    }

    @Test
    void testNoSavedGameException_CreationAndUsage() {
        // Test basic exception creation
        NoSavedGameException basicException = new NoSavedGameException("No saved data");
        assertEquals("No saved data", basicException.getMessage());

        // Test convenience methods
        NoSavedGameException noQuest = NoSavedGameException.forNoActiveQuest();
        assertTrue(noQuest.getMessage().contains("No active quest found"));

        NoSavedGameException noProgress = NoSavedGameException.forNoSavedProgress();
        assertTrue(noProgress.getMessage().contains("No saved progress"));

        NoSavedGameException noSession = NoSavedGameException.forMissingSession();
        assertTrue(noSession.getMessage().contains("User session not found"));
    }

    @Test
    void testExceptionHierarchy() {
        // Verify exceptions extend Exception properly
        InvalidInputException inputEx = new InvalidInputException("test");
        assertTrue(inputEx instanceof Exception);

        InvalidCommandException cmdEx = new InvalidCommandException("test");
        assertTrue(cmdEx instanceof Exception);

        NoSavedGameException gameEx = new NoSavedGameException("test");
        assertTrue(gameEx instanceof Exception);
    }

    @Test
    void testExceptionWithCause() {
        // Test exceptions with underlying causes
        RuntimeException cause = new RuntimeException("Original error");

        InvalidInputException inputEx = new InvalidInputException("Input error", cause);
        assertEquals("Input error", inputEx.getMessage());
        assertEquals(cause, inputEx.getCause());

        InvalidCommandException cmdEx = new InvalidCommandException("Command error", cause);
        assertEquals("Command error", cmdEx.getMessage());
        assertEquals(cause, cmdEx.getCause());

        NoSavedGameException gameEx = new NoSavedGameException("Game error", cause);
        assertEquals("Game error", gameEx.getMessage());
        assertEquals(cause, gameEx.getCause());
    }
}
