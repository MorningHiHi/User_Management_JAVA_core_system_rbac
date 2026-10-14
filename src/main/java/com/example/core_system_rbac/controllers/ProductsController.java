package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.entities.Product;
import com.example.core_system_rbac.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductsController {
    public final ProductService productService;

    @GetMapping("/products")
    public String viewProducts(Model model) {
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "products.html";
    }

    @PostMapping("/products")
    public String product(@RequestParam String name, @RequestParam double price, Model model) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        productService.addProduct(p);

        List<Product> products = productService.getAll();
        model.addAttribute("products", products);// model phải được kiểm soát bởi mọt đối tượng gì đó có trong spring?
        return "products.html" ;
    }

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

}
