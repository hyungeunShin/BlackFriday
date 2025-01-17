package com.example.catalogservice.dto;

import java.util.List;

public record RegisterProductDTO(
        Long sellerId, String name, String description,
        Long price, Long stockCount, List<String> tags
) {
}
