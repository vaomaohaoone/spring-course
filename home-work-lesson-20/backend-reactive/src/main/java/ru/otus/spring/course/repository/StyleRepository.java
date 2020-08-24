package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.course.documents.Style;

import java.util.Set;

public interface StyleRepository extends MongoRepository<Style, String> {
    Flux<Style> findStylesByStyle(String style);
}
