package com.cliapp.commands;

/** Base interface for all CLI commands */
public interface Command {

    /** Execute the command with given arguments */
    void execute(String[] args);

    /** Get command name */
    String getName();

    /** Get command description */
    String getDescription();

    /** Get command usage */
    String getUsage();

    /** Validate command arguments */
    boolean validateArgs(String[] args);
}
