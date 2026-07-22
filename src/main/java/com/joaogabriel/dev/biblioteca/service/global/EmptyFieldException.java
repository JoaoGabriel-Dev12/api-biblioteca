package com.joaogabriel.dev.biblioteca.service.global;

public class EmptyFieldException extends RuntimeException{
    private String messageError;

    public EmptyFieldException(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
