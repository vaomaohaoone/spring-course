package ru.otus.spring.course.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.course.handler.BookHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRouter {

    @Bean
    public RouterFunction<ServerResponse> bookRoute(BookHandler handler) {
        return route()
                .GET(Endpoints.BOOKS, handler::getAllBooks)
                .GET(Endpoints.BOOKS + "/{isbn}", handler::getById)
                .POST(Endpoints.BOOKS, handler::saveBook)
                .DELETE(Endpoints.BOOKS + "/{isbn}", handler::deleteById)
                .PUT(Endpoints.BOOKS + "/{isbn}", handler::updateBook)
                .POST(Endpoints.BOOKS + "/author/link", handler::linkAuthorAndBook)
                .DELETE(Endpoints.BOOKS + "/author/unlink", handler::unlinkAuthorAndBook)
                .POST(Endpoints.BOOKS + "/style/link", handler::linkStyleAndBook)
                .DELETE(Endpoints.BOOKS + "/style/unlink", handler::unlinkStyleAndBook)
                .GET(Endpoints.BOOKS + "/isbn/{isbn}/authors/list", handler::getAllAuthorsOfBook)
                .GET(Endpoints.BOOKS + "/authors/count/{count}/list", handler::getBooksWhereAuthorsEquals)
                .GET(Endpoints.BOOKS + "/after/year/{year}/list", handler::getBooksByPublishedYearGreaterThan)
                .build();
    }
}
