package com.example.library_springboot.services;

import com.example.library_springboot.dtos.BookDTO;
import com.example.library_springboot.models.Book;
import com.example.library_springboot.models.Category;
import com.example.library_springboot.repositories.BookRepository;
import com.example.library_springboot.repositories.CategoryRepository;
import com.example.library_springboot.responses.BaseResponses;
import com.example.library_springboot.responses.BookResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public Book findBookById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {

        return bookRepository.findAll(pageable).map( book -> {
            return BookResponse.fromBook(book);
        });

    }

    public BookResponse createBook(BookDTO bookDTO) {
        Category category = categoryRepository.findById(bookDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Book book = Book.builder()
                .bookTitle(bookDTO.getBookTitle())
                .authorName(bookDTO.getAuthorName())
                .category(category)
                .bookDescription(bookDTO.getBookDescription())
                .build();

        return BookResponse.fromBook(bookRepository.save(book));
    }

    @Override
    public BookResponse updateBook(Long id, BookDTO bookDTO) {
        Book book = findBookById(id);
        if(book == null) {
            throw new RuntimeException("Book not found");
        }
        book.setBookTitle(bookDTO.getBookTitle());
        book.setAuthorName(bookDTO.getAuthorName());
        book.setBookDescription(bookDTO.getBookDescription());
        Category category = categoryRepository.findById(bookDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        book.setCategory(category);
        Book updatedBook = bookRepository.save(book);

        return BookResponse.fromBook(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = findBookById(id);
        if(book == null) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}
