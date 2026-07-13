package com.joaogabriel.dev.biblioteca.controller.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.joaogabriel.dev.biblioteca.service.global.MethodArgumentNotValidException;
import com.joaogabriel.dev.biblioteca.service.global.ObjectNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> nullArgumentsException(IllegalArgumentException exc){
        Map<String, Object> message = new HashMap<>();
        message.put("error", "Campos nulos");
        message.put("status", 400);
        message.put("message", exc.getMessage());
        message.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> nullArgumentsException(MethodArgumentNotValidException exc){
        Map<String, Object> message = new HashMap<>();
        message.put("error", "Campos vazios");
        message.put("status", 422);
        message.put("message", exc.getMessageError());
        message.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.unprocessableContent().body(message);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> objectNotFoundException(ObjectNotFoundException exc, HttpServletRequest request){
        Map<String, Object> message = new HashMap<>();
        message.put("error", "Objeto não encontrado.");
        message.put("status", 404);
        message.put("message", exc.getMessage());
        message.put("path", request.getRequestURI());
        message.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(404).body(message);
    }
}
