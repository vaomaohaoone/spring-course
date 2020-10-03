package ru.otus.spring.course.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

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
    @Type(type="uuid-char")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @ManyToMany(mappedBy = "authors")
    @JsonIgnoreProperties(value = {"authors"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();
}