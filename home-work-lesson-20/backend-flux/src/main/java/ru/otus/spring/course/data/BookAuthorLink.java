package ru.otus.spring.course.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAuthorLink {
    private String isbn;
    private String authorId;
}
