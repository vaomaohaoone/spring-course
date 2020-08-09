package ru.otus.spring.course.documents;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
@Data
@Accessors(chain = true)
public class Comment {
    @Id
    private String id;

    @Field(name = "text")
    private String text;
    @DBRef(lazy = true)
    private Book book;
}
