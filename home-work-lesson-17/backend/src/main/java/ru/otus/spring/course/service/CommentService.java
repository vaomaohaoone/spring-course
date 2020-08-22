package ru.otus.spring.course.service;

import ru.otus.spring.course.documents.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment saveComment(Comment comment);
    Optional<Comment> getCommentById(String commentId);
    void deleteById(String commentId);
    List<Comment> getAll();
    Comment addCommentToBook(String isbn, String text);
    List<Comment> getCommentsByIsbn(String isbn);
}
