package ru.otus.spring.course.service;

import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface AppLibraryService {
    Book createBook(String name, Year year);

    Book createBook(String name, Year year, String style);

    Author createAuthor(String name, String surname);

    boolean linkAuthorAndBook(UUID authorId, UUID isbn);

    boolean addStyleForBook(UUID isbn, String style);

    Style createStyle(String style);

    List<Author> getAllAuthorsOfBook(UUID isbn);

    List<Book> getAllBooksByAuthor(UUID authorId);
}
