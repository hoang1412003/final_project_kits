package com.example.library_springboot.services;

import com.example.library_springboot.dtos.OrderDetailDTO;
import com.example.library_springboot.models.Book;
import com.example.library_springboot.models.Order;
import com.example.library_springboot.models.OrderDetail;
import com.example.library_springboot.repositories.BookRepository;
import com.example.library_springboot.repositories.OrderDetailRepository;
import com.example.library_springboot.repositories.OrderRepository;
import com.example.library_springboot.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order=orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Book book=bookRepository.findById(orderDetailDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Product not found"));;
        OrderDetail orderDetail=OrderDetail.builder()
                .order(order)
                .book(book)
                .numberOfBooks(orderDetailDTO.getNumberOfBooks())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
        OrderDetail savedOrderDetail=orderDetailRepository.save(orderDetail);
        OrderDetailResponse orderDetailResponse= OrderDetailResponse.fromOrderDetail(savedOrderDetail);
        return orderDetailResponse;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws RuntimeException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order Not found"));
    }

    @Override
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws RuntimeException {
        OrderDetail existingOrderDetail=orderDetailRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order detail Not found"));
        Order existingOrder=orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new RuntimeException("Order Not found"));
        Book existingBook=bookRepository.findById(orderDetailDTO.getBookId())
                .orElseThrow(()-> new RuntimeException("Product not found"));
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setBook(existingBook);
        existingOrderDetail.setNumberOfBooks(orderDetailDTO.getNumberOfBooks());
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());

        OrderDetail savedOrderDetail=orderDetailRepository.save(existingOrderDetail);
        OrderDetailResponse orderDetailResponse= OrderDetailResponse.fromOrderDetail(savedOrderDetail);
        return  orderDetailResponse;
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long orderId) {
        List<OrderDetail> orderDetailList=orderDetailRepository.findByOrderId(orderId);

        List<OrderDetailResponse> orderDetailResponses=orderDetailList
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return orderDetailResponses;
    }
}
