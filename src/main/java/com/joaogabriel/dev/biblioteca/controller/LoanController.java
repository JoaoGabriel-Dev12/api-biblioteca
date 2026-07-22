package com.joaogabriel.dev.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.joaogabriel.dev.biblioteca.dtos.LoanRequest;
import com.joaogabriel.dev.biblioteca.dtos.LoanResponse;
import com.joaogabriel.dev.biblioteca.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> loan(@RequestBody LoanRequest dto){
        LoanResponse response = service.loan(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(response.id()).toUri();
        
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long id){
        service.returnBook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> getAll(Pageable pageable){
        return ResponseEntity.ok(service.getAll(pageable).getContent());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<LoanResponse>> getByClientId(@PathVariable Long id){
        return ResponseEntity.ok(service.getLoansByClient(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}