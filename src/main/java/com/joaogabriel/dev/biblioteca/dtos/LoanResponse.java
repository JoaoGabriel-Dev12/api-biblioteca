package com.joaogabriel.dev.biblioteca.dtos;

import java.time.OffsetDateTime;

import com.joaogabriel.dev.biblioteca.model.enums.LoanStatus;

public record LoanResponse(
    Long id, 
    ClientResponse client, 
    BookResponse book, 
    LoanStatus status,
    boolean late,
    OffsetDateTime loanDate,
    OffsetDateTime returnBookDate
) {

}
