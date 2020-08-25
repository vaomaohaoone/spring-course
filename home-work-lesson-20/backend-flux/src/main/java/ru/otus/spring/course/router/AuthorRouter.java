package ru.otus.spring.course.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.course.handler.AuthorHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthorRouter {
    @Bean
    public RouterFunction<ServerResponse> authorRoute(AuthorHandler handler) {
        return route()
                .GET(Endpoints.AUTHORS, handler::getAllAuthors)
                .GET(Endpoints.AUTHORS + "/{id}", handler::getById)
                .POST(Endpoints.AUTHORS, handler::saveAuthor)
                .DELETE(Endpoints.AUTHORS + "/{id}", handler::deleteById)
                .PUT(Endpoints.AUTHORS + "/{id}", handler::updateAuthor)
                .GET(Endpoints.AUTHORS + "/id/{authorId}/books/list", handler::getAllBooksByAuthor)
                .GET(Endpoints.AUTHORS + "/books/count/{count}/list", handler::getAuthorsWhereBooksEquals)
                .GET(Endpoints.AUTHORS + "/name/{name}/list", handler::getAuthorsWithName)
                .GET(Endpoints.AUTHORS + "/surname/{surname}/list", handler::getAuthorsWithSurname)
                .build();
    }
}
