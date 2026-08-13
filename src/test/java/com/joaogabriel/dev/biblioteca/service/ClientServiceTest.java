package com.joaogabriel.dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.joaogabriel.dev.biblioteca.dtos.ClientRequest;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.repository.ClientRepository;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void findAll_return_list_clients(){
        Client client = new Client(1L, "teste", "teste@email.com",
            "9087645324", "82329279094", "Rua 11, Bairro Centro");

        List<Client> list = new ArrayList<>();
        list.add(client);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Client> page = new PageImpl<>(list, pageable, list.size());

        when(clientRepository.findAll(pageable)).thenReturn(page);

        List<ClientResponse> result = clientService.getAll(pageable).getContent();

        assertEquals(list.size(), result.size());
    }

    @Test
    public void updateClient_return_client_updated(){
        ClientRequest request = new ClientRequest("test", "test@email", "9087645324", "82329279094", "¨Tatakae, Eren");
        Client clientExits = new Client(1L, "lastNome", "lastEmail",
            "90874367123", "58659798090", "lastEndereco");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientExits));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientResponse clientResponseUpdate = clientService.update(1L, request);

        assertEquals(clientExits.getId(), clientResponseUpdate.id());
        assertEquals(request.email(), clientResponseUpdate.email());
    }

    @Test
    public void removeClientById_success(){
        long clientId = 1L;

        doNothing().when(clientRepository).deleteById(clientId);
        clientService.deleteById(clientId);

        verify(clientRepository).deleteById(clientId);
    }
}
