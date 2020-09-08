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
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorRouterTest {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @Autowired
    private RouterFunction authorRoute;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient
                .bindToRouterFunction(authorRoute)
                .build();
    }

    @Test
    void getAllAuthors() {
        Author author = EntityUtils.createAuthor();
        Book book = EntityUtils.createBook();
        author.getBooks().add(book);
        List<Author> authors = Arrays.asList(author);
        Flux<Author> authorFlux = Flux.fromIterable(authors);
        when(authorRepository.findAll()).thenReturn(authorFlux);

        client.get()
                .uri(Endpoints.AUTHORS)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class)
                .isEqualTo(authors);

        verify(authorRepository).findAll();
    }

    @Test
    void getAuthor() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));

        client.get()
                .uri(Endpoints.AUTHORS + "/" + author.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Author.class)
                .isEqualTo(author);

        verify(authorRepository).findById(author.getId());
    }

    @Test
    void saveAuthor() {
        Author author = EntityUtils.createAuthor();
        when(authorRepository.save(author)).thenReturn(Mono.just(author));

        client.post()
                .uri(Endpoints.AUTHORS)
                .bodyValue(author)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Author.class)
                .isEqualTo(author);

        verify(authorRepository).save(author);
    }

    @Test
    void updateAuthor() {
        Author author = EntityUtils.createAuthor();
        when(authorRepository.save(author)).thenReturn(Mono.just(author));

        client.put()
                .uri(Endpoints.AUTHORS + "/" + author.getId())
                .bodyValue(author)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Author.class)
                .isEqualTo(author);

        verify(authorRepository).save(author);
    }

    @Test
    void deleteBook() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        book.getAuthors().add(author);
        author.getBooks().add(book);

        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(authorRepository.deleteById(author.getId())).thenReturn(Mono.empty());

        client.delete()
                .uri(Endpoints.AUTHORS + "/" + author.getId())
                .exchange()
                .expectStatus()
                .isOk();

        verify(authorRepository).findById(author.getId());
        verify(bookRepository).save(any(Book.class));
        verify(authorRepository).deleteById(author.getId());
    }

    @Test
    void getAllBooksByAuthor() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        book.getAuthors().add(author);
        author.getBooks().add(book);
        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));

        client.get()
                .uri(Endpoints.AUTHORS + "/id/" + author.getId() + "/books/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .isEqualTo(Arrays.asList(book));

        verify(authorRepository).findById(author.getId());
    }

    @Test
    void getAuthorsWhereAuthorsEquals() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        book.getAuthors().add(author);
        author.getBooks().add(book);
        when(authorRepository.findAuthorsWhereBooksEquals(1)).thenReturn(Flux.just(author));

        client.get()
                .uri(Endpoints.AUTHORS + "/books/count/" + 1 + "/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class)
                .isEqualTo(Arrays.asList(author));

        verify(authorRepository).findAuthorsWhereBooksEquals(1);
    }

    @Test
    void getAuthorsWithName() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));

        when(authorRepository.findAuthorsByName(author.getName())).thenReturn(Flux.just(author));

        client.get()
                .uri(Endpoints.AUTHORS + "/name/" + author.getName() + "/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class)
                .isEqualTo(Arrays.asList(author));

        verify(authorRepository).findAuthorsByName(author.getName());
    }

    @Test
    void getAuthorsWithSurname() {
        Author author = EntityUtils.createAuthor().setId(EntityUtils.rdmString(36));

        when(authorRepository.findAuthorsBySurname(author.getSurname())).thenReturn(Flux.just(author));

        client.get()
                .uri(Endpoints.AUTHORS + "/surname/" + author.getSurname() + "/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class)
                .isEqualTo(Arrays.asList(author));

        verify(authorRepository).findAuthorsBySurname(author.getSurname());
    }
}
