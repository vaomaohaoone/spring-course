package ru.otus.spring.course.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.course.domain.Decision;
import ru.otus.spring.course.domain.Portfolio;

import java.util.Collection;

@MessagingGateway
public interface EmploymentService {
    @Gateway(requestChannel = "portfolioChannel", replyChannel = "decisionChannel")
    Collection<Decision> interview(Collection<Portfolio> portfolios);
}
