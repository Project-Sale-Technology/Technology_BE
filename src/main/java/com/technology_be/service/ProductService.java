package com.technology_be.service;

import com.technology_be.model.Product;
import com.technology_be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /* Find all */
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }
}
