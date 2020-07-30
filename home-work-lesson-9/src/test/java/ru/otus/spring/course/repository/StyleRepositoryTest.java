package ru.otus.spring.course.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Style;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Import(StyleRepositoryImpl.class)
public class StyleRepositoryTest {
    @Autowired
    private StyleRepositoryImpl styleRepository;

    @Autowired
    private TestEntityManager em;

    private Style style;

    @BeforeEach
    void before() {
        style = em.persist(EntityUtils.createStyle());
    }

    @Test
    void findByIdTest() {
        Style actual = styleRepository.findById(style.getId());
        assertEquals(style, actual);
    }

    @Test
    void findAllTest() {
        List<Style> styles = styleRepository.findAll();
        assertEquals(style, styles.get(0));
    }

    @Test
    void deleteTest() {
        Style actual = styleRepository.findById(style.getId());
        assertEquals(style, actual);

        styleRepository.delete(style);
        actual = styleRepository.findById(actual.getId());
        assertNull(actual);
    }

    @Test
    void saveWhenIdIsNullTest() {
        Style style = EntityUtils.createStyle();
        style = styleRepository.save(style);

        Style actual = styleRepository.findById(style.getId());
        assertEquals(style, actual);
    }
}
