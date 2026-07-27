package com.joaogabriel.dev.biblioteca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookRequest(
    @NotBlank(message = "Título não pode estar em branco")
    String titulo,
    
    @NotBlank(message = "Descrição não pode estar em branco")
    String descricao,
    
    @NotBlank(message = "Código não pode estar em branco")
    String codigo, 

    @NotBlank(message = "Autor não pode estar em branco")
    String autor, 

    @NotNull(message = "Ano de lançamento não pode estar nulo")
    @Positive(message = "Ano de lançamento deve ser maior que zero")
    Integer anoLancamento
) {
}
