package com.example.orderservice.service;

import com.example.orderservice.dto.ProductOrderDetailDTO;
import com.example.orderservice.dto.StartOrderResponseDTO;
import com.example.orderservice.entity.ProductOrder;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.feign.CatalogClient;
import com.example.orderservice.feign.DeliveryClient;
import com.example.orderservice.feign.PaymentClient;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(OrderService.class)
public class OrderServiceTest {
    @MockitoSpyBean
    OrderRepository orderRepository;

    @MockitoBean
    PaymentClient paymentClient;

    @MockitoBean
    DeliveryClient deliveryClient;

    @MockitoBean
    CatalogClient catalogClient;

    @Autowired
    OrderService orderService;

    @MockitoBean
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Test
    void startOrderTest() {
        //given
        Map<String, Object> paymentMethodRes = new HashMap<>();
        Map<String, Object> userAddressRes = new HashMap<>();
        paymentMethodRes.put("paymentMethodType", "CREDIT_CARD");
        userAddressRes.put("address", "경기도 성남시");

        Mockito.when(paymentClient.getPaymentMethod(1L)).thenReturn(paymentMethodRes);
        Mockito.when(deliveryClient.getUserAddress(1L)).thenReturn(userAddressRes);

        //when
        StartOrderResponseDTO startOrderResponseDTO = orderService.startOrder(1L, 1L, 2L);

        //then
        Assertions.assertNotNull(startOrderResponseDTO.orderId());
        Assertions.assertEquals(paymentMethodRes, startOrderResponseDTO.paymentMethod());
        Assertions.assertEquals(userAddressRes, startOrderResponseDTO.address());

        Optional<ProductOrder> order = orderRepository.findById(startOrderResponseDTO.orderId());
        Assertions.assertEquals(OrderStatus.INITIATED, order.get().getOrderStatus());
    }

    @Test
    void finishOrderTest() {
        //given
        ProductOrder orderStarted = new ProductOrder(
                1L,
                1L,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );
        orderRepository.save(orderStarted);

        String address = "경기도 성남시";

        Map<String, Object> catalogResponse = new HashMap<>();
        Map<String, Object> deliveryResponse = new HashMap<>();
        catalogResponse.put("price", "100");
        deliveryResponse.put("address", address);

        Mockito.when(catalogClient.getProduct(orderStarted.getProductId())).thenReturn(catalogResponse);
        Mockito.when(deliveryClient.getAddress(1L)).thenReturn(deliveryResponse);

        //when
        ProductOrder response = orderService.finishOrder(orderStarted.getId(), 1L, 1L);

        //then
        Assertions.assertEquals(address, response.getDeliveryAddress());
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(
                ArgumentMatchers.eq("payment_request"),
                ArgumentMatchers.any(byte[].class)
        );
    }

    @Test
    void getUserOrdersTest() {
        //given
        Long userId = 123L;

        ProductOrder order1 = new ProductOrder(
                userId,
                100L,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );

        ProductOrder order2 = new ProductOrder(
                userId,
                110L,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                null
        );

        orderRepository.save(order1);
        orderRepository.save(order2);

        //when
        List<ProductOrder> response = orderService.getUserOrders(userId);

        // then
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(100L, response.get(0).getProductId());
        Assertions.assertEquals(110L, response.get(1).getProductId());
    }

    @Test
    void getOrderDetailTest() {
        //given
        ProductOrder productOrder = new ProductOrder(
                1L,
                1L,
                1L,
                OrderStatus.DELIVERY_REQUESTED,
                10L,
                11L,
                null
        );

        orderRepository.save(productOrder);

        String paymentStatus = "COMPLETED";
        String deliveryStatus = "IN_DELIVERY";

        Map<String, Object> paymentResponse = new HashMap<>();
        Map<String, Object> deliveryResponse = new HashMap<>();
        paymentResponse.put("paymentStatus", paymentStatus);
        deliveryResponse.put("status", deliveryStatus);

        Mockito.when(orderRepository.findById(1000L)).thenReturn(Optional.of(productOrder));
        Mockito.when(paymentClient.getPayment(10L)).thenReturn(paymentResponse);
        Mockito.when(deliveryClient.getDelivery(11L)).thenReturn(deliveryResponse);

        // when
        ProductOrderDetailDTO response = orderService.getOrderDetail(1000L);

        // then
        Assertions.assertEquals(10L, response.paymentId());
        Assertions.assertEquals(11L, response.deliveryId());
        Assertions.assertEquals(paymentStatus, response.paymentStatus());
        Assertions.assertEquals(deliveryStatus, response.deliveryStatus());
    }
}
