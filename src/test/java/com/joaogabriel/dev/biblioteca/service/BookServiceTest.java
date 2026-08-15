package com.joaogabriel.dev.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Test
    public void findAll_return_list_books(){
        Book book = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.LIVRE);

        List<Book> books = new ArrayList<>();
        books.add(book);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Book> page = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(page);

        List<BookResponse> listResponse = bookService.getAll(pageable).getContent();

        assertEquals(books.size(), listResponse.size());
    }

    @Test
    public void updateBook_return_book_updated(){
        BookRequest request = new BookRequest("titulo atualizado", "descricao atualizada",
            "473847GUm", "Messi", 2012);

        Book bookExists = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.LIVRE);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookExists));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        BookResponse response = bookService.update(1L, request);

        assertEquals(request.titulo(), response.titulo());
        assertEquals(request.descricao(), response.descricao());
        assertEquals(request.codigo(), response.codigo());
        assertEquals(request.autor(), response.autor());
        assertEquals(request.anoLancamento(), response.anoLancamento());
    }

    @Test
    public void updateStatus_of_book(){
        Book bookExist = new Book(1L, "teste", "leia o livro",
            "473847GUm", "Cristiano Ronaldo", 2012, BookStatus.LIVRE);
        BookStatus newStatus = BookStatus.EMPRESTADO;

        bookService.updateStatus(bookExist, newStatus);

        verify(bookRepository).save(bookExist);
        assertEquals(newStatus, bookExist.getStatus());
    }

    @Test
    public void removeBookById_sucess(){
        Long bookId = 1L;

        doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteById(bookId);

        verify(bookRepository).deleteById(bookId);
    }
}
