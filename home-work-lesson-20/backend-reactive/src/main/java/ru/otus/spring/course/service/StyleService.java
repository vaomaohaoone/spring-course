package ru.otus.spring.course.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.course.documents.Style;

import java.util.List;
import java.util.Optional;

public interface StyleService {
    Mono<Style> saveStyle(Style style);
    Mono<Style> getStyleById(String styleId);
    void deleteById(String styleId);
    Flux<Style> getAll();
    Mono<Style> createStyle(String style);
}
