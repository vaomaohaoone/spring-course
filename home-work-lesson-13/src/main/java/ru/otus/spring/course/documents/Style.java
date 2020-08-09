package ru.otus.spring.course.documents;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "styles")
@Data
@Accessors(chain = true)
public class Style {
    @Id
    private String id;
    private String style;
    @DBRef(lazy = true)
    private Set<Book> books = new HashSet<>();
}
