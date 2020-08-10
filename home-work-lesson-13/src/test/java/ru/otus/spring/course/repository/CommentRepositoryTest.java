package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.documents.converter.YearConverter;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Import(YearConverter.class)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private Comment comment;
    private Book book;

    @BeforeEach
    void before() {
        bookRepository.deleteAll();
        commentRepository.deleteAll();
        book = bookRepository.save(EntityUtils.createBook());
        comment = commentRepository.save(EntityUtils.createComment(book));
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
