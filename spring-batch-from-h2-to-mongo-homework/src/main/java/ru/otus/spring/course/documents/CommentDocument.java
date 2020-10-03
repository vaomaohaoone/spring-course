package ru.otus.spring.course.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
@Data
@Accessors(chain = true)
public class CommentDocument {
    @Id
    private String id;
    @Field(name = "text")
    private String text;
    @Field(name = "book")
    @DBRef(lazy = true)
    @JsonIgnoreProperties(value = {"authors", "target"})
    private BookDocument book;
}