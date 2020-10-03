package ru.otus.spring.course.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.course.documents.BookDocument;

import java.util.Optional;
import java.util.Set;

public interface BookRepositoryMongo extends MongoRepository<BookDocument, String> {
    Set<BookDocument> findByPublishedYearGreaterThan(Integer year);

    @Query(value = "{$where : 'this.authors.length = :#{#count}'}")
    Set<BookDocument> findBooksWhereAuthorsEquals(@Param("count") Integer count);

    Optional<BookDocument> findByName(String name);

    Optional<BookDocument> findByNameAndPublishedYear(String name, Integer year);
}
