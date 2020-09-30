package ru.otus.spring.course.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.course.documents.AuthorDocument;

import java.util.Optional;
import java.util.Set;

public interface AuthorRepositoryMongo extends MongoRepository<AuthorDocument, String> {
    @Query(value = "{$where : 'this.books.length = :#{#count}'}")
    Set<AuthorDocument> findAuthorsWhereBooksEquals(@Param("count") Integer count);

    Set<AuthorDocument> findAuthorsByName(String name);

    Set<AuthorDocument> findAuthorsBySurname(String surname);

    Optional<AuthorDocument> findByNameAndSurname(String name, String surname);
}
