package com.example.orderservice.service;

import blackfriday.protobuf.EdaMessage;
import com.example.orderservice.dto.DecreaseStockCountDTO;
import com.example.orderservice.entity.ProductOrder;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.feign.CatalogClient;
import com.example.orderservice.repository.OrderRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(EventListener.class)
class EventListenerTest {
    @MockitoSpyBean
    OrderRepository orderRepository;

    @MockitoBean
    CatalogClient catalogClient;

    @MockitoBean
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    EventListener eventListener;

    @Test
    void consumePaymentResultTest() throws InvalidProtocolBufferException {
        //given
        Long productId = 111L;
        Long paymentId = 222L;

        ProductOrder productOrder = new ProductOrder(
                1L,
                productId,
                1L,
                OrderStatus.INITIATED,
                null,
                null,
                "경기도 수원시"
        );
        ProductOrder order = orderRepository.save(productOrder);

        EdaMessage.PaymentResultV1 paymentResultMessage = EdaMessage.PaymentResultV1.newBuilder()
                .setOrderId(order.getId())
                .setPaymentId(paymentId)
                .setPaymentStatus("COMPLETED")
                .build();

        Map<String, Object> catalogResponse = new HashMap<>();
        catalogResponse.put("name", "Hello TV");

        Mockito.when(catalogClient.getProduct(productId)).thenReturn(catalogResponse);

        //when
        eventListener.consumePaymentResult(paymentResultMessage.toByteArray());

        // then
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(
                ArgumentMatchers.eq("delivery_request"),
                ArgumentMatchers.any(byte[].class)
        );
        assertEquals(paymentId, order.getPaymentId());
    }

    @Captor
    ArgumentCaptor<DecreaseStockCountDTO> captor;

    @Test
    void consumeDeliveryStatusUpdateTest() throws InvalidProtocolBufferException {
        //given
        Long productId = 111L;
        Long deliveryId = 333L;
        Long productCount = 10L;

        ProductOrder productOrder = new ProductOrder(
                1L,
                productId,
                productCount,
                OrderStatus.INITIATED,
                null,
                null,
                "경기도 수원시"
        );
        ProductOrder order = orderRepository.save(productOrder);

        EdaMessage.DeliveryStatusUpdateV1 deliveryStatusUpdateMessage = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(order.getId())
                .setDeliveryStatus("REQUESTED")
                .setDeliveryId(deliveryId)
                .build();

        //when
        eventListener.consumeDeliveryStatusUpdate(deliveryStatusUpdateMessage.toByteArray());

        //then
        assertEquals(deliveryId, order.getDeliveryId());
        Mockito.verify(catalogClient, Mockito.times(1)).decreaseStockCount(
                ArgumentMatchers.eq(productId),
                captor.capture()
        );

        assertEquals(productCount, captor.getValue().decreaseCount());
    }

    @Test
    void consumeDeliveryStatusUpdateTest_not_REQUESTED() throws InvalidProtocolBufferException {
        //given
        EdaMessage.DeliveryStatusUpdateV1 deliveryStatusUpdateMessage = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(1L)
                .setDeliveryStatus("IN_DELIVERY")
                .setDeliveryId(10L)
                .build();

        //when
        eventListener.consumeDeliveryStatusUpdate(deliveryStatusUpdateMessage.toByteArray());

        //then
        Mockito.verify(orderRepository, Mockito.times(0)).save(ArgumentMatchers.any());
        Mockito.verify(catalogClient, Mockito.times(0)).decreaseStockCount(ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}