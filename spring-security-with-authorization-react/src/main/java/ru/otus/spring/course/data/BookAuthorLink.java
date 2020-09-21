package ru.otus.spring.course.data;

import lombok.Data;

@Data
public class BookAuthorLink {
    private String isbn;
    private String authorId;
}
