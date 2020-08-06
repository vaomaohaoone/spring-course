package ru.otus.spring.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.course.entities.Book;

import java.time.Year;
import java.util.Set;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Set<Book> findByPublishedYearGreaterThan(Year year);
    @Query("select b from book b left join b.authors where b.authors.size > :count")
    Set<Book> findBooksWhereAuthorsMoreThan(@Param("count") Integer count);
}
