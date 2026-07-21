package com.joaogabriel.dev.biblioteca.dtos;

import java.time.OffsetDateTime;

import com.joaogabriel.dev.biblioteca.model.Book;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.model.enums.LoanStatus;

public record LoanResponse(
    Long id, 
    Client client, 
    Book book, 
    LoanStatus status,
    boolean late,
    OffsetDateTime loanDate,
    OffsetDateTime returnBookDate
) {

}
