package com.example.ProductBot.repository;

import com.example.ProductBot.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteByName(String name);
}
