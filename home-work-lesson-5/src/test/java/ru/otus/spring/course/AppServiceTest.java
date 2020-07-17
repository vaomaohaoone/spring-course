package ru.otus.spring.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.spring.course.data.Line;
import ru.otus.spring.course.data.User;
import ru.otus.spring.course.service.AppService;
import ru.otus.spring.course.service.LocaleTextService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class AppServiceTest {

    @MockBean
    private LocaleTextService localeTextService;

    @Autowired
    private AppService appService;

    @Test
    public void testGameWhenRequestedNameIsValid() {
        doNothing().when(localeTextService).printHelloLine();
        doNothing().when(localeTextService).printWelcomeLine();
        String requestedFullName = "Боб Малкович";
        when(localeTextService.requestFullName()).thenReturn(requestedFullName);
        when(localeTextService.printQuestionAndReadAnswer(any(Line.class))).thenReturn(3);
        doNothing().when(localeTextService).printUserScore(any(User.class), any());

        appService.game();

        verify(localeTextService).printHelloLine();
        verify(localeTextService).printWelcomeLine();
        verify(localeTextService).requestFullName();
        verify(localeTextService, times(5)).printQuestionAndReadAnswer(any(Line.class));
    }

    @Test
    public void testGameWhenRequestedNameIsInvalid() {
        doNothing().when(localeTextService).printHelloLine();
        doNothing().when(localeTextService).printWelcomeLine();
        String requestedFullName = "БобМалкович";
        when(localeTextService.requestFullName()).thenReturn(requestedFullName);
        doNothing().when(localeTextService).printInvalidBunchFullName();

        appService.game();

        verify(localeTextService).printHelloLine();
        verify(localeTextService).printWelcomeLine();
        verify(localeTextService).requestFullName();
        verify(localeTextService).printInvalidBunchFullName();
    }
}
