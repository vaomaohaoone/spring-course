package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.course.documents.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
