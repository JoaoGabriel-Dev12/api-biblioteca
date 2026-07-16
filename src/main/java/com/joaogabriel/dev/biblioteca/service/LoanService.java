package com.joaogabriel.dev.biblioteca.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.BookRequest;
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
import com.joaogabriel.dev.biblioteca.service.global.BookNotFreeException;

@Service
public class LoanService {
    private final LoanRepository repository;
    private final ClientService clientService;
    private final BookService bookService;

    public LoanService(LoanRepository repository, ClientService clientService, BookService bookService){
        this.repository = repository;
        this.clientService = clientService;
        this.bookService = bookService;
    }

    public LoanResponse loan(LoanRequest dto){
        if(fieldIsNull(dto)){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        ClientResponse clientResponse = clientService.getById(dto.idClient());
        BookResponse bookResponse = bookService.getById(dto.idBook());
        
        if(bookNotFree(bookResponse.status())){throw new BookNotFreeException();}

        Client client = new Client(clientResponse.id(), clientResponse.nome(), clientResponse.email(), 
        clientResponse.telefone(), clientResponse.cpf(), clientResponse.endereco());
        
        Book book = new Book(bookResponse.id(), bookResponse.titulo(), 
        bookResponse.descricao(), bookResponse.codigo(), bookResponse.autor(), 
        bookResponse.anoLancamento(), bookResponse.status());
        

        bookService.updateStatus(book, BookStatus.EMPRESTADO);

        Loan loan = new Loan(null, client, book, LoanStatus.ACTIVE);
        loan.setLoanDate(OffsetDateTime.now());
        loan.setReturnBookDate(OffsetDateTime.now().plusDays(7));

        repository.save(loan);
        return toResponse(loan);
    }

    private LoanResponse toResponse(Loan loan){
        return new LoanResponse(loan.getId(), loan.getClient(), loan.getBook(), 
        loan.getStatus(), loan.getLoanDate(), loan.getReturnBookDate());
    }

    private boolean fieldIsNull(LoanRequest dto){
        return dto.idClient() == null || dto.idBook() == null;
    }

    private boolean bookNotFree(BookStatus status){
        return status.equals(BookStatus.EMPRESTADO); 
    }
}
