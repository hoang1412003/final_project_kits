package com.example.library_springboot.services;

import com.example.library_springboot.dtos.OrderDTO;
import com.example.library_springboot.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id)  ;
    Order updateOrder(Long id,OrderDTO orderDTO);
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
