package com.cliapp.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cliapp.collections.QuestCollection;
import com.cliapp.domain.Quest;
import com.cliapp.services.BadgeManager;
import com.cliapp.io.Console;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestListCommandTest {

    private QuestListCommand questListCommand;
    private QuestCollection questCollection;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        questCollection = new QuestCollection();
        questListCommand = new QuestListCommand(questCollection, true); // Use test mode

        // Capture output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore output
        System.setOut(originalOut);
    }

    @Test
    void testQuestListCommandCreation() {
        assertNotNull(questListCommand, "Quest list command should be created");
        assertEquals("quest", questListCommand.getName(), "Command name should be 'quest'");
        assertNotNull(questListCommand.getDescription(), "Description should not be null");
        assertNotNull(questListCommand.getUsage(), "Usage should not be null");
    }

    @Test
    void testCommandProperties() {
        assertEquals("quest", questListCommand.getName());
        assertEquals(
                "Display all available quests with their learning modules, difficulty, and completion status",
                questListCommand.getDescription());
        assertEquals("quest", questListCommand.getUsage());
        assertTrue(questListCommand.validateArgs(new String[] {}));
        assertTrue(questListCommand.validateArgs(null));
    }

    @Test
    void testExecute_EmptyQuestCollection() {
        // Act
        questListCommand.execute(new String[] {});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("=== Available Learning Quests ==="));
        assertTrue(output.contains("No quests available"));
    }

    @Test
    void testExecute_WithQuests() {
        // Arrange
        Quest quest1 =
                new Quest(
                        "git-1",
                        "Git Fundamentals",
                        "Learn basic Git commands",
                        Arrays.asList("Learn git init", "Learn git add", "Learn git commit"),
                        1);
        Quest quest2 =
                new Quest(
                        "git-2",
                        "Advanced Git",
                        "Master advanced Git features",
                        Arrays.asList("Learn git branch", "Learn git merge"),
                        3);
        quest2.setCompleted(true);

        questCollection.add(quest1);
        questCollection.add(quest2);

        // Act
        questListCommand.execute(new String[] {});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("=== Available Learning Quests ==="));
        assertTrue(output.contains("Git Fundamentals"));
        assertTrue(output.contains("Advanced Git"));
        assertTrue(output.contains("*")); // Difficulty 1
        assertTrue(output.contains("***")); // Difficulty 3
        assertTrue(output.contains("Status: N")); // Not completed
        assertTrue(output.contains("Status: Y")); // Completed
        assertTrue(output.contains("Learn git init"));
        assertTrue(output.contains("Learn git branch"));
        assertTrue(output.contains("Total quests available: 2"));
    }

    @Test
    void testExecute_SingleQuest() {
        // Arrange
        Quest quest =
                new Quest(
                        "test-1",
                        "Test Quest",
                        "Test description",
                        Arrays.asList("Module 1", "Module 2"),
                        5);
        questCollection.add(quest);

        // Act
        questListCommand.execute(new String[] {});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Test Quest"));
        assertTrue(output.contains("*****")); // Difficulty 5
        assertTrue(output.contains("Status: N")); // Not completed
        assertTrue(output.contains("Module 1"));
        assertTrue(output.contains("Module 2"));
        assertTrue(output.contains("Total quests available: 1"));
    }

    @Test
    void testValidateArgs() {
        assertTrue(questListCommand.validateArgs(new String[] {}), "Should validate empty args");
        assertTrue(
                questListCommand.validateArgs(new String[] {"completed"}),
                "Should validate single arg");
        assertTrue(questListCommand.validateArgs(null), "Should validate null args");
    }
}
