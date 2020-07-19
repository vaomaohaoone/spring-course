package ru.otus.spring.course.entities;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Optional;
import java.util.UUID;

@Value
@AllArgsConstructor
public class Author {
    UUID id;
    @With
    String name;
    @With
    String surname;

    public Author(String name, String surname) {
        this(UUID.randomUUID(), name, surname);
    }

    public static final EntitySpecification<Author, UUID> SPECIFICATION = new AuthorSpecification();

    private static class AuthorSpecification implements SingleIdSpecification<Author, UUID> {
        private static final RowMapper<Author> ROW_MAPPER = (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString(Fields.ID));
            String name = resultSet.getString(Fields.NAME);
            String surname = resultSet.getString(Fields.SURNAME);
            return new Author(id, name, surname);
        };

        @Override
        public String tableName() {
            return "author";
        }

        @Override
        public String idColumn() {
            return Fields.ID;
        }

        @Override
        public RowMapper<Author> rowMapper() {
            return ROW_MAPPER;
        }

        @Override
        public MapSqlParameterSource params(Author entity) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            Optional.ofNullable(entity.getId()).ifPresent(id -> params.addValue(Fields.ID, id));
            Optional.ofNullable(entity.getName()).ifPresent(name -> params.addValue(Fields.NAME, name));
            Optional.ofNullable(entity.getSurname()).ifPresent(surname -> params.addValue(Fields.SURNAME, surname));
            return params;
        }

        @Override
        public UUID getId(Author entity) {
            return entity.getId();
        }
    }

    @UtilityClass
    public static class Fields {
        public final String ID = "id";
        public final String NAME = "name";
        public final String SURNAME = "surname";
    }
}
