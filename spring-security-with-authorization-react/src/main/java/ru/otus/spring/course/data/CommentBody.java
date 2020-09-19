package ru.otus.spring.course.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentBody {
    private String text;
    private String isbn;
}
