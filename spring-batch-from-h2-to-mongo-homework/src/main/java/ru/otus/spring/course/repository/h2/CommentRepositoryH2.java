package ru.otus.spring.course.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.course.entities.Comment;

import java.util.UUID;

public interface CommentRepositoryH2 extends JpaRepository<Comment, UUID> {
}

