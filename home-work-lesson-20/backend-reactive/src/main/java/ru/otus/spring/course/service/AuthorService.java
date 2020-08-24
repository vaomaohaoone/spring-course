package ru.otus.spring.course.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    Mono<Author> saveAuthor(Author author);
    Mono<Author> getAuthorById(String authorId);
    void deleteById(String authorId);
    Flux<Author> getAll();
    Mono<Author> createAuthor(String name, String surname);
    Flux<Book> getAllBooksByAuthor(String authorId);
    Flux<Author> getAuthorsWhereBooksEquals(int count);
    Flux<Author> getAuthorsByName(String name);
    Flux<Author> getAuthorsBySurname(String surname);
}
