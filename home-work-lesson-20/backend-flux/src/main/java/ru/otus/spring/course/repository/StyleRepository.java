package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.course.documents.Style;

public interface StyleRepository extends ReactiveMongoRepository<Style, String> {
    Flux<Style> findStylesByStyle(String style);
}
