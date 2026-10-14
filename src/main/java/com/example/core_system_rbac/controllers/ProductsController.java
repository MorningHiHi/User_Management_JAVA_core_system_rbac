package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.entities.Product;
import com.example.core_system_rbac.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductsController {
    public final ProductService productService;

    @RequestMapping(path="/products",method = RequestMethod.POST)
    public String product(@RequestParam String name, @RequestParam double price, Model model) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        productService.addProduct(p);

        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "products.html" ;
    }

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

}
