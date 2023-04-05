package org.example.service;

import jakarta.annotation.Resource;
import org.example.entity.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Resource
    private ProductRepository repository;

    public List<Product> findAll() {
      return repository.findAll();
    }
    public List<Product> findProductsByCategoryContains() {
      return repository.findAll();
    }
    public List<Product> findByCategoryId(Long categoryId) {
      return repository.findByCategoryId(categoryId);
    }
    public Optional<Product> findById(Long categoryId) {
      return repository.findById(categoryId);
    }
}
