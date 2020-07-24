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
public class BookRepository implements EntityRepository<Book, UUID> {
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
        }
        else
            return entityManager.merge(book);
    }

    @Override
    public Book update(Book book) {
        entityManager.merge(book);
        return book;
    }

    public void linkAuthorAndBook(Author author, Book book) {
        book.getAuthors().add(author);
        author.getBooks().add(book);
        update(book);
    }

    public void linkBookAndStyle(Book book, Style style) {
        book.getStyles().add(style);
        style.getBooks().add(book);
        update(book);
    }
}
