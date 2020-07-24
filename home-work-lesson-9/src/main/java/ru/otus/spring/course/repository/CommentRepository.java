package ru.otus.spring.course.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.entities.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class CommentRepository implements EntityRepository<Comment, UUID>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Comment findById(UUID id) {
        return entityManager.find(Comment.class, id);
    }

    @Override
    public List<Comment> findAll() {
        return entityManager.createQuery("from comment", Comment.class).getResultList();
    }

    @Override
    public void delete(Comment comment) {
        entityManager.remove(comment);
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            entityManager.persist(comment);
            return comment;
        }
        else
            return entityManager.merge(comment);
    }

    @Override
    public Comment update(Comment comment) {
        entityManager.merge(comment);
        return comment;
    }
}
