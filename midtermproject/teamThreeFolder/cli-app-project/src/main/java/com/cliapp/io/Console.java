package com.cliapp.io;

/** Interface for console input/output operations to enable testing */
public interface Console {
    void println(String message);

    void print(String message);

    String readLine();

    void close();
}
