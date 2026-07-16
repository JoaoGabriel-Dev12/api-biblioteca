package com.joaogabriel.dev.biblioteca.model;

import java.time.OffsetDateTime;

import com.joaogabriel.dev.biblioteca.model.enums.LoanStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan")
public class Loan {
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private OffsetDateTime loanDate;
    private OffsetDateTime returnBookDate;

    public Loan(){}

    public Loan(Long id, Client client, Book book, LoanStatus status){
        this.id = id;
        this.client = client;
        this.book = book;
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setLoanDate(OffsetDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public OffsetDateTime getLoanDate() {
        return loanDate;
    }

    public void setReturnBookDate(OffsetDateTime returnBookDate) {
        this.returnBookDate = returnBookDate;
    }

    public OffsetDateTime getReturnBookDate() {
        return returnBookDate;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }
}
