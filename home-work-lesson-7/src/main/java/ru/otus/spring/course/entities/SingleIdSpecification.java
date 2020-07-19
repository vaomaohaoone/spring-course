package ru.otus.spring.course.entities;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Collections;
import java.util.Set;

public interface SingleIdSpecification<T, ID> extends EntitySpecification<T, ID> {

    String idColumn();

    @Override
    default Set<String> idColumns() {
        return Collections.singleton(idColumn());
    }

    @Override
    default MapSqlParameterSource idParams(ID id) {
        MapSqlParameterSource idParams = new MapSqlParameterSource();
        idParams.addValue(idColumn(), id);
        return idParams;
    }
}
