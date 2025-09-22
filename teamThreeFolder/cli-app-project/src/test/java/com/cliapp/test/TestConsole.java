package com.cliapp.test;

import com.cliapp.io.Console;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

/** Test double for Console that captures output and provides pre-defined input */
public class TestConsole implements Console {
    private final List<String> output = new ArrayList<>();
    private final Queue<String> inputQueue = new ArrayDeque<>();
    private boolean closed = false;

    public void addInput(String input) {
        inputQueue.offer(input);
    }

    public void addInputs(String... inputs) {
        for (String input : inputs) {
            inputQueue.offer(input);
        }
    }

    public void setInput(String... inputs) { // adapter for tests
        if (inputs == null) {
            return;
        }
        for (String in : inputs) {
            inputQueue.offer(in == null ? "" : in);
        }
    }

    public String getOutput() { // aggregate output as single string (adapter for tests)
        return String.join("", output).replace("\n", System.lineSeparator());
    }

    public List<String> getOutputLines() {
        return new ArrayList<>(output);
    }

    public String getAllOutput() {
        return String.join("\n", output);
    }

    public boolean wasClosed() {
        return closed;
    }

    public void clearOutput() {
        output.clear();
    }

    @Override
    public void println(String message) {
        output.add((message == null ? "null" : message) + System.lineSeparator());
    }

    @Override
    public void print(String message) {
        output.add(message == null ? "null" : message);
    }

    @Override
    public String readLine() {
        if (inputQueue.isEmpty()) {
            return "a"; // Return default answer for quiz/test flows
        }
        return inputQueue.poll();
    }

    @Override
    public void close() {
        closed = true;
    }

    public Stream<String> stream() {
        return output.stream();
    }
}
