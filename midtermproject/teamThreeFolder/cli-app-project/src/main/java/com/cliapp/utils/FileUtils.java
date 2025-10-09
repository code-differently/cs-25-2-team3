package com.cliapp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Utility class for file operations. */
public class FileUtils {

    /** Check if a file exists at the given path. */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /** Read all content from a file as string. */
    public static String readFileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return new String(Files.readAllBytes(path));
    }

    /** Write content to a file. */
    public static void writeToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes());
    }

    /** Create directory if it doesn't exist. */
    public static void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /** Get the resource path for the application. */
    public static String getResourcePath(String fileName) {
        return "src/main/resources/" + fileName;
    }
}
