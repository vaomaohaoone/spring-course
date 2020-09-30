package ru.otus.spring.course.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import ru.otus.spring.course.entities.converter.YearConverter;

import javax.persistence.*;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Table(name = "book")
@Entity(name = "book")
@Accessors(chain = true)
public class Book {
    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "published_year")
    @Convert(converter = YearConverter.class)
    private Year publishedYear;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties(value = {"books"})
    private Set<Author> authors = new HashSet<>();

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

}
