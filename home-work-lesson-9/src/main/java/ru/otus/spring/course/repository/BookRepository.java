package ru.otus.spring.course.repository;

import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;

import java.util.UUID;

public interface BookRepository extends EntityRepository<Book, UUID>{
    void linkAuthorAndBook(Author author, Book book);
    void linkBookAndStyle(Book book, Style style);
}
