package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findCommentsByBook_Id(String isbn);
    Mono<Void> deleteCommentByBook_Id(String isbn);
}
