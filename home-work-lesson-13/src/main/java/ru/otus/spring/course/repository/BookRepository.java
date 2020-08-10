package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.course.documents.Book;

import java.util.Set;

public interface BookRepository extends MongoRepository<Book, String> {
    Set<Book> findByPublishedYearGreaterThan(Integer year);

    @Query(value = "{$where : 'this.authors.length > :#{#count}'}")
    Set<Book> findBooksWhereAuthorsMoreThan(@Param("count") Integer count);
}
