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

@Document(collection = "authors")
@Data
@Accessors(chain = true)
public class Author {
    @Id
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "surname")
    private String surname;
    @Field(name = "books")
    @JsonIgnoreProperties(value = {"authors", "styles"})
    @DBRef(lazy = true)
    @EqualsAndHashCode.Exclude
    private Set<Book> books = new HashSet<>();
}
