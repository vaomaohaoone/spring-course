package ru.otus.spring.course.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.entities.Author;

import java.util.List;
import java.util.UUID;

@Transactional
public interface AuthorRepositoryH2 extends JpaRepository<Author, UUID> {
    @Query("select a from author a left join a.books where a.books.size = 0")
    List<Author> findAuthorsWithoutBooks();

}
