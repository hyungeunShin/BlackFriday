package com.example.orderservice.service;

import blackfriday.protobuf.EdaMessage;
import com.example.orderservice.dto.DecreaseStockCountDTO;
import com.example.orderservice.entity.ProductOrder;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.feign.CatalogClient;
import com.example.orderservice.repository.OrderRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @KafkaListener(topics = "payment_result")
    public void consumePaymentResult(byte[] message) throws InvalidProtocolBufferException {
        EdaMessage.PaymentResultV1 paymentResult = EdaMessage.PaymentResultV1.parseFrom(message);
        log.info("[payment_result] {}", paymentResult);

        //결제 정보 업데이트
        ProductOrder order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
        order.updatePaymentId(paymentResult.getPaymentId());
        order.updateStatus(OrderStatus.DELIVERY_REQUESTED);
        orderRepository.save(order);

        Map<String, Object> product = catalogClient.getProduct(order.getProductId());
        EdaMessage.DeliveryRequestV1 deliveryRequest = EdaMessage.DeliveryRequestV1.newBuilder()
                .setOrderId(order.getId())
                .setProductName(product.get("name").toString())
                .setProductCount(order.getCount())
                .setAddress(order.getDeliveryAddress())
                .build();

        kafkaTemplate.send("delivery_request", deliveryRequest.toByteArray());
    }

    @KafkaListener(topics = "delivery_status_update")
    public void consumeDeliveryStatusUpdate(byte[] message) throws InvalidProtocolBufferException {
        EdaMessage.DeliveryStatusUpdateV1 deliveryStatusUpdate = EdaMessage.DeliveryStatusUpdateV1.parseFrom(message);
        log.info("[delivery_status_update] {}", deliveryStatusUpdate);

        if(deliveryStatusUpdate.getDeliveryStatus().equals("REQUESTED")) {
            ProductOrder order = orderRepository.findById(deliveryStatusUpdate.getOrderId()).orElseThrow();

            //deliveryId 저장
            order.updateDeliveryId(deliveryStatusUpdate.getDeliveryId());
            orderRepository.save(order);

            //상품 재고 감소
            DecreaseStockCountDTO decreaseStockCountDTO = new DecreaseStockCountDTO(order.getCount());
            catalogClient.decreaseStockCount(order.getProductId(), decreaseStockCountDTO);
        }
    }
}
