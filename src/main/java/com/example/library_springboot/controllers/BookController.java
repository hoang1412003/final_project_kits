package com.example.library_springboot.controllers;

import com.example.library_springboot.dtos.BookDTO;
import com.example.library_springboot.responses.ApiResponse;
import com.example.library_springboot.responses.BookListResponse;
import com.example.library_springboot.responses.BookResponse;
import com.example.library_springboot.services.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookResponse> bookResponses = bookService.getAllBooks(pageable);
        int totalPages = bookResponses.getTotalPages();
        List<BookResponse> books = bookResponses.getContent();
        BookListResponse bookListResponse = BookListResponse.builder()
                .bookResponseList(books)
                .totalPages(totalPages)
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookListResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> createBook(@Valid @RequestBody BookDTO bookDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation Failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }

        BookResponse bookResponse = bookService.createBook(bookDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookResponse)
                .message("Book created")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation Failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }

        BookResponse bookResponse = bookService.updateBook(id, bookDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookResponse)
                .message("Book updated")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Book deleted")
                .status(HttpStatus.OK.value())
                .data(id)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
