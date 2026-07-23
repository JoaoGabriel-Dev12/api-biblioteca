package com.joaogabriel.dev.biblioteca.service.global;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(Object id){
        super("Objeto não foi encontrado. Id: " +id);
    }

    public ObjectNotFoundException(String msg){
        super(msg);
    }

    // Mensagem padrão
    public ObjectNotFoundException(){
        super("Objeto não foi encontrado");
    }
}
