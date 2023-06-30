package org.itstep.lessonsecuritylib.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itstep.lessonsecuritylib.command.BookCommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "authors")
@ToString(exclude = "authors")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private Set<Author> authors = new HashSet<>();
    private List<Author> authors = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Publisher publisher;

    public Book(String title) {
        this.title = title;
    }

    public void addAuthor(Author author) {
        author.getBooks().add(this);
        authors.add(author);
    }

    public void setPublisher(Publisher publisher) {
        publisher.getBooks().add(this);
        this.publisher = publisher;
    }

    public static Book fromCommand(BookCommand command) {
        return new Book(command.title());
    }
}


