package com.example.searchservice.controller;

import com.example.searchservice.dto.ProductTagsDTO;
import com.example.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/addTag")
    public void addTagCache(@RequestBody ProductTagsDTO dto) {
        searchService.addTagCache(dto.productId(), dto.tags());
    }

    @PostMapping("/removeTag")
    public void removeTagCache(@RequestBody ProductTagsDTO dto) {
        searchService.removeTagCache(dto.productId(), dto.tags());
    }

    @GetMapping("/tags/{tag}")
    public List<Long> getTagProductIds(@PathVariable("tag") String tag) {
        return searchService.getProductIdsByTag(tag);
    }
}
