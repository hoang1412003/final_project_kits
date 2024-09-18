package com.example.library_springboot.repositories;

import com.example.library_springboot.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
