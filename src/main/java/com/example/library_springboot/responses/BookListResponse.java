package com.example.library_springboot.responses;

import com.example.library_springboot.models.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookListResponse {
    private List<BookResponse> bookResponseList;
    private int totalPages;
}
