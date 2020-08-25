package ru.otus.spring.course.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.data.BookAuthorLink;
import ru.otus.spring.course.data.BookStyleLink;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


@SpringBootTest
public class BookRouterTest {

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private StyleRepository styleRepository;
    @MockBean
    private CommentRepository commentRepository;
    @Autowired
    private RouterFunction bookRoute;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient
                .bindToRouterFunction(bookRoute)
                .build();
    }

    @Test
    void getAllBooks() {
        Author author = EntityUtils.createAuthor();
        Style style = EntityUtils.createStyle();
        Book book = EntityUtils.createBook();
        book.getStyles().add(style);
        book.getAuthors().add(author);
        List<Book> books = Arrays.asList(book);
        Flux<Book> bookFlux = Flux.fromIterable(books);
        when(bookRepository.findAll()).thenReturn(bookFlux);

        client.get()
                .uri(Endpoints.BOOKS)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .isEqualTo(books);

        verify(bookRepository).findAll();
    }

    @Test
    void getBook() {
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));

        client.get()
                .uri(Endpoints.BOOKS + "/" + book.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .isEqualTo(book);

        verify(bookRepository).findById(book.getId());
    }

    @Test
    void saveBook() {
        Book book = EntityUtils.createBook();
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        client.post()
                .uri(Endpoints.BOOKS)
                .bodyValue(book)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .isEqualTo(book);

        verify(bookRepository).save(book);
    }

    @Test
    void updateBook() {
        Book book = EntityUtils.createBook();
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        client.put()
                .uri(Endpoints.BOOKS + "/" + book.getId())
                .bodyValue(book)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .isEqualTo(book);

        verify(bookRepository).save(book);
    }

    @Test
    void deleteBook() {
        Author author = EntityUtils.createAuthor();
        Style style = EntityUtils.createStyle();
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        book.getAuthors().add(author);
        book.getStyles().add(style);
        author.getBooks().add(book);
        style.getBooks().add(book);

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(authorRepository.save(any(Author.class))).thenReturn(Mono.just(author));
        when(styleRepository.save(any(Style.class))).thenReturn(Mono.just(style));
        when(commentRepository.deleteCommentByBook_Id(book.getId())).thenReturn(Mono.empty());
        when(bookRepository.deleteById(book.getId())).thenReturn(Mono.empty());

        client.delete()
                .uri(Endpoints.BOOKS + "/" + book.getId())
                .exchange()
                .expectStatus()
                .isOk();

        verify(bookRepository).findById(book.getId());
        verify(authorRepository).save(any(Author.class));
        verify(styleRepository).save(any(Style.class));
        verify(commentRepository).deleteCommentByBook_Id(book.getId());
        verify(bookRepository).deleteById(book.getId());
    }

    @Test
    void linkAuthorAndBook() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(authorRepository.save(any(Author.class))).thenReturn(Mono.just(author));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        client.post()
                .uri(Endpoints.BOOKS + "/author/link")
                .bodyValue(new BookAuthorLink(book.getId(), author.getId()))
                .exchange()
                .expectStatus()
                .isOk();

        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
        verify(authorRepository).save(any(Author.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void unlinkAuthorAndBook() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(authorRepository.save(any(Author.class))).thenReturn(Mono.just(author));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        client.method(HttpMethod.DELETE)
                .uri(Endpoints.BOOKS + "/author/unlink")
                .bodyValue(new BookAuthorLink(book.getId(), author.getId()))
                .exchange()
                .expectStatus()
                .isOk();

        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
        verify(authorRepository).save(any(Author.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void linkStyleAndBook() {
        Style style = EntityUtils.createStyle().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        when(styleRepository.findStylesByStyle(style.getStyle())).thenReturn(Flux.just(style));
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(styleRepository.save(any(Style.class))).thenReturn(Mono.just(style));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        client.post()
                .uri(Endpoints.BOOKS + "/style/link")
                .bodyValue(new BookStyleLink(book.getId(), style.getStyle()))
                .exchange()
                .expectStatus()
                .isOk();

        verify(styleRepository).findStylesByStyle(style.getStyle());
        verify(bookRepository).findById(book.getId());
        verify(styleRepository).save(any(Style.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void unlinkStyleAndBook() {
        Style style = EntityUtils.createStyle().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        when(styleRepository.findStylesByStyle(style.getStyle())).thenReturn(Flux.just(style));
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(styleRepository.save(any(Style.class))).thenReturn(Mono.just(style));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        client.method(HttpMethod.DELETE)
                .uri(Endpoints.BOOKS + "/style/unlink")
                .bodyValue(new BookStyleLink(book.getId(), style.getStyle()))
                .exchange()
                .expectStatus()
                .isOk();

        verify(styleRepository).findStylesByStyle(style.getStyle());
        verify(bookRepository).findById(book.getId());
        verify(styleRepository).save(any(Style.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void getAllAuthorsOfBook() {
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        book.getAuthors().add(author);

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));

        client.get()
                .uri(Endpoints.BOOKS + "/isbn/" + book.getId() + "/authors/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class)
                .isEqualTo(Arrays.asList(author));

        verify(bookRepository).findById(book.getId());
    }

    @Test
    void getBooksWhereAuthorsEquals() {
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        book.getAuthors().add(author);

        when(bookRepository.findBooksWhereAuthorsEquals(1)).thenReturn(Flux.just(book));

        client.get()
                .uri(Endpoints.BOOKS + "/authors/count/" + 1 + "/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .isEqualTo(Arrays.asList(book));

        verify(bookRepository).findBooksWhereAuthorsEquals(1);
    }

    @Test
    void getBooksByPublishedYearGreaterThan() {
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        when(bookRepository.findByPublishedYearGreaterThan(book.getPublishedYear().getValue() - 1)).thenReturn(Flux.just(book));

        client.get()
                .uri(Endpoints.BOOKS + "/after/year/" + (book.getPublishedYear().getValue() - 1) + "/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .isEqualTo(Arrays.asList(book));

        verify(bookRepository).findByPublishedYearGreaterThan(book.getPublishedYear().getValue() - 1);
    }

}
