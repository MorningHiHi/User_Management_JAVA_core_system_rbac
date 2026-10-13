package com.example.core_system_rbac.services;

import com.example.core_system_rbac.entities.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    List<Product> products = new ArrayList<>();
    public void addProduct(Product product) {
        products.add(product);
    }
    public List<Product> getAll(){
        return products;
    }
}
