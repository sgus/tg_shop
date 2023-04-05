package org.example.tg.dto;

import org.example.entity.Category;
import org.example.entity.Product;

import java.util.List;

public class CategoryProductDto {
    private Category category;
    private List<Product> products;

    public CategoryProductDto(Category category, List<Product> products) {
        this.category = category;
        this.products = products;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
