package ru.otus.spring.course.entities;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Value
@AllArgsConstructor
public class Book {
    UUID isbn;
    @With
    String name;
    Year publishedYear;

    public static final EntitySpecification<Book, UUID> SPECIFICATION = new BookSpecification();

    public Book(String name, Year published) {
        this(UUID.randomUUID(), name, published);
    }

    private static class BookSpecification implements SingleIdSpecification<Book, UUID> {
        private static final RowMapper<Book> ROW_MAPPER = (resultSet, i) -> {
            UUID isbn = UUID.fromString(resultSet.getString(Fields.ISBN));
            String name = resultSet.getString(Fields.NAME);
            Year publishedYear = Year.of(resultSet.getInt(Fields.PUBLISHED_YEAR));
            return new Book(isbn, name, publishedYear);
        };

        @Override
        public String tableName() {
            return "book";
        }

        @Override
        public String idColumn() {
            return Fields.ISBN;
        }

        @Override
        public RowMapper<Book> rowMapper() {
            return ROW_MAPPER;
        }

        @Override
        public MapSqlParameterSource params(Book entity) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            Optional.ofNullable(entity.getIsbn()).ifPresent(isbn -> params.addValue(Fields.ISBN, isbn));
            Optional.ofNullable(entity.getName()).ifPresent(name -> params.addValue(Fields.NAME, name));
            Optional.ofNullable(entity.getPublishedYear()).ifPresent(publishedYear -> params.addValue(Fields.PUBLISHED_YEAR, publishedYear.toString()));
            return params;
        }

        @Override
        public UUID getId(Book entity) {
            return entity.getIsbn();
        }
    }

    @UtilityClass
    public static class Fields {
        public final String ISBN = "isbn";
        public final String NAME = "name";
        public final String PUBLISHED_YEAR = "published_year";
    }
}
