package org.itstep.lessonsecuritylib.repository;

import org.itstep.lessonsecuritylib.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
