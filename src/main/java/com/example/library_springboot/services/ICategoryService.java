package com.example.library_springboot.services;

import com.example.library_springboot.dtos.CategoryDTO;
import com.example.library_springboot.responses.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    CategoryResponse createCategory(CategoryDTO categoryDTO);
    void deleteCategory(Integer id);
    CategoryResponse updateCategory(Integer id, CategoryDTO categoryDTO);
}
