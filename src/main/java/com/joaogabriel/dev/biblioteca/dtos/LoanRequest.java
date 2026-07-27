package com.joaogabriel.dev.biblioteca.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoanRequest(

    @NotNull(message = "Id do cliente não pode ser nulo")
    @Positive(message = "Id do cliente deve ser maior que zero")
    Long idClient,

    @NotNull(message = "Id do livro não pode ser nulo")
    @Positive(message = "Id do livro deve ser maior que zero")
    Long idBook
) {
}
