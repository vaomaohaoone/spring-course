package ru.otus.spring.course.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.course.data.BookAuthorLink;
import ru.otus.spring.course.data.BookStyleLink;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.service.BookService;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping(value = Endpoints.BOOKS)
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable("isbn") String isbn) {
        return ResponseEntity.of(bookService.getBookById(isbn));
    }

    @DeleteMapping("/{isbn}")
    public void deleteById(@PathVariable("isbn") String isbn) {
        bookService.deleteById(isbn);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        if (bookService.getBookById(isbn).isPresent()) {
            Book result = bookService.saveBook(book);
            return ResponseEntity.ok(result);
        }
        else return ResponseEntity.of(Optional.empty());
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @PostMapping("/name/{name}/year/{publishedYear}/style/{style}")
    public ResponseEntity<Book> createBook(@PathVariable("name") String name, @PathVariable("publishedYear") Integer publishedYear, @PathVariable("style") String style) {
        return ResponseEntity.ok(bookService.createBook(name, Year.of(publishedYear), style));
    }

    @PostMapping("/author/link")
    public boolean linkAuthorAndBook(@RequestBody BookAuthorLink bookAuthorLink) {
        return bookService.linkAuthorAndBook(bookAuthorLink.getAuthorId(), bookAuthorLink.getIsbn());
    }

    @DeleteMapping("/author/unlink")
    public boolean unlinkAuthorAndBook(@RequestBody BookAuthorLink bookAuthorLink) {
        return bookService.unlinkAuthorAndBook(bookAuthorLink.getAuthorId(), bookAuthorLink.getIsbn());
    }

    @PostMapping("/style/link")
    public boolean addStyleForBook(@RequestBody BookStyleLink bookStyleLink) {
        return bookService.addStyleForBook(bookStyleLink.getIsbn(), bookStyleLink.getStyle());
    }

    @DeleteMapping("/style/unlink")
    public boolean deleteStyleForBook(@RequestBody BookStyleLink bookStyleLink) {
        return bookService.removeStyleForBook(bookStyleLink.getIsbn(), bookStyleLink.getStyle());
    }

    @GetMapping("/isbn/{isbn}/authors/list")
    public Set<Author> getAllAuthorsOfBook(@PathVariable("isbn") String isbn) {
        return bookService.getAllAuthorsOfBook(isbn);
    }

    @GetMapping("/authors/count/{count}/list")
    public Set<Book> getBooksWhereAuthorsEquals(@PathVariable("count") Integer count) {
        return bookService.getBooksWhereAuthorsEquals(count);
    }

    @GetMapping("/after/year/{year}/list")
    public Set<Book> getBooksWherePublishedYearAfter(@PathVariable("year") Integer year) {
        return bookService.getBooksByPublishedYearGreaterThan(Year.of(year));
    }
}
