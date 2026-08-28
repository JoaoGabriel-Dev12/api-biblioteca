package com.joaogabriel.dev.biblioteca.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> loan(@Valid @RequestBody LoanRequest dto){
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
    public ResponseEntity<CollectionModel<EntityModel<LoanResponse>>> getAll(Pageable pageable){
        List<LoanResponse> list = service.getAll(pageable).getContent();
        List<EntityModel<LoanResponse>> listModels = list.stream()
                .map(l -> EntityModel.of(l, linkTo(
                    methodOn(LoanController.class).getByClientId(l.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<LoanResponse>> collectionModel = CollectionModel.of(listModels, linkTo(
            methodOn(LoanController.class).getAll(PageRequest.ofSize(1))).withSelfRel());


        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LoanResponse>> getById(@PathVariable Long id){
        LoanResponse response = service.getById(id);

        EntityModel<LoanResponse> model = EntityModel.of(response, linkTo(
            methodOn(LoanController.class).getAll(PageRequest.of(0, 1))).withRel("loans"));

        return ResponseEntity.ok(model);
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
