package com.joaogabriel.dev.biblioteca.service.global;

public class MethodArgumentNotValidException extends RuntimeException{
    private String messageError;

    public MethodArgumentNotValidException(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
