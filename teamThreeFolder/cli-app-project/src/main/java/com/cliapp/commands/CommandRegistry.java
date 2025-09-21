package com.cliapp.commands;

import com.cliapp.io.Console;
import java.util.Map;
import java.util.HashMap;

/**
 * Registry for managing available commands.
 */
public class CommandRegistry {
    
    private final Map<String, Command> commands;
    private final Console console;
    
    public CommandRegistry(Console console) {
        this.console = console;
        this.commands = new HashMap<>();
        registerDefaultCommands();
    }
    
    private void registerDefaultCommands() {
        register("help", new HelpCommand(console));
        register("exit", new ExitCommand(console));
        
        // For now, register basic commands - can be enhanced later
        // These would need proper service injection in a full implementation
    }
    
    public void register(String name, Command command) {
        commands.put(name.toLowerCase(), command);
        
        // If registering with HelpCommand, add to its registry too
        if (commands.containsKey("help") && commands.get("help") instanceof HelpCommand) {
            HelpCommand helpCommand = (HelpCommand) commands.get("help");
            helpCommand.registerCommand(name, command);
        }
    }
    
    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }
    
    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
    }
    
    public Map<String, Command> getAllCommands() {
        return new HashMap<>(commands);
    }
}
