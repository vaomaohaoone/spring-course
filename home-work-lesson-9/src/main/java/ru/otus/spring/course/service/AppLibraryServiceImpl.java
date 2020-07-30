package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.repository.AuthorRepositoryImpl;
import ru.otus.spring.course.repository.BookRepositoryImpl;
import ru.otus.spring.course.repository.CommentRepositoryImpl;
import ru.otus.spring.course.repository.StyleRepositoryImpl;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppLibraryServiceImpl implements AppLibraryService{
    private final AuthorRepositoryImpl authorRepository;
    private final BookRepositoryImpl bookRepository;
    private final StyleRepositoryImpl styleRepository;
    private final CommentRepositoryImpl commentRepository;

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
        if (styleRepository.findById(style) == null){
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
        Author author = authorRepository.findById(authorId);
        Book book = bookRepository.findById(isbn);
        if(author != null && book != null) {
            bookRepository.linkAuthorAndBook(author, book);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean addStyleForBook(UUID isbn, String styleName) {
        Book book = bookRepository.findById(isbn);
        if(book != null) {
            Style style = styleRepository.findById(styleName);
            if(style == null)
                style = styleRepository.save(new Style().setId(styleName));
            bookRepository.linkBookAndStyle(book, style);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Set<Author> getAllAuthorsOfBook(UUID isbn) {
        Book book = bookRepository.findById(isbn);
        if(book != null)
            return book.getAuthors();
        else
            return new HashSet<>();
    }

    @Override
    @Transactional
    public Set<Book> getAllBooksByAuthor(UUID authorId) {
        Author author = authorRepository.findById(authorId);
        if (author != null)
            return author.getBooks();
        else
            return new HashSet<>();
    }

    @Override
    @Transactional
    public Comment addCommentToBook(UUID isbn, String text) {
        Book book = bookRepository.findById(isbn);
        if(book != null)
            return commentRepository.save(new Comment().setText(text));
        else
            return null;
    }
}
