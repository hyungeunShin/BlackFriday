package com.example.catalogservice.dto;

import java.util.List;

public record ProductTagsDTO(Long productId, List<String> tags) {
}
