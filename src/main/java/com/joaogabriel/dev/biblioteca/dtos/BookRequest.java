package com.joaogabriel.dev.biblioteca.dtos;

public record BookRequest(String titulo, String descricao, String codigo, String autor, Integer anoLancamento) {
}
