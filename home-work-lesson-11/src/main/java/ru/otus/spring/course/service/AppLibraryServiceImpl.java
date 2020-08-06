package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppLibraryServiceImpl implements AppLibraryService{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final StyleRepository styleRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Book createBook(String name, Year year) {
        return bookRepository.save(new Book()
                .setName(name)
                .setPublishedYear(year));
    }

    @Override
    @Transactional
    public Book createBook(String name, Year year, String style) {
        if (styleRepository.findById(style).isEmpty()){
            styleRepository.save(new Style().setId(style));
        }
        return bookRepository.save(new Book()
                .setName(name)
                .setPublishedYear(year));
    }

    @Override
    @Transactional
    public Author createAuthor(String name, String surname) {
        return authorRepository.save(new Author()
                .setName(name)
                .setSurname(surname));
    }

    @Override
    @Transactional
    public Style createStyle(String style) {
        return styleRepository.save(new Style()
                .setId(style));
    }

    @Override
    @Transactional
    public boolean linkAuthorAndBook(UUID authorId, UUID isbn) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if(optionalAuthor.isPresent() && optionalBook.isPresent()) {
            optionalBook.get().getAuthors().add(optionalAuthor.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean addStyleForBook(UUID isbn, String styleName) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if(optionalBook.isPresent()) {
            Optional<Style> optionalStyle = styleRepository.findById(styleName);
            Style style;
            if(optionalStyle.isEmpty())
                style = styleRepository.save(new Style().setId(styleName));
            else
                style = optionalStyle.get();
            optionalBook.get().getStyles().add(style);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Author> getAllAuthorsOfBook(UUID isbn) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if(optionalBook.isPresent())
            return optionalBook.get().getAuthors();
        else
            return new HashSet<>();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Book> getAllBooksByAuthor(UUID authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if (optionalAuthor.isPresent())
            return optionalAuthor.get().getBooks();
        else
            return new HashSet<>();
    }

    @Override
    @Transactional
    public Comment addCommentToBook(UUID isbn, String text) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if(optionalBook.isPresent())
            return commentRepository.save(new Comment().setText(text));
        else
            return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Book> getBooksByPublishedYearGreaterThan(Year year) {
        return bookRepository.findByPublishedYearGreaterThan(year);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Book> getBooksWhereAuthorsMoreThan(int count) {
        return bookRepository.findBooksWhereAuthorsMoreThan(count);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Author> getAuthorsWhereBooksMoreThan(int count) {
        return authorRepository.findAuthorsWhereBooksMoreThan(count);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Author> getAuthorsByName(String name) {
        return authorRepository.findAuthorsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Author> getAuthorsBySurname(String surname) {
        return authorRepository.findAuthorsBySurname(surname);
    }
}
