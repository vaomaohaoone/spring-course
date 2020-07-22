package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppLibraryServiceImpl implements AppLibraryService{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final StyleRepository styleRepository;

    @Override
    public Book createBook(String name, Year year) {
        return bookRepository.save(new Book()
                .setName(name)
                .setPublishedYear(year));
    }

    @Override
    public Book createBook(String name, Year year, String style) {
        if (styleRepository.findById(style) == null){
            styleRepository.save(new Style().setId(style));
        }
        return bookRepository.save(new Book()
                .setName(name)
                .setPublishedYear(year));
    }

    @Override
    public Author createAuthor(String name, String surname) {
        return authorRepository.save(new Author()
                .setName(name)
                .setSurname(surname));
    }

    @Override
    public Style createStyle(String style) {
        return styleRepository.save(new Style()
                .setId(style));
    }

    @Override
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
    public Set<Author> getAllAuthorsOfBook(UUID isbn) {
        Book book = bookRepository.findById(isbn);
        if(book != null)
            return book.getAuthors();
        else
            return new HashSet<>();
    }

    @Override
    public Set<Book> getAllBooksByAuthor(UUID authorId) {
        Author author = authorRepository.findById(authorId);
        if (author != null)
            return author.getBooks();
        else
            return new HashSet<>();
    }
}
