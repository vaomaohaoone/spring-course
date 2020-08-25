package ru.otus.spring.course;

import org.apache.commons.lang3.RandomStringUtils;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.documents.Style;

import java.time.Year;
import java.util.HashSet;

public class EntityUtils {
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
        return new Style().setStyle(rdmString(10));
    }

    public static Comment createComment(Book book) {
        return new Comment().setText(rdmString(50)).setBook(book);
    }

    public static String rdmString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    private static Year rdmYear() {
        return Year.of(Double.valueOf(Math.random() * 5000).intValue());
    }
}