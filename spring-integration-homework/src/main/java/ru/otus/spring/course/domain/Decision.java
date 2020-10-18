package ru.otus.spring.course.domain;

import lombok.Value;

@Value
public class Decision {
    String name;
    String surname;
    boolean accepted;
}
