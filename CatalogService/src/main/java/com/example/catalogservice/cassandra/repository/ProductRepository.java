package com.example.catalogservice.cassandra.repository;

import com.example.catalogservice.cassandra.entity.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ProductRepository extends CassandraRepository<Product, Long> {
}
