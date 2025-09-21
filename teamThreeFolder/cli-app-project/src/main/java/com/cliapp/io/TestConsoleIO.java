package com.cliapp.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Test implementation of ConsoleIO for automated testing.
 * Allows pre-programmed inputs and captures outputs.
 */
public class TestConsoleIO implements ConsoleIO {
    private final Queue<String> inputs = new ConcurrentLinkedQueue<>();
    private final List<String> outputs = new ArrayList<>();

    /**
     * Adds an input line for the next readLine() call.
     */
    public void addInput(String input) {
        inputs.offer(input);
    }

    /**
     * Gets all captured output lines.
     */
    public List<String> getOutputs() {
        return new ArrayList<>(outputs);
    }

    /**
     * Gets the last output line.
     */
    public String getLastOutput() {
        return outputs.isEmpty() ? null : outputs.get(outputs.size() - 1);
    }

    /**
     * Clears all captured outputs.
     */
    public void clearOutputs() {
        outputs.clear();
    }

    /**
     * Checks if there are any remaining inputs.
     */
    public boolean hasMoreInputs() {
        return !inputs.isEmpty();
    }

    @Override
    public void print(String message) {
        outputs.add(message);
    }

    @Override
    public void println(String message) {
        outputs.add(message + System.lineSeparator());
    }

    @Override
    public String readLine() {
        String input = inputs.poll();
        if (input == null) {
            throw new RuntimeException("No more test inputs available");
        }
        return input;
    }

    @Override
    public String readLine(String prompt) {
        print(prompt);
        return readLine();
    }

    @Override
    public void printf(String format, Object... args) {
        outputs.add(String.format(format, args));
    }
}
