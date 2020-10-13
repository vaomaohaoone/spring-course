package ru.otus.spring.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.domain.Decision;
import ru.otus.spring.course.domain.Scoring;

@Service
@Slf4j
public class HeadDepartment {
    public Decision decide(Scoring scoring) throws InterruptedException {
        Thread.sleep(1000);
        Decision result = new Decision(scoring.getName(), scoring.getSurname(), checkAge(scoring.getAge()) && checkPoints(scoring.getPoints()));
        log.info(String.format("Head department result for candidate %s %s was %b", result.getName(), result.getSurname(), result.isAccepted()));
        return result;
    }

    private boolean checkAge(int age) {
        return age >= 18 && age < 60;
    }

    private boolean checkPoints(int points) {
        return points >= 60;
    }
}
