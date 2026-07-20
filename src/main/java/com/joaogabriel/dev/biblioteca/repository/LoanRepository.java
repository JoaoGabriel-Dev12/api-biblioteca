package com.joaogabriel.dev.biblioteca.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{

    Page<Loan> findAll(Pageable pageable);

    List<Loan> findByClient(Client client);
}
