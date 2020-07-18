package ru.otus.spring.course.entities;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Set;

public interface EntitySpecification<T, ID> {
    String tableName();

    Set<String> idColumns();

    RowMapper<T> rowMapper();

    MapSqlParameterSource params(T entity);

    MapSqlParameterSource idParams(ID id);

    ID getId(T entity);
}
