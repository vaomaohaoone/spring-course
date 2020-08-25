package ru.otus.spring.course.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.data.CommentBody;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentRouterTest {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CommentRepository commentRepository;
    @Autowired
    private RouterFunction commentRoute;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient
                .bindToRouterFunction(commentRoute)
                .build();
    }

    @Test
    void getAllComments() {
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));
        List<Comment> comments = Arrays.asList(comment);
        Flux<Comment> commentFlux = Flux.fromIterable(comments);
        when(commentRepository.findAll()).thenReturn(commentFlux);

        client.get()
                .uri(Endpoints.COMMENTS)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Comment.class)
                .isEqualTo(comments);

        verify(commentRepository).findAll();
    }

    @Test
    void getComment() {
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));
        when(commentRepository.findById(comment.getId())).thenReturn(Mono.just(comment));

        client.get()
                .uri(Endpoints.COMMENTS + "/" + comment.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Comment.class)
                .isEqualTo(comment);

        verify(commentRepository).findById(comment.getId());
    }

    @Test
    void saveComment() {
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));
        when(commentRepository.save(comment)).thenReturn(Mono.just(comment));

        client.post()
                .uri(Endpoints.COMMENTS)
                .bodyValue(comment)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Comment.class)
                .isEqualTo(comment);

        verify(commentRepository).save(comment);
    }

    @Test
    void updateComment() {
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));
        when(commentRepository.save(comment)).thenReturn(Mono.just(comment));

        client.put()
                .uri(Endpoints.COMMENTS + "/" + comment.getId())
                .bodyValue(comment)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Comment.class)
                .isEqualTo(comment);

        verify(commentRepository).save(comment);
    }

    @Test
    void deleteBook() {
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));

        when(commentRepository.deleteById(comment.getId())).thenReturn(Mono.empty());

        client.delete()
                .uri(Endpoints.COMMENTS + "/" + comment.getId())
                .exchange()
                .expectStatus()
                .isOk();

        verify(commentRepository).deleteById(comment.getId());
    }

    @Test
    void addCommentToBook() {
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(commentRepository.save(any(Comment.class))).thenReturn(Mono.just(comment));

        client.post()
                .uri(Endpoints.COMMENTS + "/add")
                .bodyValue(new CommentBody(comment.getText(), book.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Comment.class)
                .isEqualTo(comment);

        verify(bookRepository).findById(book.getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getCommentsByBookIsbn() {
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));
        Comment comment = EntityUtils.createComment(book).setId(EntityUtils.rdmString(36));

        when(commentRepository.findCommentsByBook_Id(book.getId())).thenReturn(Flux.just(comment));

        client.get()
                .uri(Endpoints.COMMENTS + "/isbn/" + book.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Comment.class)
                .isEqualTo(Arrays.asList(comment));

        verify(commentRepository).findCommentsByBook_Id(book.getId());
    }
}
