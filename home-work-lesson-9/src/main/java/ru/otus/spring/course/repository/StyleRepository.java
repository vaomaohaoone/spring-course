package ru.otus.spring.course.repository;

import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.Style;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StyleRepository implements EntityRepository<Style, String>{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Style findById(String id) {
        return entityManager.find(Style.class, id);
    }

    @Override
    public List<Style> findAll() {
        return entityManager.createQuery("from style", Style.class).getResultList();
    }

    @Override
    public void delete(Style style) {
        entityManager.remove(style);
    }

    @Override
    public Style save(Style style) {
        if (style.getId() == null) {
            entityManager.persist(style);
            return style;
        }
        else
            return entityManager.merge(style);
    }

    @Override
    public Style update(Style style) {
        return entityManager.merge(style);
    }
}
