package ru.otus.spring.course.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.StyleRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class StyleHandler {
    private final BookRepository bookRepository;
    private final StyleRepository styleRepository;

    public Mono<ServerResponse> getAllStyles(ServerRequest servletRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(styleRepository.findAll(), Style.class);
    }

    public Mono<ServerResponse> saveStyle(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Style.class).flatMap(styleRepository::save), Style.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(styleRepository.findById(serverRequest.pathVariable("id")), Style.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(styleRepository.findById(serverRequest.pathVariable("id"))
                        .map(style -> {
                            for (Book book : style.getBooks()) {
                                book.getStyles().remove(style);
                                bookRepository.save(book).subscribe();
                            }
                            return style;
                        }).and(styleRepository.deleteById(serverRequest.pathVariable("id"))), Void.class);
    }

    public Mono<ServerResponse> updateStyle(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Style.class).flatMap(styleRepository::save), Style.class);
    }
}
