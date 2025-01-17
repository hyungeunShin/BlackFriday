package com.example.catalogservice.service;

import com.example.catalogservice.cassandra.entity.Product;
import com.example.catalogservice.cassandra.repository.ProductRepository;
import com.example.catalogservice.dto.ProductTagsDTO;
import com.example.catalogservice.feign.SearchClient;
import com.example.catalogservice.mysql.entity.SellerProduct;
import com.example.catalogservice.mysql.repository.SellerProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CatalogService {
    private final ProductRepository productRepository;
    private final SellerProductRepository sellerProductRepository;
    private final SearchClient searchClient;

    @Transactional
    public Product registerProduct(Long sellerId, String name, String description, Long price, Long stockCount, List<String> tags) {
        SellerProduct sellerProduct = sellerProductRepository.save(new SellerProduct(sellerId));
        searchClient.addTagCache(new ProductTagsDTO(sellerProduct.getId(), tags));
        return productRepository.save(new Product(
                sellerProduct.getId(), sellerId, name,
                description, price, stockCount, tags
        ));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        searchClient.removeTagCache(new ProductTagsDTO(productId, product.getTags()));
        productRepository.deleteById(productId);
        sellerProductRepository.deleteById(productId);
    }

    public List<Product> getProductsBySellerId(Long sellerId) {
        return sellerProductRepository.findBySellerId(sellerId)
                                      .stream()
                                      .map(sellerProduct -> productRepository.findById(sellerProduct.getId()))
                                      .filter(Optional::isPresent)
                                      .map(Optional::get)
                                      .toList();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    @Transactional
    public Product decreaseStockCount(Long productId, Long decreaseCount) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.decreaseCount(decreaseCount);
        return productRepository.save(product);
    }
}