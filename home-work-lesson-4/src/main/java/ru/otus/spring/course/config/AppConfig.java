package ru.otus.spring.course.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.course.service.*;

import java.util.Locale;
import java.util.Scanner;

@Configuration
public class AppConfig {

    @Value("${app.language}")
    private Locale language;

    @Value("${app.infinite}")
    private String isInfinite;

    @Value("${csv.file}")
    private String csvFile;

    @Bean
    public ConsoleService consoleService() {
        return new ConsoleService(new Scanner(System.in), System.out);
    }

    @Bean
    public LocaleTextService localeTextService(MessageSource messageSource, ConsoleService consoleService) {
        return new LocaleTextServiceImpl(consoleService, language, messageSource);
    }

    @Bean
    public AppService appService(LocaleTextService localeTextService) {
        return new AppServiceImpl(localeTextService, Boolean.parseBoolean(isInfinite), "/" + csvFile + "_" + language + ".csv");
    }
}
