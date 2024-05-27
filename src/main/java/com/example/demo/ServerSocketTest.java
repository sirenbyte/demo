package com.example.demo;


import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import static com.sun.javafx.font.PrismFontFactory.isWindows;


public class ServerSocketTest {
    public static void main(String[] args) throws Exception {
        runCommand("/", "java");
    }

    public static void runCommand(String whereToRun, String command) throws Exception {
        System.out.println("Running in: " + whereToRun);
        System.out.println("Command: " + command);

        Process process = Runtime.getRuntime().exec(new String[]{"nc", "-l", "56567"});

        OutputStream outputStream = process.getOutputStream();
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        printStream(inputStream);
        printStream(errorStream);

        boolean isFinished = process.waitFor(30, TimeUnit.SECONDS);
        outputStream.flush();
        outputStream.close();

        if (!isFinished) {
            process.destroyForcibly();
        }
    }

    private static void printStream(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        }
    }
}