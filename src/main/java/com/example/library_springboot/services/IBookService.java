package com.example.library_springboot.services;

import com.example.library_springboot.dtos.BookDTO;
import com.example.library_springboot.models.Book;
import com.example.library_springboot.responses.BaseResponses;
import com.example.library_springboot.responses.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    Book findBookById(long id);
    Page<BookResponse> getAllBooks(Pageable pageable);
    BookResponse createBook (BookDTO bookDTO);
    BookResponse updateBook (Long id, BookDTO bookDTO);
    void deleteBook (Long id);
}
