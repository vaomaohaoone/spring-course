package ru.otus.spring.course.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.course.handler.CommentHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CommentRouter {
    @Bean
    public RouterFunction<ServerResponse> commentRoute(CommentHandler handler) {
        return route()
                .GET(Endpoints.COMMENTS, handler::getAllComments)
                .GET(Endpoints.COMMENTS + "/{id}", handler::getById)
                .POST(Endpoints.COMMENTS, handler::saveComment)
                .DELETE(Endpoints.COMMENTS + "/{id}", handler::deleteById)
                .PUT(Endpoints.COMMENTS + "/{id}", handler::updateComment)
                .POST(Endpoints.COMMENTS + "/add", handler::addCommentToBook)
                .GET(Endpoints.COMMENTS + "/isbn/{isbn}", handler::getCommentsByBookIsbn)
                .build();
    }
}
