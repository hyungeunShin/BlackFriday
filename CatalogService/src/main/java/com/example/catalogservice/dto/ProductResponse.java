package com.example.catalogservice.dto;

import com.example.catalogservice.cassandra.entity.Product;

import java.util.List;

public record ProductResponse(
        Long id, Long sellerId, String name,
        String description, Long price, Long stockCount, List<String> tags) {
    public ProductResponse(Product product) {
        this(product.getId(), product.getSellerId(), product.getName(), product.getDescription(), product.getPrice(), product.getStockCount(), product.getTags());
    }
}
