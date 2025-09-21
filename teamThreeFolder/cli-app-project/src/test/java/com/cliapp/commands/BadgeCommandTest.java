package com.cliapp.commands;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.services.BadgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BadgeCommandTest {

    private BadgeCommand badgeCommand;
    private BadgeService badgeService;

    @BeforeEach
    void setUp() {
        badgeService = new BadgeService();
        badgeCommand = new BadgeCommand(badgeService);
    }

    @Test
    void testBadgeCommandCreation() {
        assertNotNull(badgeCommand, "Badge command should be created");
        assertEquals("badges", badgeCommand.getName(), "Command name should be 'badges'");
        assertNotNull(badgeCommand.getDescription(), "Description should not be null");
        assertNotNull(badgeCommand.getUsage(), "Usage should not be null");
    }

    @Test
    void testExecuteCommand() {
        assertDoesNotThrow(
                () -> badgeCommand.execute(new String[] {}), "Execute should not throw exception");
        assertDoesNotThrow(
                () -> badgeCommand.execute(new String[] {"arg1"}),
                "Execute with args should not throw exception");
        assertDoesNotThrow(
                () -> badgeCommand.execute(null), "Execute with null should not throw exception");
    }

    @Test
    void testCommandProperties() {
        assertEquals("badges", badgeCommand.getName(), "Name should be 'badges'");
        assertTrue(badgeCommand.getDescription().length() > 0, "Description should not be empty");
        assertTrue(badgeCommand.getUsage().length() > 0, "Usage should not be empty");
        assertTrue(badgeCommand.validateArgs(new String[] {}), "Should validate empty args");
        assertTrue(badgeCommand.validateArgs(null), "Should validate null args");
    }
}
