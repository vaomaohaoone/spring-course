package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.course.entities.links.BookStyle;
import ru.otus.spring.course.JdbcTest;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookStyleRepositoryTest extends JdbcTest {

    @Autowired
    private BookStyleRepository bookStyleRepository;
    private BookStyle bookStyle;

    @BeforeEach
    void before() {
        Book book = entityUtils.createAndSaveBook();
        Style style = entityUtils.createAndSaveStyle();
        bookStyle = entityUtils.linkBookWithStyle(book.getIsbn(), style.getName());
    }

    @Test
    void testSaveBookStyle() {
        Book book = entityUtils.createAndSaveBook();
        Style style = entityUtils.createAndSaveStyle();

        BookStyle bookStyle = new BookStyle(book.getIsbn(), style.getName());
        BookStyle actualBookStyle = bookStyleRepository.save(bookStyle);
        assertEquals(bookStyle, actualBookStyle);
    }

    @Test
    void testFindByIdBookStyle() {
        Optional<BookStyle> optionalBookStyle = bookStyleRepository.findById(bookStyle);
        assertTrue(optionalBookStyle.isPresent());
        assertEquals(bookStyle, optionalBookStyle.get());
    }

    @Test
    void testDeleteByIdBookStyle() {
        Optional<BookStyle> optionalBookStyle = bookStyleRepository.findById(bookStyle);
        assertTrue(optionalBookStyle.isPresent());
        assertEquals(bookStyle, optionalBookStyle.get());

        bookStyleRepository.deleteById(bookStyle);
        optionalBookStyle = bookStyleRepository.findById(bookStyle);
        assertFalse(optionalBookStyle.isPresent());
    }

    @Test
    void testFindAllBookStyle() {
        Book book = entityUtils.createAndSaveBook();
        Style style = entityUtils.createAndSaveStyle();

        BookStyle bookStyle = new BookStyle(book.getIsbn(), style.getName());
        BookStyle actualBookStyle = bookStyleRepository.save(bookStyle);

        List<BookStyle> bookStyles = bookStyleRepository.findAll();

        assertEquals(2, bookStyles.size());
    }
}
