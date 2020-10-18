package ru.otus.spring.course.domain;

import lombok.Value;

@Value
public class Portfolio {
    String name;
    String surname;
    int age;
    Skill skill;
}
