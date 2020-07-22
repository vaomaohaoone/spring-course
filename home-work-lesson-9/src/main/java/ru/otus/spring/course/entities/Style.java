package ru.otus.spring.course.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "style")
@Entity(name = "style")
@Accessors(chain = true)
public class Style {
    @Id
    private String id;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "styles")
    private Set<Book> books = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Style)) return false;
        Style style = (Style) o;
        return getId().equals(style.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Style{" +
                "id='" + id + '\'' +
                '}';
    }
}
