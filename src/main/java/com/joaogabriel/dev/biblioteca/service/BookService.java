package com.joaogabriel.dev.biblioteca.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.dtos.BookRequest;
import com.joaogabriel.dev.biblioteca.dtos.BookResponse;
import com.joaogabriel.dev.biblioteca.model.Book;
import com.joaogabriel.dev.biblioteca.model.enums.BookStatus;
import com.joaogabriel.dev.biblioteca.repository.BookRepository;
import com.joaogabriel.dev.biblioteca.service.global.MethodArgumentNotValidException;
import com.joaogabriel.dev.biblioteca.service.global.ObjectNotFoundException;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository){
        this.repository = repository;
    }

    public BookResponse save(BookRequest dto){
        if(fieldIsNull(dto)){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        if (fieldIsBlank(dto)) {
            throw new MethodArgumentNotValidException("Campos vazios no corpo da requisição");
        }

        Book book = repository.save(new Book(null, dto.titulo(), dto.descricao(), dto.codigo(),
                dto.autor(), dto.anoLancamento(), BookStatus.LIVRE));
        
        return toResponse(book);
    }

    @Cacheable(value = "books", key = "#id")
    public BookResponse getById(Long id){
        Book book = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
        return toResponse(book);
    }

    public Page<BookResponse> getAll(Pageable pageable){
        return repository.findAll(pageable).map(this::toResponse);
    }

    @CachePut(value = "books", key = "#id")
    public BookResponse update(Long id, BookRequest dto){
        if(fieldIsNull(dto) || id == null){
            throw new IllegalArgumentException("Campos inválidos no corpo da requisição");
        }

        if (fieldIsBlank(dto)) {
            throw new MethodArgumentNotValidException("Campos vazios no corpo da requisição");
        }

        BookResponse response = getById(id);
        Book book = new Book(response.id(), response.titulo(), 
        response.descricao(), response.codigo(), response.autor(), 
        response.anoLancamento(), response.status());
        repository.save(book);

        return toResponse(book);
    }

    @CacheEvict(value = "books", key = "#book.id")
    public void updateStatus(Book book, BookStatus status){
        book.setStatus(status);
        repository.save(book);
    }

    protected Book findEntity(Long id){
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
    }

    private BookResponse toResponse(Book book){
        return new BookResponse(book.getId(), book.getTitulo(), book.getDescricao(), 
        book.getCodigo(), book.getAutor(), book.getAnoLancamento(), book.getStatus());
    }

    private boolean fieldIsNull(BookRequest dto){
        return dto.titulo() == null || dto.descricao() == null || dto.codigo() == null 
        || dto.autor() == null || dto.anoLancamento() == null;
    }

    private boolean fieldIsBlank(BookRequest dto){
        return dto.titulo().isBlank() || dto.descricao().isBlank() || dto.codigo().isBlank() 
        || dto.autor().isBlank();
    }
}
