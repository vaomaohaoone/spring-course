package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    private Comment comment;
    private Book book;

    @BeforeEach
    void before() {
        book = em.persist(EntityUtils.createBook());
        comment = em.persist(EntityUtils.createComment(book));
    }

    @Test
    void findByIdTest() {
        Optional<Comment> actual = commentRepository.findById(comment.getId());
        assertTrue(actual.isPresent());
        assertEquals(comment, actual.get());
    }

    @Test
    void findAllTest() {
        List<Comment> comments = commentRepository.findAll();
        assertEquals(comment, comments.get(0));
    }

    @Test
    void deleteTest() {
        Optional<Comment> actual = commentRepository.findById(comment.getId());
        assertTrue(actual.isPresent());
        assertEquals(comment, actual.get());

        commentRepository.delete(comment);
        actual = commentRepository.findById(actual.get().getId());
        assertTrue(actual.isEmpty());
    }

    @Test
    void saveWhenIdIsNullTest() {
        Comment comment = EntityUtils.createComment(book);
        comment = commentRepository.save(comment);

        Optional<Comment> actual = commentRepository.findById(comment.getId());
        assertTrue(actual.isPresent());
        assertEquals(comment, actual.get());
    }

    @Test
    void updateTest() {
        Optional<Comment> actual = commentRepository.findById(comment.getId());
        assertTrue(actual.isPresent());
        assertEquals(comment, actual.get());

        comment = comment.setText("It is very good book!!!");
        comment = commentRepository.save(comment);
        actual = commentRepository.findById(comment.getId());
        assertTrue(actual.isPresent());
        assertEquals(comment, actual.get());
    }
}
