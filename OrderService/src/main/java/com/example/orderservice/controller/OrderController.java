package com.example.orderservice.controller;

import com.example.orderservice.dto.*;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/start-order")
    public StartOrderResponseDTO startOrder(@RequestBody StartOrderDTO dto) {
        return orderService.startOrder(dto.userId(), dto.productId(), dto.count());
    }

    @PostMapping("/finish-order")
    public ProductOrderResponse finishOrder(@RequestBody FinishOrderDTO dto) {
        return new ProductOrderResponse(orderService.finishOrder(dto.orderId(), dto.paymentMethodId(), dto.addressId()));
    }

    @GetMapping("/users/{userId}/orders")
    public List<ProductOrderResponse> getUserOrders(@PathVariable("userId") Long userId) {
        return orderService.getUserOrders(userId).stream().map(ProductOrderResponse::new).toList();
    }

    @GetMapping("/orders/{orderId}")
    public ProductOrderDetailDTO getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderDetail(orderId);
    }
}
