package ru.otus.spring.course.service;

import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    Author saveAuthor(Author author);
    Optional<Author> getAuthorById(String authorId);
    void deleteById(String authorId);
    List<Author> getAll();
    Author createAuthor(String name, String surname);
    Set<Book> getAllBooksByAuthor(String authorId);
    Set<Author> getAuthorsWhereBooksEquals(int count);
    Set<Author> getAuthorsByName(String name);
    Set<Author> getAuthorsBySurname(String surname);
}
