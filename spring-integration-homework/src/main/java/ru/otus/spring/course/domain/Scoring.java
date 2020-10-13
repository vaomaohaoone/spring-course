package ru.otus.spring.course.domain;

import lombok.Value;

@Value
public class Scoring {
    String name;
    String surname;
    int age;
    int points;
}
