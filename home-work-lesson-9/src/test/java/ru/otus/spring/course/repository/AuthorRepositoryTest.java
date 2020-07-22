package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Import(AuthorRepository.class)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    private Author author;

    @BeforeEach
    void before() {
        author = em.persist(EntityUtils.createAuthor());
    }

    @Test
    void findByIdTest() {
        Author actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);
    }

    @Test
    void findAllTest() {
        List<Author> authors = authorRepository.findAll();
        assertEquals(author, authors.get(0));
    }

    @Test
    void deleteTest() {
        Author actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);

        authorRepository.delete(author);
        actual = authorRepository.findById(actual.getId());
        assertNull(actual);
    }

    @Test
    void saveWhenIdIsNullTest() {
        Author author = EntityUtils.createAuthor();
        author = authorRepository.save(author);

        Author actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);
    }

    @Test
    void saveWhenIdIsNotNull() {
        Author actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);

        author = author.setName("Alex").setSurname("Pushkin");
        author = authorRepository.save(author);
        actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);
    }

    @Test
    void updateTest() {
        Author actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);

        author = author.setName("Alex").setSurname("Pushkin");
        author = authorRepository.update(author);
        actual = authorRepository.findById(author.getId());
        assertEquals(author, actual);
    }
}
