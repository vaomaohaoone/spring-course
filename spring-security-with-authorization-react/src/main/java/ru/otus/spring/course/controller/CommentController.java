package ru.otus.spring.course.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.saveComment(comment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable("id") String id) {
        return ResponseEntity.of(commentService.getCommentById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id) {
        commentService.deleteById(id);
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Comment> addCommentToBook(@RequestBody CommentBody commentBody) {
        return ResponseEntity.ok(commentService.addCommentToBook(commentBody.getIsbn(), commentBody.getText()));
    }

    @GetMapping("/isbn/{isbn}")
    public List<Comment> getCommentsByBookIsbn(@PathVariable("isbn") String isbn) {
        return commentService.getCommentsByIsbn(isbn);
    }
}
