package ru.otus.spring.course.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.links.AuthorBook;

import java.util.List;
import java.util.UUID;

@Repository
public class AuthorBookRepository extends DataRepository<AuthorBook, AuthorBook>{
    public AuthorBookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, AuthorBook.SPECIFICATION);
    }

    public List<Author> findAllAuthorsOfBook(UUID isbn) {
        String query = String.format("SELECT * FROM %s ab INNER JOIN %s a ON ab.%s = a.%s WHERE ab.%s=:isbn",
                AuthorBook.SPECIFICATION.tableName(),
                Author.SPECIFICATION.tableName(),
                AuthorBook.Fields.AUTHOR_ID,
                Author.Fields.ID,
                AuthorBook.Fields.BOOK_ISBN
                );
        MapSqlParameterSource params = new MapSqlParameterSource("isbn", isbn);
        return jdbcTemplate.query(query, params, Author.SPECIFICATION.rowMapper());
    }

    public List<Book> findAllBooksForByAuthor(UUID authorId) {
        String query = String.format("SELECT * FROM %s ab INNER JOIN %s b ON ab.%s = b.%s WHERE ab.%s=:author_id",
                AuthorBook.SPECIFICATION.tableName(),
                Book.SPECIFICATION.tableName(),
                AuthorBook.Fields.BOOK_ISBN,
                Book.Fields.ISBN,
                AuthorBook.Fields.AUTHOR_ID
        );
        MapSqlParameterSource params = new MapSqlParameterSource("author_id", authorId);
        return jdbcTemplate.query(query, params, Book.SPECIFICATION.rowMapper());
    }

}
