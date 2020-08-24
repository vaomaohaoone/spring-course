package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final StyleRepository styleRepository;
    private final CommentRepository commentRepository;

    @Override
    public Mono<Book> saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Mono<Book> getBookById(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public void deleteById(String isbn) {
        Mono<Book> optionalBook = getBookById(isbn);
        if (optionalBook.isPresent()) {
            Set<Author> authors = optionalBook.get().getAuthors();
            for (Author author: authors) {
                author.getBooks().remove(optionalBook.get());
                authorRepository.save(author);
            }
            Set<Style> styles = optionalBook.get().getStyles();
            for (Style style: styles) {
                style.getBooks().remove(optionalBook.get());
                styleRepository.save(style);
            }
            commentRepository.deleteCommentByBook_Id(isbn);
            bookRepository.deleteById(isbn);
        }
    }

    @Override
    public Flux<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Mono<Book> createBook(String name, Year year) {
        return bookRepository.save(new Book()
                .setName(name)
                .setPublishedYear(year));
    }

    @Override
    public Mono<Book> createBook(String name, Year year, String style) {
        if (styleRepository.findById(style).isEmpty()) {
            styleRepository.save(new Style().setStyle(style));
        }
        return bookRepository.save(new Book()
                .setName(name)
                .setPublishedYear(year));
    }

    @Override
    public Mono<Boolean> linkAuthorAndBook(String authorId, String isbn) {
        Mono<Author> optionalAuthor = authorRepository.findById(authorId);
        Mono<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalAuthor.isPresent() && optionalBook.isPresent()) {
            optionalBook.get().getAuthors().add(optionalAuthor.get());
            optionalAuthor.get().getBooks().add(optionalBook.get());
            bookRepository.save(optionalBook.get());
            authorRepository.save(optionalAuthor.get());
            return true;
        }
        return false;
    }

    @Override
    public Mono<Boolean> unlinkAuthorAndBook(String authorId, String isbn) {
        Mono<Author> optionalAuthor = authorRepository.findById(authorId);
        Mono<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalAuthor.isPresent() && optionalBook.isPresent()) {
            optionalBook.get().getAuthors().remove(optionalAuthor.get());
            optionalAuthor.get().getBooks().remove(optionalBook.get());
            bookRepository.save(optionalBook.get());
            authorRepository.save(optionalAuthor.get());
            return true;
        }
        return false;
    }

    @Override
    public Mono<Boolean> addStyleForBook(String isbn, String styleName) {
        Mono<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isPresent()) {
            Flux<Style> stylesFromDb = styleRepository.findStylesByStyle(styleName);
            Style style;
            if (stylesFromDb.isEmpty())
                style = styleRepository.save(new Style().setStyle(styleName));
            else
                style = stylesFromDb.stream().findFirst().get();
            optionalBook.get().getStyles().add(style);
            style.getBooks().add(optionalBook.get());
            bookRepository.save(optionalBook.get());
            styleRepository.save(style);
            return true;
        }
        return false;
    }

    @Override
    public Mono<Boolean> removeStyleForBook(String isbn, String styleName) {
        Mono<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isPresent()) {
            Flux<Style> stylesFromDb = styleRepository.findStylesByStyle(styleName);
            Style style;
            if (stylesFromDb.isEmpty())
                return false;
            else
                style = stylesFromDb.stream().findFirst().get();
            optionalBook.get().getStyles().remove(style);
            style.getBooks().remove(optionalBook.get());
            bookRepository.save(optionalBook.get());
            styleRepository.save(style);
            return true;
        }
        return false;
    }

    @Override
    public Flux<Author> getAllAuthorsOfBook(String isbn) {
        Mono<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isPresent())
            return optionalBook.get().getAuthors();
        else
            return new HashSet<>();
    }

    @Override
    public Flux<Book> getBooksWhereAuthorsEquals(int count) {
        return bookRepository.findBooksWhereAuthorsEquals(count);
    }

    @Override
    public Flux<Book> getBooksByPublishedYearGreaterThan(Year year) {
        return bookRepository.findByPublishedYearGreaterThan(year.getValue());
    }
}
