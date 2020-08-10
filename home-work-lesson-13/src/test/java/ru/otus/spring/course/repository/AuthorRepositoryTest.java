package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.converter.YearConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Import(YearConverter.class)
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    private Author author;

    @BeforeEach
    void before() {
        authorRepository.deleteAll();
        bookRepository.deleteAll();
        author = authorRepository.save(EntityUtils.createAuthor());
    }

    @Test
    void findByIdTest() {
        Optional<Author> actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());
    }

    @Test
    void findAllTest() {
        List<Author> authors = authorRepository.findAll();
        assertEquals(author, authors.get(0));
    }

    @Test
    void deleteTest() {
        Optional<Author> actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());

        authorRepository.delete(author);
        actual = authorRepository.findById(actual.get().getId());
        assertTrue(actual.isEmpty());
    }

    @Test
    void saveWhenIdIsNullTest() {
        Author author = EntityUtils.createAuthor();
        author = authorRepository.save(author);

        Optional<Author> actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());
    }

    @Test
    void saveWhenIdIsNotNull() {
        Optional<Author> actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());

        author = author.setName("Alex").setSurname("Pushkin");
        author = authorRepository.save(author);
        actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());
    }

    @Test
    void updateTest() {
        Optional<Author> actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());

        author = author.setName("Alex").setSurname("Pushkin");
        author = authorRepository.save(author);
        actual = authorRepository.findById(author.getId());
        assertTrue(actual.isPresent());
        assertEquals(author, actual.get());
    }

    @Test
    void findAuthorsWhereBooksMoreThanZeroShouldReturnEmptyList() {
        Set<Author> authors = authorRepository.findAuthorsWhereBooksMoreThan(0);
        assertTrue(authors.isEmpty());
    }

    @Test
    void findAuthorsWhereBooksMoreThanZeroShouldReturnNonEmptyList() {
        Book book = EntityUtils.createBook();
        bookRepository.save(book);
        author.getBooks().add(book);
        book.getAuthors().add(author);
        authorRepository.save(author);

        Set<Author> authors = authorRepository.findAuthorsWhereBooksMoreThan(0);
        assertFalse(authors.isEmpty());
    }

    @Test
    void findAuthorsByName() {
        Set<Author> authors = authorRepository.findAuthorsByName(author.getName());
        assertEquals(1, authors.size());
        assertEquals(author.getName(), new ArrayList<>(authors).get(0).getName());
    }

    @Test
    void findAuthorsBySurname() {
        Set<Author> authors = authorRepository.findAuthorsBySurname(author.getSurname());
        assertEquals(1, authors.size());
        assertEquals(author.getSurname(), new ArrayList<>(authors).get(0).getSurname());
    }


}
