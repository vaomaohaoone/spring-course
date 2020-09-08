package ru.otus.spring.course.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.data.CommentBody;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class CommentHandler {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> getAllComments(ServerRequest servletRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(commentRepository.findAll(), Comment.class);
    }

    public Mono<ServerResponse> saveComment(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Comment.class).flatMap(commentRepository::save), Comment.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(commentRepository.findById(serverRequest.pathVariable("id")), Comment.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(commentRepository.deleteById(serverRequest.pathVariable("id")), Void.class);
    }

    public Mono<ServerResponse> updateComment(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Comment.class).flatMap(commentRepository::save), Comment.class);
    }

    public Mono<ServerResponse> addCommentToBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(CommentBody.class)
                        .flatMap(commentBody -> {
                            Mono<Book> bookMono = bookRepository.findById(commentBody.getIsbn());
                            return bookMono.flatMap(book -> commentRepository.save(
                                    new Comment()
                                            .setText(commentBody.getText()).setBook(book)));
                        }), Comment.class);
    }

    public Mono<ServerResponse> getCommentsByBookIsbn(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                commentRepository.findCommentsByBook_Id(serverRequest.pathVariable("isbn")
                ), Comment.class);
    }

}
