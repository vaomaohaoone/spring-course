package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;

import java.io.PrintStream;
import java.util.Scanner;

@AllArgsConstructor
public class ConsoleService {
    private final Scanner reader;
    private final PrintStream writer;

    public ConsoleService() {
        this(new Scanner(System.in), System.out);
    }

    public void write(String line) {
        writer.println(line);
    }

    public String read() {
        return reader.nextLine();
    }

    public int readInt() {
        return reader.nextInt();
    }
}
