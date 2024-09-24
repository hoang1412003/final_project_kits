package com.example.library_springboot.responses;

import com.example.library_springboot.models.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("number_of_products")
    private int numberOfBooks;

    @JsonProperty("total_money")
    private Float totalMoney;


    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getBook().getId())
                .price(orderDetail.getPrice())
                .numberOfBooks(orderDetail.getNumberOfBooks())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
