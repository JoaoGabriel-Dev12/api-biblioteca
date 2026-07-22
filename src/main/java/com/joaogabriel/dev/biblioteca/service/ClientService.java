package com.joaogabriel.dev.biblioteca.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.repository.ClientRepository;
import com.joaogabriel.dev.biblioteca.service.global.EmptyFieldException;
import com.joaogabriel.dev.biblioteca.service.global.ObjectNotFoundException;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final MailService mailService;

    public ClientService(ClientRepository repository, MailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }

    public ClientResponse save(ClientRequest dto){
        if(fieldIsNull(dto)){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        if (fieldIsEmpty(dto)) {
            throw new EmptyFieldException("Campos vazios no corpo da requisição");
        }
        
        Client client = repository.save(new Client(null, dto.nome(), dto.email(), dto.telefone(), dto.cpf(), dto.endereco()));
        ClientResponse response = toResponse(client);
        mailService.sendMailCreateAccount(response.email(), response.nome());

        return response;
    }

    @Cacheable(value = "usuarios", key = "#id")
    public ClientResponse getById(Long id){
        Client client = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
        
        ClientResponse response = toResponse(client);
        return response;
    }

    protected Client findEntity(Long id){
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
    }

    public Page<ClientResponse> getAll(Pageable pageable){
        return repository.findAll(pageable)
            .map(this::toResponse);
    }

    @CachePut(value = "usuarios", key = "#id")
    public ClientResponse update(Long id, ClientRequest dto){
        if(fieldIsNull(dto) || id == null){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        if (fieldIsEmpty(dto)) {
            throw new EmptyFieldException("Campos vazios no corpo da requisição");
        }

        ClientResponse response = getById(id);
        Client client = new Client(response.id(), dto.nome(), dto.email(), 
        dto.telefone(), dto.cpf(), dto.endereco());
        repository.save(client);

        return toResponse(client);
    }

    protected ClientResponse toResponse(Client client){
        return new ClientResponse(
            client.getId(),
            client.getNome(),
            client.getEmail(),
            client.getTelefone(),
            client.getEndereço()
        );
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
