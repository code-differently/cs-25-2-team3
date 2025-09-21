package com.cliapp.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cliapp.domain.GlossaryEntry;
import com.cliapp.services.GlossaryService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GlossaryCommandTest {

    @Mock private GlossaryService glossaryService;

    private GlossaryCommand glossaryCommand;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        glossaryCommand = new GlossaryCommand(glossaryService);

        // Capture output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testGlossaryCommandCreation() {
        assertNotNull(glossaryCommand, "Glossary command should be created");
        assertEquals("glossary", glossaryCommand.getName(), "Command name should be 'glossary'");
        assertNotNull(glossaryCommand.getDescription(), "Description should not be null");
        assertNotNull(glossaryCommand.getUsage(), "Usage should not be null");
    }

    @Test
    void testCommandProperties() {
        assertEquals("glossary", glossaryCommand.getName());
        assertEquals(
                "Display Git command glossary and definitions", glossaryCommand.getDescription());
        assertEquals("glossary", glossaryCommand.getUsage());
        assertTrue(glossaryCommand.validateArgs(new String[] {}));
        assertTrue(glossaryCommand.validateArgs(new String[] {"search", "git"}));
        assertTrue(glossaryCommand.validateArgs(null));
    }

    @Test
    void testValidateArgs() {
        assertTrue(glossaryCommand.validateArgs(new String[] {}), "Should validate empty args");
        assertTrue(
                glossaryCommand.validateArgs(new String[] {"search"}),
                "Should validate single arg");
        assertTrue(glossaryCommand.validateArgs(null), "Should validate null args");
    }

    @Test
    void testExecute_EmptyGlossary() {
        // Arrange
        when(glossaryService.getAllEntries()).thenReturn(Collections.emptyList());

        // Act
        glossaryCommand.execute(new String[] {});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("=== Git Command Glossary ==="));
        assertTrue(output.contains("No commands available"));
        verify(glossaryService).getAllEntries();

        // Restore output
        System.setOut(originalOut);
    }

    @Test
    void testExecute_WithGlossaryEntries() {
        // Arrange
        GlossaryEntry entry1 =
                new GlossaryEntry(
                        "git init", "Initialize a new Git repository", "git init", "Basic");
        GlossaryEntry entry2 =
                new GlossaryEntry("git add", "Add files to staging area", "git add .", "Basic");
        List<GlossaryEntry> entries = Arrays.asList(entry1, entry2);

        when(glossaryService.getAllEntries()).thenReturn(entries);

        // Act
        glossaryCommand.execute(new String[] {});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("=== Git Command Glossary ==="));
        assertTrue(output.contains("Available Git Commands:"));
        assertTrue(output.contains("1. git init - Initialize a new Git repository"));
        assertTrue(output.contains("2. git add - Add files to staging area"));
        assertTrue(output.contains("Total commands: 2"));
        verify(glossaryService).getAllEntries();

        // Restore output
        System.setOut(originalOut);
    }

    @Test
    void testExecute_SingleEntry() {
        // Arrange
        GlossaryEntry entry =
                new GlossaryEntry(
                        "git commit",
                        "Save changes to repository",
                        "git commit -m 'message'",
                        "Basic");
        List<GlossaryEntry> entries = Arrays.asList(entry);

        when(glossaryService.getAllEntries()).thenReturn(entries);

        // Act
        glossaryCommand.execute(new String[] {});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("1. git commit - Save changes to repository"));
        assertTrue(output.contains("Total commands: 1"));

        // Restore output
        System.setOut(originalOut);
    }
}
