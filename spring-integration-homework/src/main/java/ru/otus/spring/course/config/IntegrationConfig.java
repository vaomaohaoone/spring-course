package ru.otus.spring.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.course.domain.Portfolio;
import ru.otus.spring.course.domain.Scoring;
import ru.otus.spring.course.domain.Skill;
import ru.otus.spring.course.service.AnalyticInterview;
import ru.otus.spring.course.service.DevelopmentInterview;
import ru.otus.spring.course.service.HeadDepartment;
import ru.otus.spring.course.service.TestingInterview;

import static org.springframework.integration.scheduling.PollerMetadata.DEFAULT_POLLER;

@IntegrationComponentScan
@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean
    public QueueChannel portfolioChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel decisionChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public IntegrationFlow portfolioFlow() {
        return IntegrationFlows.from("portfolioChannel")
                .split()
                .route((Portfolio portfolio) -> {
                    Skill skill = portfolio.getSkill();
                    if (skill.equals(Skill.DEVELOPING))
                        return "developmentDepartmentChannel";
                    else if (skill.equals(Skill.ANALYSIS))
                        return "analysisDepartmentChannel";
                    else
                        return "testingDepartmentChannel";
                }).get();
    }

    @Bean
    public IntegrationFlow developmentDepartmentFlow(DevelopmentInterview developmentInterview) {
        return IntegrationFlows.from("developmentDepartmentChannel")
                .handle(developmentInterview, "interview")
                .convert(Scoring.class)
                .channel("headDepartmentChannel")
                .get();
    }

    @Bean
    public IntegrationFlow analyticDepartmentFlow(AnalyticInterview analyticInterview) {
        return IntegrationFlows.from("analysisDepartmentChannel")
                .handle(analyticInterview, "interview")
                .convert(Scoring.class)
                .channel("headDepartmentChannel")
                .get();
    }

    @Bean
    public IntegrationFlow testingDepartmentFlow(TestingInterview testingInterview) {
        return IntegrationFlows.from("testingDepartmentChannel")
                .handle(testingInterview, "interview")
                .convert(Scoring.class)
                .channel("headDepartmentChannel")
                .get();
    }

    @Bean
    public IntegrationFlow headDepartmentFlow(HeadDepartment headDepartment) {
        return IntegrationFlows.from("headDepartmentChannel")
                .handle(headDepartment, "decide")
                .aggregate()
                .channel("decisionChannel")
                .get();

    }

    @Bean(name = DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedDelay(100).maxMessagesPerPoll(5).get();
    }
}
