package com.joaogabriel.dev.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joaogabriel.dev.biblioteca.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}
