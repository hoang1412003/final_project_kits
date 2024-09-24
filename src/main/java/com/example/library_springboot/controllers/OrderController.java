package com.example.library_springboot.controllers;

import com.example.library_springboot.dtos.OrderDTO;
import com.example.library_springboot.models.Order;
import com.example.library_springboot.responses.ApiResponse;
import com.example.library_springboot.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000") // allow CORS for this controller
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) throws Exception {
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

        Order order = orderService.createOrder(orderDTO);

        ApiResponse apiResponse = ApiResponse.builder()
                .data(order)
                .message("Order created")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO, BindingResult result) throws Exception {
        Order order=orderService.updateOrder(id,orderDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(order)
                .message("Order updated")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long id){
        //change active
        orderService.deleteOrder(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(id)
                .message("Order deleted")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
