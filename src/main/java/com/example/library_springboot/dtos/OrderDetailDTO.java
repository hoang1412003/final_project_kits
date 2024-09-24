package com.example.library_springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value=1,message = "Order'ID must be > 0")
    private Long orderId;

    @JsonProperty("book_id")
    @Min(value=1,message = "Book'ID must be > 0")
    private Long bookId;

    private Float price;

    @JsonProperty("number_of_books")
    @Min(value=1,message = "number_of_books must be > 0")
    private int numberOfBooks;

    @JsonProperty("total_money")
    @Min(value=0,message = "total money  must be >= 0")
    private Float totalMoney;

}
