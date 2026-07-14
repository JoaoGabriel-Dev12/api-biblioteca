package com.joaogabriel.dev.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joaogabriel.dev.biblioteca.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
