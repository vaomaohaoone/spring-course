package ru.otus.spring.course.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.otus.spring.course.entities.EntitySpecification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class DataRepository<T, ID> {
    protected final NamedParameterJdbcTemplate jdbcTemplate;
    protected final EntitySpecification<T, ID> specification;

    public T save(T entity) {
        SqlParameterSource params = specification.params(entity);
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName(specification.tableName())
                .usingColumns(params.getParameterNames())
                .execute(params);
        return entity;
    }

    public void update(ID id, T entity) {
        MapSqlParameterSource params = specification.params(entity);
        String setParamsSql = concat(params, ",", param -> !specification.idColumns().contains(param));

        MapSqlParameterSource idParams = specification.idParams(id);
        String wherePlaceholder = concat(idParams, " AND ");

        String query = String.format("UPDATE %s SET %s WHERE %s", specification.tableName(), setParamsSql, wherePlaceholder);

        params.addValues(idParams.getValues());
        jdbcTemplate.update(query, params);
    }

    public Optional<T> findById(ID id) {
        MapSqlParameterSource idParams = specification.idParams(id);
        String wherePlaceholder = concat(idParams, " AND ");
        String query = String.format("SELECT * FROM %s WHERE %s", specification.tableName(), wherePlaceholder);
        List<T> result = jdbcTemplate.query(query, idParams, specification.rowMapper());
        return Optional.ofNullable(DataAccessUtils.singleResult(result));
    }

    public void deleteById(ID id) {
        MapSqlParameterSource idParams = specification.idParams(id);
        String wherePlaceholder = concat(idParams, " AND ");
        String query = String.format("DELETE FROM %s WHERE %s", specification.tableName(), wherePlaceholder);
        jdbcTemplate.update(query, idParams);
    }

    public List<T> findAll() {
        return jdbcTemplate.query(
                String.format("SELECT * FROM %s", specification.tableName()),
                specification.rowMapper()
        );
    }

    private String concat(MapSqlParameterSource idParams, String operator) {
        return concat(idParams, operator, param -> true);
    }

    private String concat(MapSqlParameterSource idParams, String operator, Predicate<String> filter) {
        return Arrays.stream(idParams.getParameterNames())
                .filter(filter)
                .map(param -> String.format("%1$s=:%1$s", param))
                .collect(Collectors.joining(operator));
    }
}
