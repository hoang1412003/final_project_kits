package com.example.library_springboot.services;

import com.example.library_springboot.dtos.CategoryDTO;
import com.example.library_springboot.exceptions.ResourceNotFoundException;
import com.example.library_springboot.models.Category;
import com.example.library_springboot.repositories.CategoryRepository;
import com.example.library_springboot.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(category -> CategoryResponse.fromCategory(category));
    }

    @Override
    public CategoryResponse createCategory(CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory != null) {
            throw new RuntimeException("Category with name " + categoryDTO.getName() + " already exists");
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return CategoryResponse.fromCategory(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryDTO categoryDTO) {
        Category existingCategory  = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category with id " + id + " not found")
        );
        existingCategory.setName(categoryDTO.getName());
        CategoryResponse categoryResponse = CategoryResponse.fromCategory(categoryRepository.save(existingCategory));
        return categoryResponse;
    }


}
