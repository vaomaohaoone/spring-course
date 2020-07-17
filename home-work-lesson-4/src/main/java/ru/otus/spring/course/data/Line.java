package ru.otus.spring.course.data;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Line {
    private final String question;
    private final List<String> options;
    private final int answer;
}
