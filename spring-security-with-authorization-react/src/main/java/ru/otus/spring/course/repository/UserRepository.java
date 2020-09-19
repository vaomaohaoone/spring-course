package ru.otus.spring.course.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.course.documents.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String name);
}