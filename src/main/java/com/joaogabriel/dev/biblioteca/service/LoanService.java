package com.joaogabriel.dev.biblioteca.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.LoanRequest;
import com.joaogabriel.dev.biblioteca.dtos.LoanResponse;
import com.joaogabriel.dev.biblioteca.model.Book;
import com.joaogabriel.dev.biblioteca.model.Client;
import com.joaogabriel.dev.biblioteca.model.Loan;
import com.joaogabriel.dev.biblioteca.model.enums.BookStatus;
import com.joaogabriel.dev.biblioteca.model.enums.LoanStatus;
import com.joaogabriel.dev.biblioteca.repository.LoanRepository;
import com.joaogabriel.dev.biblioteca.service.global.BookNotFreeException;
import com.joaogabriel.dev.biblioteca.service.global.ObjectNotFoundException;

import jakarta.transaction.Transactional;

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

    @Transactional
    public LoanResponse loan(LoanRequest dto){
        if(fieldIsNull(dto)){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        Client client = clientService.findEntity(dto.idClient());
        Book book = bookService.findEntity(dto.idBook());
        
        if(bookNotFree(book.getStatus())){throw new BookNotFreeException();}

        bookService.updateStatus(book, BookStatus.EMPRESTADO);

        Loan loan = new Loan(null, client, book, LoanStatus.ACTIVE);
        loan.setLoanDate(OffsetDateTime.now());
        loan.setReturnBookDate(OffsetDateTime.now().plusDays(7));

        repository.save(loan);
        return toResponse(loan);
    }

    @Transactional
    @CacheEvict(value = "loans", key = "#id")
    public void returnBook(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id deve ser válido");
        }

        LoanResponse loanResponse = getById(id);
        Loan loan = new Loan(loanResponse.id(), loanResponse.client(), 
        loanResponse.book(), loanResponse.status());
        loan.setLoanDate(loanResponse.loanDate());
        loan.setReturnBookDate(loanResponse.returnBookDate());

        Book book = loan.getBook();

        loan.setStatus(LoanStatus.RETURNED);
        bookService.updateStatus(book, BookStatus.LIVRE);
        repository.save(loan);

    }

    public Page<LoanResponse> getAll(Pageable pageable){
        return repository.findAll(pageable).map(this::toResponse);
    }

    public List<LoanResponse> getLoansByClient(Long idClient){
        Client client = clientService.findEntity(idClient);
        return repository.findByClient(client).stream()
        .map(this::toResponse).toList();
    }

    @Cacheable(value = "loans", key = "#id")
    public LoanResponse getById(Long id){
        Loan loan = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
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