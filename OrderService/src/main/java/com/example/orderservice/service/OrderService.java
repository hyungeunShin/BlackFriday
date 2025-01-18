package com.example.orderservice.service;

import blackfriday.protobuf.EdaMessage;
import com.example.orderservice.dto.*;
import com.example.orderservice.entity.ProductOrder;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.feign.CatalogClient;
import com.example.orderservice.feign.DeliveryClient;
import com.example.orderservice.feign.PaymentClient;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final CatalogClient catalogClient;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Transactional
    public StartOrderResponseDTO startOrder(Long userId, Long productId, Long count) {
        //1. 상품 정보 조회
        Map<String, Object> product = catalogClient.getProduct(productId);

        //2. 결제 수단 정보 조회
        Map<String, Object> paymentMethod = paymentClient.getPaymentMethod(userId);

        //3. 배송지 정보 조회
        Map<String, Object> address = deliveryClient.getUserAddress(userId);

        //4. 주문 정보 생성
        ProductOrder order = new ProductOrder(userId, productId, count, OrderStatus.INITIATED, null, null, null);
        orderRepository.save(order);

        return new StartOrderResponseDTO(order.getId(), paymentMethod, address);
    }

    @Transactional
    public ProductOrder finishOrder(Long orderId, Long paymentMethodId, Long addressId) {
        ProductOrder order = orderRepository.findById(orderId).orElseThrow();

        //1. 상품 정보 조회
        Map<String, Object> product = catalogClient.getProduct(order.getProductId());

        //2. 결제 요청
//        ProcessPaymentDTO processPaymentDTO = new ProcessPaymentDTO(orderId, order.getUserId(), Long.parseLong(product.get("price").toString()) * order.getCount(), paymentMethodId);
//        Map<String, Object> payment = paymentClient.processPayment(processPaymentDTO);

        EdaMessage.PaymentRequestV1 paymentRequest = EdaMessage.PaymentRequestV1.newBuilder()
                .setOrderId(orderId)
                .setUserId(order.getUserId())
                .setAmountKRW(Long.parseLong(product.get("price").toString()) * order.getCount())
                .setPaymentMethodId(paymentMethodId)
                .build();
        kafkaTemplate.send("payment_request", paymentRequest.toByteArray());

        //3. 배송 요청
//        Map<String, Object> address = deliveryClient.getAddress(addressId);
//        ProcessDeliveryDTO processDeliveryDTO = new ProcessDeliveryDTO(orderId, product.get("name").toString(), order.getCount(), address.get("address").toString());
//        Map<String, Object> delivery = deliveryClient.processDelivery(processDeliveryDTO);

        //4. 상품 재고 감소
//        DecreaseStockCountDTO decreaseStockCountDTO = new DecreaseStockCountDTO(order.getCount());
//        catalogClient.decreaseStockCount(order.getProductId(), decreaseStockCountDTO);

        //5. 주문 정보 업데이트
//        order.updateStatus(Long.parseLong(payment.get("id").toString()), Long.parseLong(delivery.get("id").toString()), OrderStatus.DELIVERY_REQUESTED);
        Map<String, Object> address = deliveryClient.getAddress(addressId);
        order.updateStatus(OrderStatus.PAYMENT_REQUESTED);
        order.updateDeliveryAddress(address.get("address").toString());
        return orderRepository.save(order);
    }

    public List<ProductOrder> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public ProductOrderDetailDTO getOrderDetail(Long orderId) {
        ProductOrder order = orderRepository.findById(orderId).orElseThrow();

        Map<String, Object> payment = paymentClient.getPayment(order.getPaymentId());
        Map<String, Object> delivery = deliveryClient.getDelivery(order.getDeliveryId());

        return new ProductOrderDetailDTO(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getPaymentId(),
                order.getDeliveryId(),
                order.getOrderStatus(),
                payment.get("paymentStatus").toString(),
                delivery.get("status").toString()
        );
    }
}
