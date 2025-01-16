package com.example.deliveryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAddressDTO {
    private Long userId;
    private String address;
    private String alias;
}
