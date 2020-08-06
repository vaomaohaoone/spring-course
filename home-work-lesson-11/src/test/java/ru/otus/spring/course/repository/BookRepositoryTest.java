package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private Book book;

    @BeforeEach
    void before() {
        book = em.persist(EntityUtils.createBook());
    }

    @Test
    void findByIdTest() {
        Optional<Book> actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());
    }

    @Test
    void findAllTest() {
        List<Book> books = bookRepository.findAll();
        assertEquals(book, books.get(0));
    }

    @Test
    void deleteTest() {
        Optional<Book> actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());

        bookRepository.delete(book);
        actual = bookRepository.findById(actual.get().getId());
        assertTrue(actual.isEmpty());
    }

    @Test
    void saveWhenIdIsNullTest() {
        Book book = EntityUtils.createBook();
        book = bookRepository.save(book);

        Optional<Book> actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());
    }

    @Test
    void saveWhenIdIsNotNull() {
        Optional<Book> actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());

        book = book.setName("Azbuka").setPublishedYear(Year.of(1902));
        book = bookRepository.save(book);
        actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());
    }

    @Test
    void updateTest() {
        Optional<Book> actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());

        book = book.setName("Azbuka").setPublishedYear(Year.of(1902));
        book = bookRepository.save(book);
        actual = bookRepository.findById(book.getId());
        assertTrue(actual.isPresent());
        assertEquals(book, actual.get());
    }

    @Test
    void findBooksWhereAuthorsMoreThanZeroShouldReturnEmptyList() {
        Set<Book> books = bookRepository.findBooksWhereAuthorsMoreThan(0);
        assertTrue(books.isEmpty());
    }

    @Test
    void findBooksWhereBooksMoreThanZeroShouldReturnNonEmptyList() {
        Author author = EntityUtils.createAuthor();
        em.persist(author);
        book.getAuthors().add(author);
        author.getBooks().add(book);
        bookRepository.save(book);

        Set<Book> books = bookRepository.findBooksWhereAuthorsMoreThan(0);
        assertFalse(books.isEmpty());
    }

    @Test
    void findByPublishedYearGreaterThanShouldReturnEmptyList() {
        Set<Book> books = bookRepository.findByPublishedYearGreaterThan(book.getPublishedYear());
        assertTrue(books.isEmpty());
    }

    @Test
    void findByPublishedYearGreaterThanShouldReturnNonEmptyList() {
        Set<Book> books = bookRepository.findByPublishedYearGreaterThan(book.getPublishedYear().minusYears(2));
        assertFalse(books.isEmpty());
    }
}
