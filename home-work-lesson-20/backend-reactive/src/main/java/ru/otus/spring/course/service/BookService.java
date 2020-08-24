package ru.otus.spring.course.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;

import java.time.Year;

public interface BookService {
    Mono<Book> saveBook(Book book);
    Mono<Book> getBookById(String isbn);
    void deleteById(String isbn);
    Flux<Book> getAll();
    Mono<Book> createBook(String name, Year year);
    Mono<Book> createBook(String name, Year year, String style);
    Mono<Boolean> linkAuthorAndBook(String authorId, String isbn);
    Mono<Boolean> unlinkAuthorAndBook(String authorId, String isbn);
    Mono<Boolean> addStyleForBook(String isbn, String styleName);
    Mono<Boolean> removeStyleForBook(String isbn, String styleName);
    Flux<Author> getAllAuthorsOfBook(String isbn);
    Flux<Book> getBooksWhereAuthorsEquals(int count);
    Flux<Book> getBooksByPublishedYearGreaterThan(Year year);
}
