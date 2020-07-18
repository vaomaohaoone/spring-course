package ru.otus.spring.course;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.entities.links.AuthorBook;
import ru.otus.spring.course.entities.links.BookStyle;
import ru.otus.spring.course.repository.*;

import java.time.Year;
import java.util.UUID;

@Component
public class EntityUtils {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private StyleRepository styleRepository;
    @Autowired
    private AuthorBookRepository authorBookRepository;
    @Autowired
    private BookStyleRepository bookStyleRepository;

    public Author createAndSaveAuthor() {
        return authorRepository.save(createAuthor());
    }

    public Book createAndSaveBook() {
        return bookRepository.save(createBook());
    }

    public Style createAndSaveStyle() {
        return styleRepository.save(createStyle());
    }

    public AuthorBook linkAuthorAndBook(UUID authorId, UUID isbn) {
        return authorBookRepository.save(new AuthorBook(authorId, isbn));
    }

    public BookStyle linkBookWithStyle(UUID isbn, String style) {
        return bookStyleRepository.save(new BookStyle(isbn, style));
    }

    public static Author createAuthor() {
        return new Author(rdmString(5), rdmString(10));
    }

    public static Book createBook() {
        return new Book(rdmString(15), rdmYear());
    }

    public static Style createStyle() {
        return new Style(rdmString(10));
    }

    private static String rdmString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    private static Year rdmYear() {
        return Year.of(Double.valueOf(Math.random() * 5000).intValue());
    }
}
