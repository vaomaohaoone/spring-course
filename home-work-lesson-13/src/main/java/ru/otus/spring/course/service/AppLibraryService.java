package ru.otus.spring.course.service;

import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.documents.Style;

import java.time.Year;
import java.util.List;
import java.util.Set;

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
    boolean linkAuthorAndBook(String authorId, String isbn);

    /**
     * Метод привязки жанра к книге
     * @param isbn id книги
     * @param style жанр
     * */
    boolean addStyleForBook(String isbn, String style);

    /**
     * Метод создания жанра
     * @param style имя жанра
     * */
    Style createStyle(String style);

    /**
     * Метод выдачи множества авторов данной книги
     * @param isbn id книги
     * */
    Set<Author> getAllAuthorsOfBook(String isbn);

    /**
     * Метод выдачи множества книг написанных данным автором
     * @param authorId id автора
     * */
    Set<Book> getAllBooksByAuthor(String authorId);

    /**
     * Метод добавления комментария к книге
     * */
    Comment addCommentToBook(String isbn, String text);

    /**
     * Метод выдачи книг, где количество авторов больше, чем @param count
     * */
    Set<Book> getBooksWhereAuthorsMoreThan(int count);

    /**
     * Метод выдачи авторов, где количество книг больше, чем @param count
     * */
    Set<Author> getAuthorsWhereBooksMoreThan(int count);

    /**
     * Метод выдачи книг с датой публикации больше, чем @param year
     * */
    Set<Book> getBooksByPublishedYearGreaterThan(Year year);

    /**
     * Метод выдачи авторов по имени
     * */
    Set<Author> getAuthorsByName(String name);

    /**
     * Метож выдачи авторов по фамилии
     * */
    Set<Author> getAuthorsBySurname(String surname);
}
