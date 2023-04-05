package org.example.repository;

import org.example.entity.Category;
import org.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    List<Product> findAll();

    @Override
    Optional<Product> findById(Long integer);

    @Query(value = "SELECT o FROM Product o WHERE o.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

}
