package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import ru.otus.spring.course.documents.Author;

import java.util.Set;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    @Query(value = "{$where : 'this.books.length = :#{#count}'}")
    Flux<Author> findAuthorsWhereBooksEquals(@Param("count") Integer count);

    Flux<Author> findAuthorsByName(String name);

    Flux<Author> findAuthorsBySurname(String surname);
}
