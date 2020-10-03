package ru.otus.spring.course.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.course.documents.CommentDocument;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryMongo extends MongoRepository<CommentDocument, String> {
    List<CommentDocument> findCommentsByBook_Id(String isbn);
    Optional<CommentDocument> findByText(String text);
    void deleteCommentByBook_Id(String isbn);
}
