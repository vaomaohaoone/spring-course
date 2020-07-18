package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.course.JdbcTest;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.links.AuthorBook;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class AuthorBookRepositoryTest extends JdbcTest {
    @Autowired
    private AuthorBookRepository authorBookRepository;
    private AuthorBook authorBook;
    private Author author;
    private Book book;

    @BeforeEach
    void before() {
        author = entityUtils.createAndSaveAuthor();
        book = entityUtils.createAndSaveBook();
        authorBook = entityUtils.linkAuthorAndBook(author.getId(), book.getIsbn());
    }

    @Test
    void testSaveAuthorBook() {
        Author author = entityUtils.createAndSaveAuthor();
        Book book = entityUtils.createAndSaveBook();
        AuthorBook authorBook = new AuthorBook(author.getId(), book.getIsbn());
        AuthorBook actualAuthorBook = authorBookRepository.save(authorBook);
        assertEquals(authorBook, actualAuthorBook);
    }

    @Test
    void testFindByIdAuthorBook() {
        Optional<AuthorBook> authorBookOptional = authorBookRepository.findById(authorBook);
        assertTrue(authorBookOptional.isPresent());
        assertEquals(authorBook, authorBookOptional.get());
    }

    @Test
    void testDeleteByIdAuthorBook() {
        Optional<AuthorBook> authorBookOptional = authorBookRepository.findById(authorBook);
        assertTrue(authorBookOptional.isPresent());
        assertEquals(authorBook, authorBookOptional.get());

        authorBookRepository.deleteById(authorBook);
        authorBookOptional = authorBookRepository.findById(authorBook);
        assertFalse(authorBookOptional.isPresent());
    }

    @Test
    void testFindAllAuthorBook() {
        Author author = entityUtils.createAndSaveAuthor();
        Book book = entityUtils.createAndSaveBook();
        AuthorBook authorBook = new AuthorBook(author.getId(), book.getIsbn());
        authorBookRepository.save(authorBook);

        List<AuthorBook> authorBooks = authorBookRepository.findAll();

        assertEquals(2, authorBooks.size());
    }

    @Test
    void testFindAllAuthorsOfBook() {
        List<Author> authors = authorBookRepository.findAllAuthorsOfBook(book.getIsbn());
        assertEquals(author, authors.get(0));
    }

    @Test
    void testFindAllBooksForByAuthor() {
        List<Book> books = authorBookRepository.findAllBooksForByAuthor(author.getId());
        assertEquals(book, books.get(0));
    }

}
