package ru.otus.spring.course.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.links.BookStyle;

@Repository
public class BookStyleRepository extends DataRepository<BookStyle, BookStyle> {
    public BookStyleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, BookStyle.SPECIFICATION);
    }
}
