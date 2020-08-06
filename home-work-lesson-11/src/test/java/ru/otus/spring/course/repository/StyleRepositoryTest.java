package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Style;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StyleRepositoryTest {
    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private TestEntityManager em;

    private Style style;

    @BeforeEach
    void before() {
        style = em.persist(EntityUtils.createStyle());
    }

    @Test
    void findByIdTest() {
        Optional<Style> actual = styleRepository.findById(style.getId());
        assertTrue(actual.isPresent());
        assertEquals(style, actual.get());
    }

    @Test
    void findAllTest() {
        List<Style> styles = styleRepository.findAll();
        assertEquals(style, styles.get(0));
    }

    @Test
    void deleteTest() {
        Optional<Style> actual = styleRepository.findById(style.getId());
        assertTrue(actual.isPresent());
        assertEquals(style, actual.get());

        styleRepository.delete(style);
        actual = styleRepository.findById(actual.get().getId());
        assertTrue(actual.isEmpty());
    }

    @Test
    void saveWhenIdIsNullTest() {
        Style style = EntityUtils.createStyle();
        style = styleRepository.save(style);

        Optional<Style> actual = styleRepository.findById(style.getId());
        assertTrue(actual.isPresent());
        assertEquals(style, actual.get());
    }
}
