package com.example.deliveryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDeliveryDTO {
    private Long orderId;
    private String productName;
    private Long productCount;
    private String address;
}
