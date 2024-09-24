package com.example.library_springboot.controllers;

import com.example.library_springboot.dtos.BookDTO;
import com.example.library_springboot.dtos.BookImageDTO;
import com.example.library_springboot.models.BookImage;
import com.example.library_springboot.responses.ApiResponse;
import com.example.library_springboot.responses.BookListResponse;
import com.example.library_springboot.responses.BookResponse;
import com.example.library_springboot.services.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // allow CORS for this controller
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookResponse> bookResponses = bookService.getAllBooks(pageable);
        int totalPages = bookResponses.getTotalPages();
        List<BookResponse> books = bookResponses.getContent();
        BookListResponse bookListResponse = BookListResponse.builder()
                .bookResponseList(books)
                .totalPages(totalPages)
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookListResponse)
                .message("Get All Books successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> createBook(@Valid @RequestBody BookDTO bookDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation Failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }

        BookResponse bookResponse = bookService.createBook(bookDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookResponse)
                .message("Book created")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation Failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }

        BookResponse bookResponse = bookService.updateBook(id, bookDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookResponse)
                .message("Book updated")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Book deleted")
                .status(HttpStatus.OK.value())
                .data(id)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search-category")
    public ResponseEntity<ApiResponse> searchCategory(@RequestParam("category_id") Integer categoryId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookResponse> bookResponses = bookService.findBooksByCategoryId(categoryId, pageable);
        int totalPages = bookResponses.getTotalPages();
        List<BookResponse> books = bookResponses.getContent();
        BookListResponse bookListResponse = BookListResponse.builder()
                .bookResponseList(books)
                .totalPages(totalPages)
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookListResponse)
                .message("search category success")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/uploads/{id}")
    public ResponseEntity<ApiResponse> uploadBook(@PathVariable Long id, @ModelAttribute("files") List<MultipartFile> files) throws IOException {
        List<BookImage> bookImages = new ArrayList<>();
        int count = 0;
        for(MultipartFile file : files) {
            if(file != null) {
                if(file.getSize() == 0) {
                    count++;
                    continue;
                }
                String fileName =  storeFile(file);
                BookImageDTO bookImageDTO = BookImageDTO.builder()
                        .imageUrl(fileName)
                        .build();
                BookImage bookImage = bookService.createBookImage(id, bookImageDTO);
                bookImages.add(bookImage);
            }
        }
        if(count == 1) {
            throw new IllegalArgumentException("Files chưa chọn");
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Insert successfully")
                .data(bookImages)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString()+"_"+fileName;
        java.nio.file.Path uploadDir = Paths.get("upload");
        if(!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping("/productDetail/{id}")
    public ResponseEntity<ApiResponse> getAllBookImagesByBookId(@PathVariable Long id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookService.getAllBookImagesByBookId(id))
                .message("All book images")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("upload", imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if(resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/product-image/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id) {
        bookService.deleteBookImage(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(id)
                .message("Image deleted")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
