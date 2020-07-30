package ru.otus.spring.course.entities;

import lombok.*;
import lombok.experimental.Accessors;
import ru.otus.spring.course.entities.converter.YearConverter;

import javax.persistence.*;
import java.time.Year;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "book")
@Entity(name = "book")
@Accessors(chain = true)
public class Book {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "publishedYear")
    @Convert(converter = YearConverter.class)
    private Year publishedYear;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "book_style",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id"))
    private Set<Style> styles = new HashSet<>();

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void addStyle(Style style) {
        this.styles.add(style);
        style.getBooks().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getId().equals(book.getId()) &&
                getName().equals(book.getName()) &&
                getPublishedYear().equals(book.getPublishedYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPublishedYear());
    }
}
