package ru.otus.spring.course.service;

import ru.otus.spring.course.data.Line;
import ru.otus.spring.course.data.User;

/**
 * Сервис локализованного ввода/вывода текста
 * */
public interface LocaleTextService {

    /**
     * Метод выводящий сообщение "Итог: или Total:"*/
    void printTotalLine();

    /**
     * Метод выводящий сообщение "Программа завершена или Program completed"*/
    void printProgramCompletedLine();

    /**
     * Метод выводящий сообщение об ошибке в вводе имени и фамилии*/
    void printInvalidBunchFullName();

    /**
     * Метод выводящий текст предложения ввода имени и фамилии*/
    void printWelcomeLine();

    /**
     * Метод выводящий приветственное предложение*/
    void printHelloLine();

    /**
     * Метод, выводящий количество очков пользователя*/
    void printUserScore(User user, Integer points);

    /**
     * Метод запроса ввода имени и фамилии*/
    String requestFullName();

    /**
     * Метод вывода вопроса и вариантов ответа, а так же запроса ответа пользователя*/
    Integer printQuestionAndReadAnswer(Line line);
}

