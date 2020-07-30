package ru.otus.spring.course.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "style")
@Entity(name = "style")
@Accessors(chain = true)
public class Style {
    @Id
    private String id;
    @ManyToMany(mappedBy = "styles")
    private Set<Book> books = new HashSet<>();
}
