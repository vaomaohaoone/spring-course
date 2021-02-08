package ru.otus.spring.course.service;

import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Book saveBook(Book book);
    Optional<Book> getBookById(String isbn);
    void deleteById(String isbn);
    List<Book> getAll();
    Book createBook(String name, Year year);
    Book createBook(String name, Year year, String style);
    boolean linkAuthorAndBook(String authorId, String isbn);
    boolean unlinkAuthorAndBook(String authorId, String isbn);
    boolean addStyleForBook(String isbn, String styleName);
    boolean removeStyleForBook(String isbn, String styleName);
    Set<Author> getAllAuthorsOfBook(String isbn);
    Set<Book> getBooksWhereAuthorsEquals(int count);
    Set<Book> getBooksByPublishedYearGreaterThan(Year year);
}
