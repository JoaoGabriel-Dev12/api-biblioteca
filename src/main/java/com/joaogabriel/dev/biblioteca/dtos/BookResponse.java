package com.joaogabriel.dev.biblioteca.dtos;

import java.io.Serializable;

import com.joaogabriel.dev.biblioteca.model.enums.BookStatus;

public record BookResponse(Long id, String titulo, String descricao, String codigo, String autor, Integer anoLancamento, BookStatus status) implements Serializable{
}
