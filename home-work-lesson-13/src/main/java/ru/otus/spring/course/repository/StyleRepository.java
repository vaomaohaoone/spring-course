package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.course.documents.Style;

public interface StyleRepository extends MongoRepository<Style, String> {
}
