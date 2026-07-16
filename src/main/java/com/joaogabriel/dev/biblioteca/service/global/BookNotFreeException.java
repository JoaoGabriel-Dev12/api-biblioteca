package com.joaogabriel.dev.biblioteca.service.global;

public class BookNotFreeException extends RuntimeException{
    public BookNotFreeException(){
        super("Livro não está disponível para o empréstimo, deve esperar a devolução");
    }
}
