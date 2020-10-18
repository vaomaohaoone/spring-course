package ru.otus.spring.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.domain.Portfolio;
import ru.otus.spring.course.domain.Scoring;

@Service
@Slf4j
public class AnalyticInterview implements Interview {
    @Override
    public Scoring interview(Portfolio portfolio) {
        int points = score();
        log.info(String.format("Candidate %s %s received %d points from %s", portfolio.getName(), portfolio.getSurname(), points, AnalyticInterview.class));
        return new Scoring(portfolio.getName(), portfolio.getSurname(), portfolio.getAge(), points);
    }
}
