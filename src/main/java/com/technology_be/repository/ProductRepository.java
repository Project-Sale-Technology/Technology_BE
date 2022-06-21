package com.technology_be.repository;

import com.technology_be.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {
    /* Get product by category */
    @Query(value = "select * from product where category_id=?1 limit ?2,?3" , nativeQuery = true)
    List<Product> getProductsByCategoryId(Long id , int currentPage , int sizePage);

    /* Get all products and pagination*/
    @Query(value = "select * from product limit ?1,?2" , nativeQuery = true)
    List<Product> getProducts(int currentPage , int sizePage);

    /* select quantity available in products */
    @Query(value = "select count(product.id) from product" , nativeQuery = true)
    int getAmountOfProducts();

    /* Get product by name and category id */
    @Query(value = "select * from product where product.`name` like concat('%', ?1 ,'%') and product.category_id = ?2" , nativeQuery=true)
    List<Product> getProductsByNameAndCategoryId(String name , Long categoryId);
}
