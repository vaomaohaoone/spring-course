package ru.otus.spring.course.documents;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
@Data
@Accessors(chain = true)
public class Role {
    @Id
    private String id;
    @Field(name = "name")
    private String name;
}
