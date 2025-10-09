package com.cliapp.io;

/**
 * Interface for console input/output operations. This abstraction allows for testable CLI
 * interactions.
 */
public interface ConsoleIO {
    /**
     * Prints a message to the console.
     *
     * @param message the message to print
     */
    void print(String message);

    /**
     * Prints a message to the console followed by a newline.
     *
     * @param message the message to print
     */
    void println(String message);

    /**
     * Reads a line of input from the console.
     *
     * @return the input line
     */
    String readLine();

    /**
     * Reads a line of input with a prompt.
     *
     * @param prompt the prompt to display
     * @return the input line
     */
    String readLine(String prompt);

    /**
     * Prints a formatted message to the console.
     *
     * @param format the format string
     * @param args the arguments
     */
    void printf(String format, Object... args);
}
