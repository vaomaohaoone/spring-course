package ru.otus.spring.course.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.course.handler.StyleHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StyleRouter {
    @Bean
    public RouterFunction<ServerResponse> styleRoute(StyleHandler handler) {
        return route()
                .GET(Endpoints.STYLES, handler::getAllStyles)
                .GET(Endpoints.STYLES + "/{id}", handler::getById)
                .POST(Endpoints.STYLES, handler::saveStyle)
                .DELETE(Endpoints.STYLES + "/{id}", handler::deleteById)
                .PUT(Endpoints.STYLES + "/{id}", handler::updateStyle)
                .build();
    }
}
