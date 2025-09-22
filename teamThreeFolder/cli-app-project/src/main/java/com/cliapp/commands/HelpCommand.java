package com.cliapp.commands;

import com.cliapp.io.Console;
import java.util.HashMap;
import java.util.Map;

/** Command to display help information about available commands. */
public class HelpCommand implements Command {

    private final Console console;
    private final Map<String, Command> commandRegistry;

    public HelpCommand(Console console) {
        this.console = console;
        this.commandRegistry = new HashMap<>();
    }

    public void registerCommand(String name, Command command) {
        commandRegistry.put(name, command);
    }

    @Override
    public void execute(String[] args) {
        console.println("Available Commands:");
        console.println("==================");

        if (commandRegistry.isEmpty()) {
            // Fallback if registry is empty
            console.println("quest    - Start or continue a quest");
            console.println("glossary - View glossary entries");
            console.println("badge    - View earned badges");
            console.println("continue - Continue saved progress");
            console.println("help     - Show this help message");
            console.println("exit     - Exit the application");
        } else {
            for (Map.Entry<String, Command> entry : commandRegistry.entrySet()) {
                String name = entry.getKey();
                Command command = entry.getValue();
                console.println(String.format("%-10s - %s", name, command.getDescription()));
            }
        }

        console.println("");
        console.println("Type a command name to execute it.");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show available commands and their descriptions";
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public boolean validateArgs(String[] args) {
        return true; // No arguments required
    }
}
