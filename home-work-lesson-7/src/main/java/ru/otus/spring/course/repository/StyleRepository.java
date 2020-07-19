package ru.otus.spring.course.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.Style;

@Repository
public class StyleRepository extends DataRepository<Style, String> {
    public StyleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Style.SPECIFICATION);
    }
}
