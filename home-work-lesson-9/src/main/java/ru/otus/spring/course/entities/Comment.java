package ru.otus.spring.course.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity(name = "comment")
@Table(name = "comment")
@Accessors(chain = true)
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "id_book")
    private Book book;
}
