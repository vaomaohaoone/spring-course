package ru.otus.spring.course.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.Author;

import java.util.UUID;

@Repository
public class AuthorRepository extends DataRepository<Author, UUID> {
    public AuthorRepository(NamedParameterJdbcTemplate jdbcTemplate){
        super(jdbcTemplate, Author.SPECIFICATION);
    }
}
