package ru.otus.spring.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.course.entities.Comment;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
