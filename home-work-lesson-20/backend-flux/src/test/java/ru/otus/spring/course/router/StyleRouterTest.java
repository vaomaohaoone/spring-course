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
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StyleRouterTest {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private StyleRepository styleRepository;
    @Autowired
    private RouterFunction styleRoute;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient
                .bindToRouterFunction(styleRoute)
                .build();
    }

    @Test
    void getAllStyles() {
        Style style = EntityUtils.createStyle();
        Book book = EntityUtils.createBook();
        style.getBooks().add(book);
        List<Style> styles = Arrays.asList(style);
        Flux<Style> styleFlux = Flux.fromIterable(styles);
        when(styleRepository.findAll()).thenReturn(styleFlux);

        client.get()
                .uri(Endpoints.STYLES)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Style.class)
                .isEqualTo(styles);

        verify(styleRepository).findAll();
    }

    @Test
    void getStyle() {
        Style style = EntityUtils.createStyle().setId(EntityUtils.rdmString(36));
        when(styleRepository.findById(style.getId())).thenReturn(Mono.just(style));

        client.get()
                .uri(Endpoints.STYLES + "/" + style.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Style.class)
                .isEqualTo(style);

        verify(styleRepository).findById(style.getId());
    }

    @Test
    void saveStyle() {
        Style style = EntityUtils.createStyle();
        when(styleRepository.save(style)).thenReturn(Mono.just(style));

        client.post()
                .uri(Endpoints.STYLES)
                .bodyValue(style)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Style.class)
                .isEqualTo(style);

        verify(styleRepository).save(style);
    }

    @Test
    void updateStyle() {
        Style style = EntityUtils.createStyle();
        when(styleRepository.save(style)).thenReturn(Mono.just(style));

        client.put()
                .uri(Endpoints.STYLES + "/" + style.getId())
                .bodyValue(style)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Style.class)
                .isEqualTo(style);

        verify(styleRepository).save(style);
    }

    @Test
    void deleteBook() {
        Style style = EntityUtils.createStyle().setId(EntityUtils.rdmString(36));
        Book book = EntityUtils.createBook().setId(EntityUtils.rdmString(36));

        book.getStyles().add(style);
        style.getBooks().add(book);

        when(styleRepository.findById(style.getId())).thenReturn(Mono.just(style));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(styleRepository.deleteById(style.getId())).thenReturn(Mono.empty());

        client.delete()
                .uri(Endpoints.STYLES + "/" + style.getId())
                .exchange()
                .expectStatus()
                .isOk();

        verify(styleRepository).findById(style.getId());
        verify(bookRepository).save(any(Book.class));
        verify(styleRepository).deleteById(style.getId());
    }
}
