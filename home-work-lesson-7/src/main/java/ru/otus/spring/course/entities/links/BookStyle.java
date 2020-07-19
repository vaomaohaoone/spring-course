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
public class BookStyle {

    UUID bookIsbn;
    String styleName;

    public static final EntitySpecification<BookStyle, BookStyle> SPECIFICATION = new BookStyleSpecification();

    private static class BookStyleSpecification implements EntitySpecification<BookStyle, BookStyle> {
        private static final RowMapper<BookStyle> ROW_MAPPER = (resultSet, i) -> {
            UUID bookIsbn = UUID.fromString(resultSet.getString(Fields.BOOK_ISBN));
            String styleName = resultSet.getString(Fields.STYLE_NAME);
            return new BookStyle(bookIsbn, styleName);
        };

        private static final Function<BookStyle, MapSqlParameterSource> PARAMS_MAPPER = bookStyle -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            Optional.ofNullable(bookStyle.getBookIsbn()).ifPresent(bookIsbn -> params.addValue(Fields.BOOK_ISBN, bookIsbn));
            Optional.ofNullable(bookStyle.getStyleName()).ifPresent(styleName -> params.addValue(Fields.STYLE_NAME, styleName));
            return params;
        };

        @Override
        public String tableName() {
            return "book_style";
        }

        @Override
        public Set<String> idColumns() {
            return Set.of(Fields.BOOK_ISBN, Fields.STYLE_NAME);
        }

        @Override
        public RowMapper<BookStyle> rowMapper() {
            return ROW_MAPPER;
        }

        @Override
        public MapSqlParameterSource params(BookStyle bookStyle) {
            return PARAMS_MAPPER.apply(bookStyle);
        }

        @Override
        public MapSqlParameterSource idParams(BookStyle bookStyle) {
            return PARAMS_MAPPER.apply(bookStyle);
        }

        @Override
        public BookStyle getId(BookStyle bookStyle) {
            return bookStyle;
        }
    }

    @UtilityClass
    public static class Fields {
        public final String BOOK_ISBN = "book_isbn";
        public final String STYLE_NAME = "style_name";
    }
}
