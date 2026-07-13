package com.joaogabriel.dev.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.repository.ClientRepository;
import com.joaogabriel.dev.biblioteca.service.global.MethodArgumentNotValidException;
import com.joaogabriel.dev.biblioteca.service.global.ObjectNotFoundException;

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

    public ClientResponse getById(Long id){
        Client client = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
        
        ClientResponse response = new ClientResponse(client.getId(), client.getNome(), client.getEmail(),
                        client.getTelefone(), client.getCpf(), client.getEndereço());
        return response;
    }

    public List<ClientResponse> getAll(){
        List<ClientResponse> listClients = repo.findAll()
            .stream().map(c -> new ClientResponse(
                c.getId(), c.getNome(), c.getEmail(), c.getTelefone(), c.getCpf(), c.getEndereço()
            )).toList();
        
        return listClients;
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
