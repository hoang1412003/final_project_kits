package com.example.library_springboot.services;

import com.example.library_springboot.dtos.BookDTO;
import com.example.library_springboot.dtos.BookImageDTO;
import com.example.library_springboot.exceptions.ResourceNotFoundException;
import com.example.library_springboot.models.Book;
import com.example.library_springboot.models.BookImage;
import com.example.library_springboot.models.Category;
import com.example.library_springboot.models.OrderDetail;
import com.example.library_springboot.repositories.BookImageRepository;
import com.example.library_springboot.repositories.BookRepository;
import com.example.library_springboot.repositories.CategoryRepository;
import com.example.library_springboot.repositories.OrderDetailRepository;
import com.example.library_springboot.responses.BaseResponses;
import com.example.library_springboot.responses.BookResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookImageRepository bookImageRepository;
    private final OrderDetailRepository orderDetailRepository;
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
        List<BookImage> bookImages = bookImageRepository.findByBookId(book.getId());
        for (BookImage bookImage : bookImages) {
            bookImageRepository.delete(bookImage);
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByBookId(id);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailRepository.delete(orderDetail);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookResponse> findBooksByCategoryId(Integer categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable).map(BookResponse::fromBook);
    }

    @Override
    public BookImage createBookImage(Long id, BookImageDTO bookImageDTO) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        BookImage bookImage = BookImage.builder()
                .book(existingBook)
                .imageUrl(bookImageDTO.getImageUrl())
                .build();
        int size = bookImageRepository.findByBookId(existingBook.getId()).size();
        if(size >=4) {
            throw new InvalidParameterException("Mỗi sinh viên ch tối da 4 hình");
        }
        return bookImageRepository.save(bookImage);
    }

    @Override
    public List<BookImage> getAllBookImagesByBookId(Long bookId) {
        return bookImageRepository.findByBookId(bookId);
    }

//    @Override
//    public void deleteBookImage(Long id) {
//        BookImage bookImage = bookImageRepository.findById(id).orElseThrow(() -> new RuntimeException("BookImage not found"));
//        bookImageRepository.delete(bookImage);
//    }
@Override
public void deleteBookImage(Long id) {
    BookImage bookImage = bookImageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("BookImage not found"));

    // Xác định đường dẫn tệp hình ảnh
    String imagePath = "upload/" + bookImage.getImageUrl(); // Hoặc đường dẫn tương ứng với cách bạn lưu trữ

    try {
        // Xóa tệp hình ảnh từ hệ thống tệp
        Files.deleteIfExists(Paths.get(imagePath));
    } catch (IOException e) {
        throw new RuntimeException("Could not delete the image file: " + e.getMessage());
    }

    // Xóa bản ghi trong cơ sở dữ liệu
    bookImageRepository.delete(bookImage);
}

}
