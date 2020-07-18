package ru.otus.spring.course.entities.links;

import lombok.Value;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.core.internal.Function;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.otus.spring.course.entities.EntitySpecification;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Value
public class AuthorBook {

    UUID authorId;
    UUID bookIsbn;

    public static final EntitySpecification<AuthorBook, AuthorBook> SPECIFICATION = new AuthorBookSpecification();

    private static class AuthorBookSpecification implements EntitySpecification<AuthorBook, AuthorBook> {
        private static final RowMapper<AuthorBook> ROW_MAPPER = (resultSet, i) -> {
            UUID authorId = UUID.fromString(resultSet.getString(Fields.AUTHOR_ID));
            UUID bookIsbn = UUID.fromString(resultSet.getString(Fields.BOOK_ISBN));
            return new AuthorBook(authorId, bookIsbn);
        };

        private static final Function<AuthorBook, MapSqlParameterSource> PARAMS_MAPPER = authorBook -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            Optional.ofNullable(authorBook.getAuthorId()).ifPresent(authorId -> params.addValue(Fields.AUTHOR_ID, authorId));
            Optional.ofNullable(authorBook.getBookIsbn()).ifPresent(bookIsbn -> params.addValue(Fields.BOOK_ISBN, bookIsbn));
            return params;
        };

        @Override
        public String tableName() {
            return "author_book";
        }

        @Override
        public Set<String> idColumns() {
            return Set.of(Fields.AUTHOR_ID, Fields.BOOK_ISBN);
        }

        @Override
        public RowMapper<AuthorBook> rowMapper() {
            return ROW_MAPPER;
        }

        @Override
        public MapSqlParameterSource params(AuthorBook authorBook) {
            return PARAMS_MAPPER.apply(authorBook);
        }

        @Override
        public MapSqlParameterSource idParams(AuthorBook authorBook) {
            return PARAMS_MAPPER.apply(authorBook);
        }

        @Override
        public AuthorBook getId(AuthorBook authorBook) {
            return authorBook;
        }
    }

    @UtilityClass
    public static class Fields {
        public final String AUTHOR_ID = "author_id";
        public final String BOOK_ISBN = "book_isbn";
    }
}
