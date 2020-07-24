package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Import(CommentRepository.class)
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
        Comment actual = commentRepository.findById(comment.getId());
        assertEquals(comment, actual);
    }

    @Test
    void findAllTest() {
        List<Comment> comments = commentRepository.findAll();
        assertEquals(comment, comments.get(0));
    }

    @Test
    void deleteTest() {
        Comment actual = commentRepository.findById(comment.getId());
        assertEquals(comment, actual);

        commentRepository.delete(comment);
        actual = commentRepository.findById(actual.getId());
        assertNull(actual);
    }

    @Test
    void saveWhenIdIsNullTest() {
        Comment comment = EntityUtils.createComment(book);
        comment = commentRepository.save(comment);

        Comment actual = commentRepository.findById(comment.getId());
        assertEquals(comment, actual);
    }

    @Test
    void updateTest() {
        Comment actual = commentRepository.findById(comment.getId());
        assertEquals(comment, actual);

        comment = comment.setText("It is very good book!!!");
        comment = commentRepository.update(comment);
        actual = commentRepository.findById(comment.getId());
        assertEquals(comment, actual);
    }
}
