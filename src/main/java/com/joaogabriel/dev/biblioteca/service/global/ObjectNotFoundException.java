package com.joaogabriel.dev.biblioteca.service.global;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(Object id){
        super("Objeto não foi encontrado. Id: " +id);
    }
}
