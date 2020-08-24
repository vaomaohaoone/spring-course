package ru.otus.spring.course.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.service.AuthorService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping(value = Endpoints.AUTHORS)
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.saveAuthor(author));
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<Author> getAuthor(@PathVariable("authorId") String authorId) {
        return ResponseEntity.of(authorService.getAuthorById(authorId));
    }

    @DeleteMapping("/{authorId}")
    public void deleteById(@PathVariable("authorId") String authorId) {
        authorService.deleteById(authorId);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<Author> updateAuthor(@PathVariable String authorId, @RequestBody Author author) {
        if (authorService.getAuthorById(authorId).isPresent()) {
            Author result = authorService.saveAuthor(author);
            return ResponseEntity.ok(result);
        }
        else return ResponseEntity.of(Optional.empty());
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAll();
    }

    @GetMapping("/id/{authorId}/books/list")
    public Set<Book> getAllBooksByAuthor(@PathVariable String authorId) {
        return authorService.getAllBooksByAuthor(authorId);
    }

    @GetMapping("/books/count/{count}/list")
    public Set<Author> getAuthorsWhereAuthorsEquals(@PathVariable("count") Integer count) {
        return authorService.getAuthorsWhereBooksEquals(count);
    }

    @GetMapping("/name/{name}/list")
    public Set<Author> getAuthorsWithName(@PathVariable("name") String name) {
        return authorService.getAuthorsByName(name);
    }

    @GetMapping("/surname/{surname}/list")
    public Set<Author> getAuthorsWithSurname(@PathVariable("surname") String surname) {
        return authorService.getAuthorsBySurname(surname);
    }
}
