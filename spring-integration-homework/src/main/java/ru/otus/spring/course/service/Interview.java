package ru.otus.spring.course.service;

import org.apache.commons.lang3.RandomUtils;
import ru.otus.spring.course.domain.Portfolio;
import ru.otus.spring.course.domain.Scoring;

public interface Interview {
    Scoring interview(Portfolio portfolio);

    default int score() {
        return RandomUtils.nextInt(0, 100);
    }
}