package ru.otus.spring.course.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.course.domain.Decision;
import ru.otus.spring.course.domain.Portfolio;
import ru.otus.spring.course.domain.Skill;
import ru.otus.spring.course.service.EmploymentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class ShellApp {

    private static final String[] names = {"Alex", "John", "Sandra", "Paul", "Kate", "Helen", "Andrew"};
    private static final String[] surnames = {"Acheson", "Dean", "Dorsey", "Frost", "Gibson", "Polley", "Statham"};

    private final EmploymentService employmentService;

    @ShellMethod(value = "run", key = "run")
    public void run() {
        Collection<Portfolio> portfolios = generatePortfolios();
        Collection<Decision> result = employmentService.interview(portfolios);
        result.forEach(decision -> {
            log.info(String.format("Interview result for candidate: %s %s is %b", decision.getName(), decision.getSurname(), decision.isAccepted()));
        });
    }

    private Collection<Portfolio> generatePortfolios() {
        List<Portfolio> portfolios = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(1, 9); i++) {
            Portfolio portfolio = generatePortfolio();
            log.info(String.format("Generate portfolio: name - %s, surname - %s, age - %d, skill - %s", portfolio.getName(),
                    portfolio.getSurname(), portfolio.getAge(), portfolio.getSkill().getName()));
            portfolios.add(portfolio);
        }
        return portfolios;
    }

    private Portfolio generatePortfolio() {
        return new Portfolio(names[RandomUtils.nextInt(0, names.length)], surnames[RandomUtils.nextInt(0, surnames.length)],
                RandomUtils.nextInt(0, 80), Skill.values()[RandomUtils.nextInt(0, 2)]);
    }
}
