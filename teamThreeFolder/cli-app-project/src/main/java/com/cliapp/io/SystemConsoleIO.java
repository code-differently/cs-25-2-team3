package com.cliapp.io;

import java.util.Scanner;

/**
 * Production implementation of ConsoleIO using System.in and System.out.
 */
public class SystemConsoleIO implements ConsoleIO {
    private final Scanner scanner;

    public SystemConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public String readLine(String prompt) {
        print(prompt);
        return readLine();
    }

    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}
