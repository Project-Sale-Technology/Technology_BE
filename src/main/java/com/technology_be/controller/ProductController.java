package com.technology_be.controller;

import com.technology_be.model.Category;
import com.technology_be.model.Product;
import com.technology_be.service.CategoryService;
import com.technology_be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/home")
@CrossOrigin("http://localhost:4200/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /* Get all products */
    @RequestMapping(value = "/products/{currentPage}&{sizePage}" , method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProducts(@PathVariable("currentPage") int currentPage ,
                                                        @PathVariable("sizePage") int sizePage) {
        List<Product> products = productService.getAllProducts(currentPage , sizePage);
        if(products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products , HttpStatus.OK);
    }

    /* Get all type products*/
    @RequestMapping(value = "/category" , method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        if(categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories , HttpStatus.OK);
    }

    /* Get products by category */
    @RequestMapping(value = "/products/category={categoryId}/{currentPage}&{sizePage}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable("categoryId") Long categoryId
    , @PathVariable("sizePage") int sizePage , @PathVariable("currentPage") int currentPage) {
        List<Product> products = productService.getProductByCategoryId(categoryId , currentPage , sizePage);
        System.out.println(products.size());
        if(products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products , HttpStatus.OK);
    }

    /* Get amount of products*/
    @RequestMapping(value = "/products/amount")
    public ResponseEntity<Integer> getAmountOfProducts() {
        return new ResponseEntity<>(productService.getAmountOfProducts() , HttpStatus.OK);
    }
}
