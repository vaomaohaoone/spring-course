package ru.otus.spring.course.documents;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "password")
    private String password;
    @Field
    @DBRef(lazy = true)
    private Set<Role> roles = new HashSet<>();
}
