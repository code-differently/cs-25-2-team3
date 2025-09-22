package com.cliapp.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cliapp.io.Console;
import org.junit.jupiter.api.Test;

class HelpCommandAndRegistryTest {
    @Test
    void testHelpCommandExecuteWithEmptyRegistry() {
        Console console = mock(Console.class);
        HelpCommand helpCommand = new HelpCommand(console);
        helpCommand.execute(new String[] {});
        verify(console, atLeastOnce()).println(contains("Available Commands"));
        verify(console, atLeastOnce()).println(contains("help"));
    }

    @Test
    void testHelpCommandRegisterCommandAndExecute() {
        Console console = mock(Console.class);
        HelpCommand helpCommand = new HelpCommand(console);
        Command dummy = mock(Command.class);
        when(dummy.getDescription()).thenReturn("desc");
        helpCommand.registerCommand("dummy", dummy);
        helpCommand.execute(new String[] {});
        verify(console, atLeastOnce()).println(contains("dummy"));
        verify(console, atLeastOnce()).println(contains("desc"));
    }

    @Test
    void testCommandRegistryRegisterAndGetCommand() {
        Console console = mock(Console.class);
        CommandRegistry registry = new CommandRegistry(console);
        Command dummy = mock(Command.class);
        registry.register("dummy", dummy);
        assertTrue(registry.hasCommand("dummy"));
        assertEquals(dummy, registry.getCommand("dummy"));
    }

    @Test
    void testCommandRegistryGetAllCommands() {
        Console console = mock(Console.class);
        CommandRegistry registry = new CommandRegistry(console);
        Command dummy = mock(Command.class);
        registry.register("dummy", dummy);
        assertTrue(registry.getAllCommands().containsKey("dummy"));
    }
}
