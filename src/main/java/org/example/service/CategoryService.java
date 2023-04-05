package org.example.service;

import jakarta.annotation.Resource;
import org.example.entity.Category;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryRepository repository;
//    public List<CategoryProductDto> findAllWithProducts(){
//        return categoryProductRepository.findAllCategoryProduct();
//    };
    public List<Category> findAll(){
        return repository.findAll();
    };
    public Category findById(Long categoryId) {
        return repository.findById(categoryId);
    }
}
