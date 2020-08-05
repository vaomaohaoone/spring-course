package ru.otus.spring.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.course.entities.Style;

import java.util.UUID;

public interface StyleRepository extends JpaRepository<Style, String>{
}
