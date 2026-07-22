package com.joaogabriel.dev.biblioteca.dtos;

import java.io.Serializable;

public record ClientResponse(Long id, String nome, String email, String telefone, String endereco) implements Serializable{
    
}
