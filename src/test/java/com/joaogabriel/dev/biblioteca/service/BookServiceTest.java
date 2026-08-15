package com.joaogabriel.dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joaogabriel.dev.biblioteca.dtos.BookRequest;
import com.joaogabriel.dev.biblioteca.dtos.BookResponse;
import com.joaogabriel.dev.biblioteca.model.Book;
import com.joaogabriel.dev.biblioteca.model.enums.BookStatus;
import com.joaogabriel.dev.biblioteca.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @Test
    public void insert_book_return_book(){
        BookRequest request = new BookRequest("teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012);

        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.LIVRE);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.save(request);

        assertEquals(request.titulo(), response.titulo());
        assertEquals(request.descricao(), response.descricao());
        assertEquals(request.codigo(), response.codigo());
        assertEquals(request.autor(), response.autor());
        assertEquals(request.anoLancamento(), response.anoLancamento());
    }

    @Test
    public void findById_return_book(){
        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.LIVRE);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getById(1L);

        assertEquals(book.getId(), response.id());
        assertEquals(book.getTitulo(), response.titulo());
        assertEquals(book.getDescricao(), response.descricao());
        assertEquals(book.getCodigo(), response.codigo());
        assertEquals(book.getAutor(), response.autor());
        assertEquals(book.getAnoLancamento(), response.anoLancamento());
        assertEquals(book.getStatus(), response.status());
    }
}
