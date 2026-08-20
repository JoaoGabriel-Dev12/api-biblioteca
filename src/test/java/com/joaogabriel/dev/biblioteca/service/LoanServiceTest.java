package com.joaogabriel.dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joaogabriel.dev.biblioteca.dtos.BookResponse;
import com.joaogabriel.dev.biblioteca.dtos.ClientResponse;
import com.joaogabriel.dev.biblioteca.dtos.LoanRequest;
import com.joaogabriel.dev.biblioteca.dtos.LoanResponse;
import com.joaogabriel.dev.biblioteca.model.Book;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.model.Loan;
import com.joaogabriel.dev.biblioteca.model.enums.BookStatus;
import com.joaogabriel.dev.biblioteca.model.enums.LoanStatus;
import com.joaogabriel.dev.biblioteca.repository.LoanRepository;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    @Mock
    LoanRepository loanRepository;

    @Mock
    MailService mailService;

    @Mock
    BookService bookService;

    @Mock
    ClientService clientService;

    @InjectMocks
    LoanService loanService;

    @Test
    public void createLoan_return_loan(){
        LoanRequest request = new LoanRequest(1L, 1L);
        Client client = new Client(1L, "teste", "teste@email.com",
            "9087645324", "82329279094", "Rua 11, Bairro Centro");
        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.LIVRE);

        Loan loan = new Loan(1L, client, book, LoanStatus.ACTIVE);
        loan.setLoanDate(OffsetDateTime.now());
        loan.setDueDate(OffsetDateTime.now().plusDays(7));

        ClientResponse clientResponse = new ClientResponse(client.getId(), client.getNome(),
            client.getEmail(), client.getTelefone(), client.getEndereco());

        BookResponse bookResponse = new BookResponse(book.getId(), book.getTitulo(),
            book.getDescricao(), book.getCodigo(), book.getAutor(), book.getAnoLancamento(),
            book.getStatus());

        when(clientService.findEntity(request.idClient())).thenReturn(client);
        when(bookService.findEntity(request.idBook())).thenReturn(book);
        when(clientService.toResponse(client)).thenReturn(clientResponse);
        when(bookService.toResponse(book)).thenReturn(bookResponse);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanResponse response = loanService.loan(request);

        assertEquals(loan.getId(), response.id());
        assertEquals(loan.getClient().getId(), response.client().id());
        assertEquals(loan.getBook().getId(), response.book().id());
    }
}
