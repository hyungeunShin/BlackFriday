package com.example.deliveryservice.service;

import blackfriday.protobuf.EdaMessage;
import com.example.deliveryservice.entity.Delivery;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final DeliveryService deliveryService;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @KafkaListener(topics = "delivery_request")
    public void consumeDeliveryRequest(byte[] message) throws InvalidProtocolBufferException {
        EdaMessage.DeliveryRequestV1 deliveryRequest = EdaMessage.DeliveryRequestV1.parseFrom(message);
        log.info("[delivery_request] {}", deliveryRequest);

        Delivery delivery = deliveryService.processDelivery(
                deliveryRequest.getOrderId(),
                deliveryRequest.getProductName(),
                deliveryRequest.getProductCount(),
                deliveryRequest.getAddress()
        );

        EdaMessage.DeliveryStatusUpdateV1 deliveryStatusUpdate = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(delivery.getOrderId())
                .setDeliveryId(delivery.getId())
                .setDeliveryStatus(delivery.getStatus().toString())
                .build();

        kafkaTemplate.send("delivery_status_update", deliveryStatusUpdate.toByteArray());
    }
}
