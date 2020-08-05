package ru.otus.spring.course.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
