package ru.otus.spring.course.domain;

public enum Skill {
    DEVELOPING("DEVELOPING"), ANALYSIS("ANALYSIS"), TESTING("TESTING");

    private final String name;

    Skill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
