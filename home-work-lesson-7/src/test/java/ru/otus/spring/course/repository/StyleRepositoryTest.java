package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.JdbcTest;
import ru.otus.spring.course.entities.Style;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StyleRepositoryTest extends JdbcTest {
    @Autowired
    private StyleRepository styleRepository;

    private Style style;

    @BeforeEach
    void before() {
        style = entityUtils.createAndSaveStyle();
    }

    @Test
    void testSaveStyleEntity() {
        Style style = EntityUtils.createStyle();
        Style actual = styleRepository.save(style);
        assertEquals(style, actual);
    }

    @Test
    void testFindByIdStyle() {
        Optional<Style> actualStyleOptional = styleRepository.findById(style.getName());
        assertTrue(actualStyleOptional.isPresent());
        assertEquals(style, actualStyleOptional.get());
    }

    @Test
    void testDeleteByIdStyle() {
        Optional<Style> actualStyleOptional = styleRepository.findById(style.getName());
        assertTrue(actualStyleOptional.isPresent());

        styleRepository.deleteById(style.getName());

        actualStyleOptional = styleRepository.findById(style.getName());
        assertFalse(actualStyleOptional.isPresent());
    }

    @Test
    void testFindAllStyle() {
        Style style = EntityUtils.createStyle();
        styleRepository.save(style);
        List<Style> styles = styleRepository.findAll();
        assertEquals(2, styles.size());
    }
}
