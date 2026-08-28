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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.joaogabriel.dev.biblioteca.dtos.BookRequest;
import com.joaogabriel.dev.biblioteca.dtos.BookResponse;
import com.joaogabriel.dev.biblioteca.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BookResponse> save(@Valid @RequestBody BookRequest dto){
        BookResponse response = service.save(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BookResponse>> getById(@PathVariable Long id){
        BookResponse response = service.getById(id);
        EntityModel<BookResponse> model = EntityModel.of(response, linkTo(
            methodOn(BookController.class).getAll(PageRequest.of(0, 1))).withRel("books"));

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BookResponse>>> getAll(Pageable pageable){
        List<BookResponse> listBooks = service.getAll(pageable).getContent();

        List<EntityModel<BookResponse>> listModel = listBooks.stream()
                .map(b -> EntityModel.of(b, linkTo(methodOn(BookController.class).getById(b.id())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<BookResponse>> collectionModel = CollectionModel.of(listModel, linkTo(
            methodOn(BookController.class).getAll(PageRequest.ofSize(1))).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody BookRequest dto){
        BookResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}
