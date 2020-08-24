package ru.otus.spring.course.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Comment;

public interface CommentService {
    Mono<Comment> saveComment(Comment comment);
    Mono<Comment> getCommentById(String commentId);
    void deleteById(String commentId);
    Flux<Comment> getAll();
    Mono<Comment> addCommentToBook(String isbn, String text);
    Flux<Comment> getCommentsByIsbn(String isbn);
}
