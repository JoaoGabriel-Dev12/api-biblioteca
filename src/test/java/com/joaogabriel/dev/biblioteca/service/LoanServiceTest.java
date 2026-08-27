package com.joaogabriel.dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Test
    public void returnBook_not_return(){
        Long id = 1L;

        Client client = new Client(1L, "teste", "teste@email.com",
            "9087645324", "82329279094", "Rua 11, Bairro Centro");
        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.EMPRESTADO);

        Loan loan = new Loan(id, client, book, LoanStatus.ACTIVE);

        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loanService.returnBook(id);

        ArgumentCaptor<Loan> captor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(captor.capture());

        Loan loanUpdate = captor.getValue();
        assertEquals(LoanStatus.RETURNED, loanUpdate.getStatus());
    }

    @Test
    public void findAll_return_loans(){
        Client client = new Client(1L, "teste", "teste@email.com",
            "9087645324", "82329279094", "Rua 11, Bairro Centro");
        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.EMPRESTADO);

        List<Loan> loans = new ArrayList<>();
        Loan loan = new Loan(1L, client, book, LoanStatus.ACTIVE);
        loan.setLoanDate(OffsetDateTime.now());
        loan.setDueDate(OffsetDateTime.now().plusDays(7));
        loans.add(loan);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Loan> page = new PageImpl<>(loans, pageable, loans.size());

        when(loanRepository.findAll(pageable)).thenReturn(page);

        List<LoanResponse> listResponse = loanService.getAll(pageable).getContent();

        assertEquals(loans.size(), listResponse.size());
    }

    @Test
    public void findAllByClient_return_loans(){
        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.EMPRESTADO);

        Client client = new Client(1L, "teste", "teste@email.com",
            "9087645324", "82329279094", "Rua 11, Bairro Centro");

        List<Loan> loans = new ArrayList<>();
        Loan loan = new Loan(1L, client, book, LoanStatus.ACTIVE);
        loan.setLoanDate(OffsetDateTime.now());
        loan.setDueDate(OffsetDateTime.now().plusDays(7));
        loans.add(loan);

        ClientResponse clientResponse = new ClientResponse(client.getId(), client.getNome(),
            client.getEmail(), client.getTelefone(), client.getEndereco());

        BookResponse bookResponse = new BookResponse(book.getId(), book.getTitulo(),
            book.getDescricao(), book.getCodigo(), book.getAutor(), book.getAnoLancamento(),
            book.getStatus());

        when(clientService.findEntity(client.getId())).thenReturn(client);
        when(clientService.toResponse(client)).thenReturn(clientResponse);
        when(bookService.toResponse(book)).thenReturn(bookResponse);
        when(loanRepository.findByClient(client)).thenReturn(loans);

        List<LoanResponse> listResponse = loanService.getLoansByClient(client.getId());
        LoanResponse loanResponse = listResponse.get(0);

        assertEquals(loans.size(), listResponse.size());
        assertEquals(loan.getId(), loanResponse.id());
        assertEquals(loan.getClient().getId(), loanResponse.client().id());
        assertEquals(loan.getBook().getId(), loanResponse.book().id());
        assertEquals(loan.getStatus(), loanResponse.status());
        assertEquals(loan.getLoanDate(), loanResponse.loanDate());
        assertEquals(loan.getDueDate(), loanResponse.returnBookDate());
    }
}
