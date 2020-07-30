package ru.otus.spring.course.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity(name = "author")
@Table(name = "author")
@Accessors(chain = true)
public class Author {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
