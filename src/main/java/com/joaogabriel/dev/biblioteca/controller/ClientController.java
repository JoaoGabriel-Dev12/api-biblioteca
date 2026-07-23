package com.joaogabriel.dev.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> save(@RequestBody ClientRequest dto){
        ClientResponse response = service.save(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Long id){
        ClientResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAll(Pageable pageable){
        List<ClientResponse> listClients = service.getAll(pageable).getContent();
        return ResponseEntity.ok(listClients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable Long id, @RequestBody ClientRequest dto){
        ClientResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/client/{cpf}")
    public ResponseEntity<ClientResponse> getByCpf(@PathVariable String cpf){
        ClientResponse response = service.getByCpf(cpf);
        return ResponseEntity.ok(response);
    }
}
