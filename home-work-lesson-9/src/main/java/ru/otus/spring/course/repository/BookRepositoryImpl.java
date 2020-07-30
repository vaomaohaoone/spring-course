package ru.otus.spring.course.repository;

import org.springframework.stereotype.Repository;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book findById(UUID id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("from book", Book.class).getResultList();
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            entityManager.persist(book);
            return book;
        } else
            return entityManager.merge(book);
    }

    @Override
    public Book update(Book book) {
        entityManager.merge(book);
        return book;
    }

    @Override
    public void linkAuthorAndBook(Author author, Book book) {
        book.addAuthor(author);
        update(book);
    }

    @Override
    public void linkBookAndStyle(Book book, Style style) {
        book.addStyle(style);
        update(book);
    }
}
