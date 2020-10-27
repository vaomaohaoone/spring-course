package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.course.documents.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findCommentsByBook_Id(String isbn);
    Optional<Comment> findByText(String text);
    void deleteCommentByBook_Id(String isbn);
}
