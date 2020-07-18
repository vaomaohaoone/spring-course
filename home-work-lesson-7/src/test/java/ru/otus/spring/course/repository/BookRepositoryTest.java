package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.JdbcTest;
import ru.otus.spring.course.entities.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class BookRepositoryTest extends JdbcTest {
    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void before() {
        book = entityUtils.createAndSaveBook();
    }

    @Test
    void testSaveBookEntity() {
        Book book = EntityUtils.createBook();
        Book actual = bookRepository.save(book);
        assertEquals(book, actual);
    }

    @Test
    void testFindByIdBook() {
        Optional<Book> actualBookOptional = bookRepository.findById(book.getIsbn());
        assertTrue(actualBookOptional.isPresent());
        assertEquals(book, actualBookOptional.get());
    }

    @Test
    void testUpdateBook() {
        Optional<Book> actualBookOptional = bookRepository.findById(book.getIsbn());
        assertTrue(actualBookOptional.isPresent());
        assertEquals(book, actualBookOptional.get());

        Book updatedBook = book.withName("new book name");
        bookRepository.update(updatedBook.getIsbn(), updatedBook);

        actualBookOptional = bookRepository.findById(updatedBook.getIsbn());
        assertTrue(actualBookOptional.isPresent());
        assertEquals(updatedBook, actualBookOptional.get());
    }

    @Test
    void testDeleteByIdBook() {
        Optional<Book> actualBookOptional = bookRepository.findById(book.getIsbn());
        assertTrue(actualBookOptional.isPresent());
        assertEquals(book, actualBookOptional.get());

        bookRepository.deleteById(book.getIsbn());

        actualBookOptional = bookRepository.findById(book.getIsbn());
        assertFalse(actualBookOptional.isPresent());
    }

    @Test
    void testFindAllBook() {
        Book book = EntityUtils.createBook();
        bookRepository.save(book);
        List<Book> books = bookRepository.findAll();
        assertEquals(2, books.size());
    }
}
