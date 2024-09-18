package com.example.library_springboot.responses;

import com.example.library_springboot.models.Book;
import com.example.library_springboot.repositories.BookImageRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookImageResponse {
    private Long id;
    private Book book;
    private String imageUrl;

//    public BookImageRepository fromBookImageRepository(Book book) {
//
//    }
}
