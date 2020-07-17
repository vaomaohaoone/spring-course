package ru.otus.spring.course;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.course.service.AppService;

@ShellComponent
@RequiredArgsConstructor
public class AppShell {
    private final AppService appService;

    @ShellMethod(value = "start app with shell", key = "start")
    public void startAppWithShell(){
        appService.game();
    }
}
