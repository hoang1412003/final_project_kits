package com.example.library_springboot.services;

import com.example.library_springboot.dtos.OrderDetailDTO;
import com.example.library_springboot.models.OrderDetail;
import com.example.library_springboot.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id);
    OrderDetailResponse updateOrderDetail(Long id,OrderDetailDTO orderDetailDTO);
    void deleteById(Long id) ;
    List<OrderDetailResponse> findByOrderId(Long orderId);
}
