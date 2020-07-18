package ru.otus.spring.course.entities;

import lombok.Value;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Optional;

@Value
public class Style {
    String name;

    public static final EntitySpecification<Style, String> SPECIFICATION = new StyleSpecification();

    private static class StyleSpecification implements SingleIdSpecification<Style, String>{

        @Override
        public String idColumn() {
            return Fields.NAME;
        }

        @Override
        public String tableName() {
            return "style";
        }

        @Override
        public RowMapper<Style> rowMapper() {
            return (resultSet, i) -> new Style(resultSet.getString(Fields.NAME));
        }

        @Override
        public MapSqlParameterSource params(Style entity) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            Optional.ofNullable(entity.getName()).ifPresent(name -> params.addValue(Fields.NAME, name));
            return params;
        }

        @Override
        public String getId(Style entity) {
            return entity.getName();
        }
    }

    @UtilityClass
    public static class Fields {
        public final String NAME = "name";
    }
}
