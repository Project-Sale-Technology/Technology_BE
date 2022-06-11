package com.technology_be.service;

import com.technology_be.model.Category;
import com.technology_be.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /* Find all */
    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }
}
