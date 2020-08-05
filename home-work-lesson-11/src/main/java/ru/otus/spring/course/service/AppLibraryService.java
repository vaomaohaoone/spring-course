package ru.otus.spring.course.service;

import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;
import ru.otus.spring.course.entities.Style;

import java.time.Year;
import java.util.Set;
import java.util.UUID;

public interface AppLibraryService {
    /**
     * Простой метод создания книги
     * @param name название книги
     * @param year год написания
     * */
    Book createBook(String name, Year year);

    /**
     * Метод создания книги с привязкой к жанру
     * @param name название книги
     * @param year год написания
     * @param style жанр
     * */
    Book createBook(String name, Year year, String style);

    /**
     * Метод создания автора
     * @param name имя
     * @param surname фамилия
     * */
    Author createAuthor(String name, String surname);

    /**
     * Метод привязки книги и автора
     * @param authorId id автора
     * @param isbn id книги
     * */
    boolean linkAuthorAndBook(UUID authorId, UUID isbn);

    /**
     * Метод привязки жанра к книге
     * @param isbn id книги
     * @param style жанр
     * */
    boolean addStyleForBook(UUID isbn, String style);

    /**
     * Метод создания жанра
     * @param style имя жанра
     * */
    Style createStyle(String style);

    /**
     * Метод выдачи множества авторов данной книги
     * @param isbn id книги
     * */
    Set<Author> getAllAuthorsOfBook(UUID isbn);

    /**
     * Метод выдачи множества книг написанных данным автором
     * @param authorId id автора
     * */
    Set<Book> getAllBooksByAuthor(UUID authorId);

    /**
     * Метод добавления комментария к книге
     * */
    Comment addCommentToBook(UUID isbn, String text);

    /**
     * Метод выдачи книг с датой публикации больше, чем @param year
     * */
    Set<Book> getBooksByPublishedYearGreaterThan(Year year);

    /**
     * Метод выдачи книг, где количество авторов больше, чем @param count
     * */
    Set<Book> getBooksWhereAuthorsMoreThan(int count);

    /**
     * Метод выдачи авторов, где количество книг больше, чем @param count
     * */
    Set<Author> getAuthorsWhereBooksMoreThan(int count);

    /**
     * Метод выдачи авторов по имени
     * */
    Set<Author> getAuthorsByName(String name);

    /**
     * Метож выдачи авторов по фамилии
     * */
    Set<Author> getAuthorsBySurname(String surname);
}
