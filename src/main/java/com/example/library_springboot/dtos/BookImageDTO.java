package com.example.library_springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookImageDTO {
    @JsonProperty("book_id")
    @Min(value = 1, message = "Id của book phải lớn hơn 0")
    private Long bookId;

    @Size(min = 5, max = 200, message = "Tên của hình ảnh phải từ 5 kí tự, không quá hơn 200 kí tự")
    @JsonProperty("image_url")
    private String imageUrl;
}
