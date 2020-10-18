package ru.otus.spring.course;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.course.config.IntegrationConfig;
import ru.otus.spring.course.domain.Portfolio;
import ru.otus.spring.course.domain.Scoring;
import ru.otus.spring.course.domain.Skill;
import ru.otus.spring.course.service.AnalyticInterview;
import ru.otus.spring.course.service.DevelopmentInterview;
import ru.otus.spring.course.service.HeadDepartment;
import ru.otus.spring.course.service.TestingInterview;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {IntegrationConfig.class, AnalyticInterview.class, DevelopmentInterview.class, TestingInterview.class,
        HeadDepartment.class})
public class InterviewTest {
    @Autowired
    private MessageChannel portfolioChannel;
    @MockBean
    private AnalyticInterview analyticInterview;
    @MockBean
    private DevelopmentInterview developmentInterview;
    @MockBean
    private TestingInterview testingInterview;
    @MockBean
    private HeadDepartment headDepartment;

    @Test
    public void test() throws InterruptedException {
        Portfolio portfolioAnalyst = new Portfolio("Alex", "Lasley", 25, Skill.ANALYSIS);
        Scoring scoringAnalyst = new Scoring(portfolioAnalyst.getName(), portfolioAnalyst.getSurname(), portfolioAnalyst.getAge(), RandomUtils.nextInt(60, 100));
        Portfolio portfolioDevelopment = new Portfolio("Mary", "Jane", 28, Skill.DEVELOPING);
        Scoring scoringDevelopment = new Scoring(portfolioDevelopment.getName(), portfolioDevelopment.getSurname(), portfolioDevelopment.getAge(), RandomUtils.nextInt(60, 100));
        Portfolio portfolioTesting = new Portfolio("Jacob", "Van", 49, Skill.TESTING);
        Scoring scoringTesting = new Scoring(portfolioTesting.getName(), portfolioTesting.getSurname(), portfolioTesting.getAge(), RandomUtils.nextInt(60, 100));

        when(analyticInterview.interview(portfolioAnalyst)).thenReturn(scoringAnalyst);
        when(developmentInterview.interview(portfolioDevelopment)).thenReturn(scoringDevelopment);
        when(testingInterview.interview(portfolioTesting)).thenReturn(scoringTesting);

        boolean isSend = portfolioChannel.send(new GenericMessage<>(Arrays.asList(portfolioAnalyst, portfolioDevelopment, portfolioTesting)));
        assertTrue(isSend);
        Thread.sleep(1000);

        verify(analyticInterview).interview(portfolioAnalyst);
        verify(developmentInterview).interview(portfolioDevelopment);
        verify(testingInterview).interview(portfolioTesting);
        verify(headDepartment).decide(scoringAnalyst);
        verify(headDepartment).decide(scoringDevelopment);
        verify(headDepartment).decide(scoringTesting);
    }
}
