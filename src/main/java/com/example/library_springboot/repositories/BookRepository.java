package com.example.library_springboot.repositories;

import com.example.library_springboot.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
    Page<Book> findAllByCategoryId(Integer categoryId, Pageable pageable);
    List<Book> findByCategoryId(Integer categoryId);
}
