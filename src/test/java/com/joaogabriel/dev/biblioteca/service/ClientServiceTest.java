package com.joaogabriel.dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.repository.ClientRepository;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    MailService mailService;

    @InjectMocks
    ClientService clientService;

    @Test
    public void insert_user_return_user(){
        ClientRequest request = new ClientRequest("test", "test@email", "9087645324", "82329279094", "¨Tatakae, Eren");

        Client clientSave = new Client(1L, request.nome(), request.email(),
            request.telefone(), request.cpf(), request.endereco());
        when(clientRepository.save(any(Client.class))).thenReturn(clientSave);
        ClientResponse response = clientService.save(request);

        assertEquals(request.nome(), response.nome());
    }

    @Test
    public void findById_return_client() {
        Client client = new Client(1L, "teste", "teste@email.com",
            "9087645324", "82329279094", "Rua 11, Bairro Centro");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        ClientResponse response = clientService.getById(1L);

       assertEquals(client.getId(), response.id());
       assertEquals(client.getEmail(), response.email());
    }
}
