package com.joaogabriel.dev.biblioteca.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.repository.ClientRepository;
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
        ClientResponse response = getById(id);
        Client client = new Client(response.id(), dto.nome(), dto.email(), 
        dto.telefone(), dto.cpf(), dto.endereco());
        repository.save(client);

        return toResponse(client);
    }

    @Cacheable(value = "usuarios", key = "#cpf")
    public ClientResponse getByCpf(String cpf){
        Client client = repository.findByCpf(cpf).orElseThrow(() -> new ObjectNotFoundException(
            "Cliente com CPF: " +cpf+ " não foi encontrado"
        ));
        return toResponse(client);
    }

    @CacheEvict(value = "usuarios", key = "#id")
    public void deleteById(Long id){
        repository.deleteById(id);
    }

    protected ClientResponse toResponse(Client client){
        return new ClientResponse(
            client.getId(),
            client.getNome(),
            client.getEmail(),
            client.getTelefone(),
            client.getEndereco()
        );
    }
}
