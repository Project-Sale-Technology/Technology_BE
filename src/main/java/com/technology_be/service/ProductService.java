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

    /* Get product by name and by category id */
    public List<Product> getProductsByNameAndCategoryId(String name , Long categoryId) {
        return this.productRepository.getProductsByNameAndCategoryId(name , categoryId);
    }

    /* Get products by category id*/
    public List<Product> getProductByCategoryId(Long id , int currentPage, int sizePage) {
        return this.productRepository.getProductsByCategoryId(id , currentPage , sizePage);
    }

    /* Get Amount of products */
    public int getAmountOfProducts() {
        return this.productRepository.getAmountOfProducts();
    }
}
