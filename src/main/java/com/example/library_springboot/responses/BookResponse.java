package com.example.library_springboot.responses;

import com.example.library_springboot.models.Book;
import com.example.library_springboot.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookResponse extends BaseResponses{
    private Long id;

    private String bookTitle;
    private  String authorName;
    private Category category;
    private String bookDescription;

    public static BookResponse fromBook(Book book) {
        BookResponse bookResponse = BookResponse.builder()
                .id(book.getId())
                .bookTitle(book.getBookTitle())
                .authorName(book.getAuthorName())
                .category(book.getCategory())
                .bookDescription(book.getBookDescription())
                .build();
        bookResponse.setCreatedAt(book.getCreatedAt());
        bookResponse.setUpdatedAt(book.getUpdatedAt());
        return bookResponse;
    }
}
