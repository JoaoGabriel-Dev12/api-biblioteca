package com.joaogabriel.dev.biblioteca.service;

import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.repository.ClientRepository;
import com.joaogabriel.dev.biblioteca.service.global.MethodArgumentNotValidException;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public ClientResponse save(ClientRequest dto){
        if(fieldIsNull(dto)){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        if (fieldIsEmpty(dto)) {
            throw new MethodArgumentNotValidException("Campos vazios no corpo da requisição");
        }
        
        Client client = repo.save(new Client(null, dto.nome(), dto.email(), dto.telefone(), dto.cpf(), dto.endereco()));
        ClientResponse response = new ClientResponse(client.getId(), client.getNome(), client.getEmail(),
                        client.getTelefone(), client.getCpf(), client.getEndereço());
        return response;
    }

    private boolean fieldIsNull(ClientRequest dto){
        return dto.nome() == null || dto.email() == null || dto.telefone() == null || dto.cpf() == null
        || dto.endereco() == null;
    }

    private boolean fieldIsEmpty(ClientRequest dto){
        return dto.nome().isEmpty() || dto.email().isEmpty() || dto.telefone().isEmpty()
        || dto.cpf().isEmpty() || dto.endereco().isEmpty();
    }
}
