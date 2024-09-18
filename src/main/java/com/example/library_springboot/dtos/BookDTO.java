package com.example.library_springboot.dtos;

import com.example.library_springboot.models.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @NotBlank(message = "Tilte không được trống nha")
    private String bookTitle;

    @NotBlank(message = "Author name không được trống nha")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự")
    private  String authorName;

    @JsonProperty("category_id")
    @Min(value = 1, message = "Id của category phải lớn hơn 0")
    private Integer categoryId;


    @NotBlank(message = "Book description không được trống nha")
    private String bookDescription;


}
