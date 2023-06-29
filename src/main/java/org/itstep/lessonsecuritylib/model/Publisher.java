package org.itstep.lessonsecuritylib.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itstep.lessonsecuritylib.command.PublisherCommand;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "publishers")
@NoArgsConstructor
@ToString(exclude = "books")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "publisher")
    private List<Book> books = new ArrayList<>();

    public Publisher(String name) {
        this.name = name;
    }

    public static Publisher fromCommand(PublisherCommand command) {
        return new Publisher(command.name());
    }
}
