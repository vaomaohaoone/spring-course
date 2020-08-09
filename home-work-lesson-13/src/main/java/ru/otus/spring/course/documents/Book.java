package ru.otus.spring.course.documents;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "books")
@Data
@Accessors(chain = true)
public class Book {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "publishedYear")
    private Year publishedYear;

    @DBRef(lazy = true)
    private Set<Author> authors = new HashSet<>();

    @DBRef(lazy = true)
    private Set<Style> styles = new HashSet<>();
}
