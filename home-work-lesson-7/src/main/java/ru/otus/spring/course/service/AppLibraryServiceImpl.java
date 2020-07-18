package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.entities.links.AuthorBook;
import ru.otus.spring.course.entities.links.BookStyle;
import ru.otus.spring.course.repository.*;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppLibraryServiceImpl implements AppLibraryService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final StyleRepository styleRepository;
    private final BookStyleRepository bookStyleRepository;
    private final AuthorBookRepository authorBookRepository;

    @Override
    public Book createBook(String name, Year year) {
        return bookRepository.save(new Book(name, year));
    }

    @Override
    public Book createBook(String name, Year year, String style) {
        if(styleRepository.findById(style).isEmpty()){
           styleRepository.save(new Style(style));
        }
        Book book = bookRepository.save(new Book(name, year));
        bookStyleRepository.save(new BookStyle(book.getIsbn(), style));
        return book;
    }

    @Override
    public Author createAuthor(String name, String surname) {
        return authorRepository.save(new Author(name, surname));
    }

    @Override
    public boolean linkAuthorAndBook(UUID authorId, UUID isbn) {
        if(authorRepository.findById(authorId).isPresent() && bookRepository.findById(isbn).isPresent()) {
            authorBookRepository.save(new AuthorBook(authorId, isbn));
            return true;
        }
        return false;
    }

    @Override
    public boolean addStyleForBook(UUID isbn, String style) {
        if(bookRepository.findById(isbn).isPresent()) {
            if(styleRepository.findById(style).isEmpty())
                styleRepository.save(new Style(style));
            bookStyleRepository.save(new BookStyle(isbn, style));
            return true;
        }
        return false;
    }

    @Override
    public Style createStyle(String style) {
        return styleRepository.save(new Style(style));
    }

    @Override
    public List<Author> getAllAuthorsOfBook(UUID isbn) {
        return authorBookRepository.findAllAuthorsOfBook(isbn);
    }

    @Override
    public List<Book> getAllBooksByAuthor(UUID authorId) {
        return authorBookRepository.findAllBooksForByAuthor(authorId);
    }
}
