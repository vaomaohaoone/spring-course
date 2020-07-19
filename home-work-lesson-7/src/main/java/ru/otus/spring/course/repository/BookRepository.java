package ru.otus.spring.course.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.Book;

import java.util.UUID;

@Repository
public class BookRepository extends DataRepository<Book, UUID>{
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate){
        super(jdbcTemplate, Book.SPECIFICATION);
    }
}
