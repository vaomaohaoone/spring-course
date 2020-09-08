package ru.otus.spring.course.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "styles")
@Data
@Accessors(chain = true)
public class Style {
    @Id
    private String id;
    @Field(name = "style")
    private String style;
    @Field(name = "books")
    @DBRef(lazy = true)
    @JsonIgnoreProperties(value = {"styles", "authors"})
    @EqualsAndHashCode.Exclude
    private Set<Book> books = new HashSet<>();
}
