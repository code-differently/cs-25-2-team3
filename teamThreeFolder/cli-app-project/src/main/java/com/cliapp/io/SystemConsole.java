package com.cliapp.io;

import java.util.Scanner;

/** System console implementation for production use */
public class SystemConsole implements Console {
    private final Scanner scanner;

    public SystemConsole() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
