package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.course.documents.Author;

import java.util.Set;

public interface AuthorRepository extends MongoRepository<Author, String> {
    @Query(value = "{$where : 'this.books.length = :#{#count}'}")
    Set<Author> findAuthorsWhereBooksEquals(@Param("count") Integer count);

    Set<Author> findAuthorsByName(String name);

    Set<Author> findAuthorsBySurname(String surname);
}
