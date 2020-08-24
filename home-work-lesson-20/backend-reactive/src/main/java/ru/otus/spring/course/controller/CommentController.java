package ru.otus.spring.course.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.data.CommentBody;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.service.CommentService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = Endpoints.COMMENTS)
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Mono<Comment> saveComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.saveComment(comment));
    }

    @GetMapping("/{id}")
    public Mono<Comment> getComment(@PathVariable("id") String id) {
        return ResponseEntity.of(commentService.getCommentById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id) {
        commentService.deleteById(id);
    }

    @GetMapping
    public Flux<Comment> getAllComments() {
        return commentService.getAll();
    }

    @PostMapping("/add")
    public Mono<Comment> addCommentToBook(@RequestBody CommentBody commentBody) {
        return ResponseEntity.ok(commentService.addCommentToBook(commentBody.getIsbn(), commentBody.getText()));
    }

    @GetMapping("/isbn/{isbn}")
    public Flux<Comment> getCommentsByBookIsbn(@PathVariable("isbn") String isbn) {
        return commentService.getCommentsByIsbn(isbn);
    }
}
