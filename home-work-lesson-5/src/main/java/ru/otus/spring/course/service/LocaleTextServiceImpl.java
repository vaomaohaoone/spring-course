package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import ru.otus.spring.course.data.Line;
import ru.otus.spring.course.data.User;

import java.util.Locale;

@AllArgsConstructor
public class LocaleTextServiceImpl implements LocaleTextService{
    private final ConsoleService consoleService;
    private final Locale locale;
    private final MessageSource messageSource;

    @Override
    public void printTotalLine() {
        String totalLine = messageSource.getMessage("message.total", null, locale);
        consoleService.write(totalLine);
    }

    @Override
    public void printProgramCompletedLine() {
        String completedLine = messageSource.getMessage("message.end", null, locale);
        consoleService.write(completedLine);
    }

    @Override
    public void printInvalidBunchFullName() {
        String invalidBunchLine = messageSource.getMessage("message.error", null, locale);
        consoleService.write(invalidBunchLine);
    }

    @Override
    public void printWelcomeLine() {
        String welcomeLine = messageSource.getMessage("message.input", null, locale);
        consoleService.write(welcomeLine);
    }

    @Override
    public void printHelloLine() {
        String helloLine = messageSource.getMessage("message.hello", null, locale);
        consoleService.write(helloLine);
    }

    @Override
    public void printUserScore(User user, Integer points) {
        String userScore = messageSource.getMessage("message.result",
                new String[]{user.getName(), user.getSurname(), points.toString()}, locale);
        consoleService.write(userScore);
    }

    @Override
    public String requestFullName() {
        return consoleService.read();
    }

    @Override
    public Integer printQuestionAndReadAnswer(Line line) {
        consoleService.write(line.getQuestion());
        for (int i = 0; i < line.getOptions().size(); i++) {
            consoleService.write(String.format("%d. %s", i + 1, line.getOptions().get(i)));
        }
        return consoleService.readInt();
    }
}
