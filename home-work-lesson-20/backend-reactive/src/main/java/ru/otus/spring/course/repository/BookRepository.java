package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import ru.otus.spring.course.documents.Book;

import java.util.Set;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findByPublishedYearGreaterThan(Integer year);

    @Query(value = "{$where : 'this.authors.length = :#{#count}'}")
    Flux<Book> findBooksWhereAuthorsEquals(@Param("count") Integer count);
}
