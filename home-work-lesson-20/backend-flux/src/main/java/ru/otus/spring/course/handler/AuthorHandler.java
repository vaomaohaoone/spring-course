package ru.otus.spring.course.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class AuthorHandler {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Mono<ServerResponse> getAllAuthors(ServerRequest servletRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAll(), Author.class);
    }

    public Mono<ServerResponse> saveAuthor(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Author.class).flatMap(authorRepository::save), Author.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findById(serverRequest.pathVariable("id")), Author.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findById(serverRequest.pathVariable("id"))
                        .map(author -> {
                            for (Book book : author.getBooks()) {
                                book.getAuthors().remove(author);
                                bookRepository.save(book).subscribe();
                            }
                            return author;
                        }).and(authorRepository.deleteById(serverRequest.pathVariable("id"))), Void.class);
    }

    public Mono<ServerResponse> updateAuthor(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Author.class).flatMap(authorRepository::save), Author.class);
    }

    public Mono<ServerResponse> getAllBooksByAuthor(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findById(serverRequest.pathVariable("authorId")).map(Author::getBooks),
                        Book.class);
    }

    public Mono<ServerResponse> getAuthorsWhereBooksEquals(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAuthorsWhereBooksEquals(Integer.valueOf(serverRequest.pathVariable("count"))),
                        Author.class
                );
    }

    public Mono<ServerResponse> getAuthorsWithName(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAuthorsByName(serverRequest.pathVariable("name")),
                        Author.class);
    }

    public Mono<ServerResponse> getAuthorsWithSurname(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAuthorsBySurname(serverRequest.pathVariable("surname")),
                        Author.class);
    }

}
