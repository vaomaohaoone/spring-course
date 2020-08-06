package ru.otus.spring.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.course.entities.Author;

import java.util.Set;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @Query("select a from author a left join a.books where a.books.size > :count")
    Set<Author> findAuthorsWhereBooksMoreThan(@Param("count") Integer count);
    Set<Author> findAuthorsByName(String name);
    Set<Author> findAuthorsBySurname(String surname);
}
