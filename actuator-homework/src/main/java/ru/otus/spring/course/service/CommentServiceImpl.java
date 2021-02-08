package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.config.security.UserRoles;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    @PreAuthorize("hasRole('" + UserRoles.COMMENTER_ROLE + "')")
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    @PreAuthorize("hasRole('" + UserRoles.COMMENTER_ROLE + "')")
    public void deleteById(String commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('" + UserRoles.COMMENTER_ROLE + "')")
    public Comment addCommentToBook(String isbn, String text) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        return optionalBook.map(book -> commentRepository.save(new Comment().setText(text).setBook(book))).orElse(null);
    }

    @Override
    public List<Comment> getCommentsByIsbn(String isbn) {
        return commentRepository.findCommentsByBook_Id(isbn);
    }
}
