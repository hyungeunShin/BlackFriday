package com.example.catalogservice.controller;

import com.example.catalogservice.dto.DecreaseStockCountDTO;
import com.example.catalogservice.dto.ProductResponse;
import com.example.catalogservice.dto.RegisterProductDTO;
import com.example.catalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;

    @PostMapping("/products")
    public ProductResponse registerProduct(@RequestBody RegisterProductDTO dto) {
        return new ProductResponse(catalogService.registerProduct(
                dto.sellerId(), dto.name(), dto.description(),
                dto.price(), dto.stockCount(), dto.tags()
        ));
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        catalogService.deleteProduct(productId);
    }

    @GetMapping("/products/{productId}")
    public ProductResponse getProductById(@PathVariable Long productId) {
        return new ProductResponse(catalogService.getProductById(productId));
    }

    @GetMapping("/sellers/{sellerId}/products")
    public List<ProductResponse> getProductsBySellerId (@PathVariable Long sellerId) {
        return catalogService.getProductsBySellerId(sellerId).stream().map(ProductResponse::new).toList();
    }

    @PostMapping("/products/{productId}/decreaseStockCount")
    public ProductResponse decreaseStockCount(@PathVariable("productId") Long productId, @RequestBody DecreaseStockCountDTO dto) {
        return new ProductResponse(catalogService.decreaseStockCount(productId, dto.decreaseCount()));
    }
}
