package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.course.documents.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Flux<Comment> findCommentsByBook_Id(String isbn);
    void deleteCommentByBook_Id(String isbn);
}
