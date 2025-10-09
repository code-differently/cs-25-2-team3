package com.cliapp.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Test implementation of Console for automated testing. Allows pre-programmed inputs and captures
 * outputs.
 */
public class TestConsole implements Console {
    private final Queue<String> inputs = new ConcurrentLinkedQueue<>();
    private final List<String> outputs = new ArrayList<>();

    /** Adds an input line for the next readLine() call. */
    public void addInput(String input) {
        inputs.offer(input);
    }

    /** Gets all captured output lines. */
    public List<String> getOutputs() {
        return new ArrayList<>(outputs);
    }

    /** Gets the last output line. */
    public String getLastOutput() {
        return outputs.isEmpty() ? null : outputs.get(outputs.size() - 1);
    }

    /** Clears all captured outputs. */
    public void clearOutputs() {
        outputs.clear();
    }

    /** Checks if there are any remaining inputs. */
    public boolean hasMoreInputs() {
        return !inputs.isEmpty();
    }

    /** Gets the full output as a single string. */
    public String getFullOutput() {
        return String.join("", outputs);
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
            // Instead of throwing exception, return quit command to avoid infinite loops
            return "5";
        }
        return input;
    }

    @Override
    public void close() {
        // Nothing to close in test implementation
    }
}
