package com.joaogabriel.dev.biblioteca.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientRequest(
    @NotBlank(message = "Nome não pode estar em branco")
    String nome,

    @NotBlank(message = "Email não pode estar em branco")
    @Email(message = "Insira um email válido")
    String email,

    @NotBlank(message = "Telefone não pode estar em branco")
    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    String telefone,

    @NotBlank(message = "CPF não pode estar em branco")
    @CPF(message = "CPF inválido")
    String cpf,

    @NotBlank(message = "Endereço não pode estar em branco")
    String endereco
) {
    
}
