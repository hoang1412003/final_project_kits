package com.example.library_springboot.controllers;

import com.example.library_springboot.dtos.OrderDetailDTO;
import com.example.library_springboot.models.OrderDetail;
import com.example.library_springboot.responses.ApiResponse;
import com.example.library_springboot.responses.OrderDetailResponse;
import com.example.library_springboot.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // allow CORS for this controller
@RequestMapping("/api/orderDetail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderDetail(@PathVariable("id") Long id) {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(orderDetail)
                .message("Success")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") Long orderId) {
        List<OrderDetailResponse> orderDetails=orderDetailService.findByOrderId(orderId);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(orderDetails)
                .message("Success")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result) throws Exception {
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
        OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(orderDetailResponse)
                .message("Created Successfully")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOrderDetail(@PathVariable("id") Long id, @Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result) {
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
        OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(orderDetailResponse)
                .message("Updated Successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrderDetail(@PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(id)
                .message("Deleted Successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
