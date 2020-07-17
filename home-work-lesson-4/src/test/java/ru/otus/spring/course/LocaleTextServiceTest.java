package ru.otus.spring.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import ru.otus.spring.course.data.Line;
import ru.otus.spring.course.data.User;
import ru.otus.spring.course.service.ConsoleService;
import ru.otus.spring.course.service.LocaleTextService;

import java.util.ArrayList;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LocaleTextServiceTest {
    private final Locale locale = Locale.forLanguageTag("ru");
    @MockBean
    private ConsoleService consoleService;
    @MockBean
    private MessageSource messageSource;
    @Autowired
    private LocaleTextService localeTextService;

    @Test
    public void testPrintTotalLine() {
        String totalLine = "Итог:";
        when(messageSource.getMessage("message.total", null, locale)).thenReturn(totalLine);
        doNothing().when(consoleService).write(totalLine);

        localeTextService.printTotalLine();

        verify(messageSource).getMessage("message.total", null, locale);
        verify(consoleService).write(totalLine);
    }

    @Test
    public void testPrintProgramCompletedLine() {
        String completedLine = "Программа завершена";
        when(messageSource.getMessage("message.end", null, locale)).thenReturn(completedLine);
        doNothing().when(consoleService).write(completedLine);

        localeTextService.printProgramCompletedLine();

        verify(messageSource).getMessage("message.end", null, locale);
        verify(consoleService).write(completedLine);
    }

    @Test
    public void testPrintInvalidBunchFullName() {
        String invalidBunchFullnameText = "Неверная связка имени и фамилии !!!";
        when(messageSource.getMessage("message.error", null, locale)).thenReturn(invalidBunchFullnameText);
        doNothing().when(consoleService).write(invalidBunchFullnameText);

        localeTextService.printInvalidBunchFullName();

        verify(messageSource).getMessage("message.error", null, locale);
        verify(consoleService).write(invalidBunchFullnameText);
    }

    @Test
    public void testPrintWelcomeLine() {
        String welcomeLineText = "Пожалуйста, введите ваше имя и фамилию в следующей строку:";
        when(messageSource.getMessage("message.input", null, locale)).thenReturn(welcomeLineText);
        doNothing().when(consoleService).write(welcomeLineText);

        localeTextService.printWelcomeLine();

        verify(messageSource).getMessage("message.input", null, locale);
        verify(consoleService).write(welcomeLineText);
    }

    @Test
    public void testPrintHelloLine() {
        String helloLineText = "Привет!";
        when(messageSource.getMessage("message.hello", null, locale)).thenReturn(helloLineText);
        doNothing().when(consoleService).write(helloLineText);

        localeTextService.printHelloLine();

        verify(messageSource).getMessage("message.hello", null, locale);
        verify(consoleService).write(helloLineText);
    }

    @Test
    public void testPrintUserScore() {
        User user = new User("Боб", "Малкович");
        Integer points = 5;
        String resultLineText = "Пользователь Боб Малкович набрал: 5";
        when(messageSource.getMessage(eq("message.result"), any(), eq(locale))).thenReturn(resultLineText);
        doNothing().when(consoleService).write(resultLineText);

        localeTextService.printUserScore(user, points);

        verify(messageSource).getMessage(eq("message.result"), any(), eq(locale));
        verify(consoleService).write(resultLineText);
    }

    @Test
    public void testRequestFullName() {
        String requestedFullName = "Боб Малкович";
        when(consoleService.read()).thenReturn(requestedFullName);

        String result = localeTextService.requestFullName();

        verify(consoleService).read();
        assertEquals(requestedFullName, result);
    }

    @Test
    public void testPrintQuestionEndReadAnswer() {
        Line line = Line.builder()
                .question("В каком году был создан java?")
                .options(new ArrayList<>(){{add("1978"); add("1999"); add("1995"); add("2003");}})
                .answer(2).build();
        doNothing().when(consoleService).write(anyString());
        when(consoleService.readInt()).thenReturn(2);

        int result = localeTextService.printQuestionAndReadAnswer(line);

        verify(consoleService, times(5)).write(anyString());
        verify(consoleService).readInt();
        assertEquals(2, result);
    }
}
