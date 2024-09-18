package com.example.library_springboot.models;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "book_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
