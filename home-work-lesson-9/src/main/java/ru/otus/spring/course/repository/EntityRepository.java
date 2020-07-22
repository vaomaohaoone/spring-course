package ru.otus.spring.course.repository;

import java.util.List;

public interface EntityRepository<T, ID> {
    T findById(ID id);

    List<T> findAll();

    void delete(T entity);

    T save(T entity);

    T update(T entity);
}
