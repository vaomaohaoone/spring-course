package ru.otus.spring.course.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.data.BookAuthorLink;
import ru.otus.spring.course.data.BookStyleLink;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;
import ru.otus.spring.course.repository.StyleRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class BookHandler {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final StyleRepository styleRepository;
    private final AuthorRepository authorRepository;

    public Mono<ServerResponse> getAllBooks(ServerRequest servletRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll(), Book.class);
    }

    public Mono<ServerResponse> saveBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Book.class).flatMap(bookRepository::save), Book.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findById(serverRequest.pathVariable("isbn")), Book.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findById(serverRequest.pathVariable("isbn"))
                        .map(book -> {
                            for (Author author : book.getAuthors()) {
                                author.getBooks().remove(book);
                                authorRepository.save(author).subscribe();
                            }
                            for (Style style : book.getStyles()) {
                                style.getBooks().remove(book);
                                styleRepository.save(style).subscribe();
                            }
                            commentRepository.deleteCommentByBook_Id(book.getId()).subscribe();
                            return book;
                        }).and(bookRepository.deleteById(serverRequest.pathVariable("isbn"))), Void.class);
    }

    public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(serverRequest.bodyToMono(Book.class).flatMap(bookRepository::save), Book.class);
    }

    public Mono<ServerResponse> linkAuthorAndBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                serverRequest.bodyToMono(BookAuthorLink.class).doOnNext(bookAuthorLink -> {
                    authorRepository.findById(bookAuthorLink.getAuthorId())
                            .zipWith(bookRepository.findById(bookAuthorLink.getIsbn()))
                            .doOnNext(objects -> {
                                objects.getT2().getAuthors().add(objects.getT1());
                                objects.getT1().getBooks().add(objects.getT2());
                                authorRepository.save(objects.getT1()).subscribe();
                                bookRepository.save(objects.getT2()).subscribe();
                            }).subscribe();
                }).map(bookAuthorLink -> true), Boolean.class);
    }

    public Mono<ServerResponse> unlinkAuthorAndBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                serverRequest.bodyToMono(BookAuthorLink.class).doOnNext(bookAuthorLink -> {
                    authorRepository.findById(bookAuthorLink.getAuthorId())
                            .zipWith(bookRepository.findById(bookAuthorLink.getIsbn()))
                            .doOnNext(objects -> {
                                objects.getT2().getAuthors().remove(objects.getT1());
                                objects.getT1().getBooks().remove(objects.getT2());
                                authorRepository.save(objects.getT1()).subscribe();
                                bookRepository.save(objects.getT2()).subscribe();
                            }).subscribe();
                }).map(bookAuthorLink -> true), Boolean.class);
    }

    public Mono<ServerResponse> linkStyleAndBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                serverRequest.bodyToMono(BookStyleLink.class).doOnNext(bookStyleLink -> {
                    styleRepository.findStylesByStyle(bookStyleLink.getStyle()).next()
                            .zipWith(bookRepository.findById(bookStyleLink.getIsbn()))
                            .doOnNext(objects -> {
                                objects.getT2().getStyles().add(objects.getT1());
                                objects.getT1().getBooks().add(objects.getT2());
                                styleRepository.save(objects.getT1()).subscribe();
                                bookRepository.save(objects.getT2()).subscribe();
                            }).subscribe();
                }).map(bookAuthorLink -> true), Boolean.class);
    }

    public Mono<ServerResponse> unlinkStyleAndBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                serverRequest.bodyToMono(BookStyleLink.class).doOnNext(bookStyleLink -> {
                    styleRepository.findStylesByStyle(bookStyleLink.getStyle()).next()
                            .zipWith(bookRepository.findById(bookStyleLink.getIsbn()))
                            .doOnNext(objects -> {
                                objects.getT2().getStyles().remove(objects.getT1());
                                objects.getT1().getBooks().remove(objects.getT2());
                                styleRepository.save(objects.getT1()).subscribe();
                                bookRepository.save(objects.getT2()).subscribe();
                            }).subscribe();
                }).map(bookAuthorLink -> true), Boolean.class);
    }

    public Mono<ServerResponse> getAllAuthorsOfBook(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                bookRepository.findById(serverRequest.pathVariable("isbn")).map(Book::getAuthors),
                Author.class
        );
    }

    public Mono<ServerResponse> getBooksWhereAuthorsEquals(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                bookRepository.findBooksWhereAuthorsEquals(Integer.valueOf(serverRequest.pathVariable("count"))), Book.class
        );
    }

    public Mono<ServerResponse> getBooksByPublishedYearGreaterThan(ServerRequest serverRequest) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(
                bookRepository.findByPublishedYearGreaterThan(Integer.valueOf(serverRequest.pathVariable("year"))), Book.class
        );
    }


}
