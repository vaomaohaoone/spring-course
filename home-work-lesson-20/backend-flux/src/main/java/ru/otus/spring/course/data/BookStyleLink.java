package ru.otus.spring.course.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStyleLink {
    private String isbn;
    private String style;
}
