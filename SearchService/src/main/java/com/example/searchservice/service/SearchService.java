package com.example.searchservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final StringRedisTemplate redisTemplate;

    //tag(keyword) -> 1,2,3,4,5(Set)
    public void addTagCache(Long productId, List<String> tags) {
        tags.forEach(tag -> redisTemplate.opsForSet().add(tag, productId.toString()));
    }

    public void removeTagCache(Long productId, List<String> tags) {
        tags.forEach(tag -> redisTemplate.opsForSet().remove(tag, productId.toString()));
    }

    public List<Long> getProductIdsByTag(String tag) {
        return Optional.of(redisTemplate.opsForSet().members(tag))
                       .orElse(Collections.emptySet())
                       .stream()
                       .map(Long::parseLong)
                       .toList();
    }
}
