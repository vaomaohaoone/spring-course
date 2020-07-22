package ru.otus.spring.course.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.entities.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class AuthorRepository implements EntityRepository<Author, UUID> {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Author findById(UUID id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> findAll() {
        return entityManager.createQuery("from author", Author.class).getResultList();
    }

    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            entityManager.persist(author);
            return author;
        }
        else
            return entityManager.merge(author);
    }

    @Override
    public Author update(Author author) {
        entityManager.merge(author);
        return author;
    }
}
