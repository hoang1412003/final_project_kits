package com.example.library_springboot.repositories;

import com.example.library_springboot.models.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
