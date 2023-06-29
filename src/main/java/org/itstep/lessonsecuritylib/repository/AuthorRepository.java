package org.itstep.lessonsecuritylib.repository;

import org.itstep.lessonsecuritylib.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
