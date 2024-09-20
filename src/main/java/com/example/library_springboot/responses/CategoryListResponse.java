package com.example.library_springboot.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryListResponse {
    private List<CategoryResponse> categoryResponseList;
    private int totalPages;
}
