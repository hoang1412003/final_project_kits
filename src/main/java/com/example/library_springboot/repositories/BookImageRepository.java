package com.example.library_springboot.repositories;

import com.example.library_springboot.models.BookImage;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
    List<BookImage> findByBookId(Long bookId);

}
