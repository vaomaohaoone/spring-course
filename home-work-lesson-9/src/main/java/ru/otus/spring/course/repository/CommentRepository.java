package ru.otus.spring.course.repository;

import ru.otus.spring.course.entities.Comment;

import java.util.UUID;

public interface CommentRepository extends EntityRepository<Comment, UUID>{
}
