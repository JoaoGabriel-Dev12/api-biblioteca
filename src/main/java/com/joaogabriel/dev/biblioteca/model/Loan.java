package com.joaogabriel.dev.biblioteca.model;

import java.time.OffsetDateTime;

import com.joaogabriel.dev.biblioteca.model.enums.LoanStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public boolean isLate(){
        return status == LoanStatus.ACTIVE && OffsetDateTime.now().isAfter(returnBookDate);
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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Loan other = (Loan) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
