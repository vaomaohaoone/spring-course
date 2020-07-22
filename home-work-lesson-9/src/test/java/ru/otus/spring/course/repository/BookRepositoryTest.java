package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BookRepository.class, AuthorRepository.class, StyleRepository.class})
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private TestEntityManager em;

    private Book book;
    private Author author;
    private Style style;

    @BeforeEach
    void before() {
        book = em.persist(EntityUtils.createBook());
        author = em.persist(EntityUtils.createAuthor());
        style = em.persist(EntityUtils.createStyle());
    }

    @Test
    void findByIdTest() {
        Book actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);
    }

    @Test
    void findAllTest() {
        List<Book> books = bookRepository.findAll();
        assertEquals(book, books.get(0));
    }

    @Test
    void deleteTest() {
        Book actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);

        bookRepository.delete(book);
        actual = bookRepository.findById(book.getId());
        assertNull(actual);
    }

    @Test
    void saveWhenIdIsNullTest() {
        Book book = EntityUtils.createBook();
        book = bookRepository.save(book);

        Book actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);
    }

    @Test
    void saveWhenIdIsNotNull() {
        Book actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);

        book = book.setName("Alex");
        book = bookRepository.save(book);
        actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);
    }

    @Test
    void updateTest() {
        Book actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);

        book = book.setName("Mu-mu");
        book = bookRepository.update(book);
        actual = bookRepository.findById(book.getId());
        assertEquals(book, actual);
    }

    @Test
    void linkAuthorAndBookTest() {
        assertTrue(book.getAuthors().isEmpty());
        assertTrue(author.getBooks().isEmpty());
        bookRepository.linkAuthorAndBook(author, book);

        Book actualBook = bookRepository.findById(book.getId());
        Author actualAuthor = authorRepository.findById(author.getId());

        assertFalse(actualBook.getAuthors().isEmpty());
        assertFalse(actualAuthor.getBooks().isEmpty());
    }

    @Test
    void linkBookAndStyleTest() {
        assertTrue(book.getAuthors().isEmpty());
        assertTrue(style.getBooks().isEmpty());
        bookRepository.linkBookAndStyle(book, style);

        Book actualBook = bookRepository.findById(book.getId());
        Style actualStyle = styleRepository.findById(style.getId());

        assertFalse(actualBook.getStyles().isEmpty());
        assertFalse(actualStyle.getBooks().isEmpty());
    }
}
