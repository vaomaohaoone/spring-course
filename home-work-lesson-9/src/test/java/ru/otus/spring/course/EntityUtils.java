package ru.otus.spring.course;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.time.Year;
import java.util.HashSet;

@Component
public class EntityUtils {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private StyleRepository styleRepository;

    public Author createAndSaveAuthor() {
        return authorRepository.save(createAuthor());
    }

    public Book createAndSaveBook() {
        return bookRepository.save(createBook());
    }

    public Style createAndSaveStyle() {
        return styleRepository.save(createStyle());
    }

    public static Author createAuthor() {
        return new Author().setName(rdmString(5)).setSurname(rdmString(10));
    }

    public static Author createAuthorWithBooks(){
        return new Author().setName(rdmString(5)).setSurname(rdmString(10)).setBooks(new HashSet<>(){{add(createBook());}});
    }

    public static Book createBook() {
        return new Book().setName(rdmString(15)).setPublishedYear(rdmYear());
    }

    public static Book createBookWithSomeAuthorsAndStyles() {
        return new Book().setName(rdmString(15)).setPublishedYear(rdmYear()).setAuthors(new HashSet<>(){{add(createAuthor());}})
                .setStyles(new HashSet<>(){{add(createStyle());}});
    }

    public static Style createStyle() {
        return new Style().setId(rdmString(10));
    }

    private static String rdmString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    private static Year rdmYear() {
        return Year.of(Double.valueOf(Math.random() * 5000).intValue());
    }
}