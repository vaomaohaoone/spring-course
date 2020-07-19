package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.JdbcTest;
import ru.otus.spring.course.entities.Author;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorRepositoryTest extends JdbcTest {
    @Autowired
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    void before() {
        author = entityUtils.createAndSaveAuthor();
    }

    @Test
    void testSaveAuthorEntity() {
        Author author = EntityUtils.createAuthor();
        Author actual = authorRepository.save(author);
        assertEquals(author, actual);
    }

    @Test
    void testFindByIdAuthor() {
        Optional<Author> actualAuthorOptional = authorRepository.findById(author.getId());
        assertTrue(actualAuthorOptional.isPresent());
        assertEquals(author, actualAuthorOptional.get());
    }

    @Test
    void testUpdateAuthor() {
        Optional<Author> actualAuthorOptional = authorRepository.findById(author.getId());
        assertTrue(actualAuthorOptional.isPresent());
        assertEquals(author, actualAuthorOptional.get());

        Author updatedAuthor = author.withName("Виктор").withSurname("Пелевин");
        authorRepository.update(updatedAuthor.getId(), updatedAuthor);

        actualAuthorOptional = authorRepository.findById(updatedAuthor.getId());
        assertTrue(actualAuthorOptional.isPresent());
        assertEquals(updatedAuthor, actualAuthorOptional.get());
    }

    @Test
    void testDeleteByIdAuthor() {
        Optional<Author> actualAuthorOptional = authorRepository.findById(author.getId());
        assertTrue(actualAuthorOptional.isPresent());
        assertEquals(author, actualAuthorOptional.get());

        authorRepository.deleteById(author.getId());

        actualAuthorOptional = authorRepository.findById(author.getId());
        assertFalse(actualAuthorOptional.isPresent());
    }

    @Test
    void testFindAllAuthor() {
        Author author = EntityUtils.createAuthor();
        authorRepository.save(author);
        List<Author> authors = authorRepository.findAll();
        assertEquals(2, authors.size());
    }
}
