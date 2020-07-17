package ru.otus.spring.course.service;

public interface AppService {
    /**
     * Метод для корректной приостановки теста, выводит результаты всех игроков по убыванию очков*/
    void addShutdownHook();

    /**
     * Метод начала теста*/
    void game();
}
