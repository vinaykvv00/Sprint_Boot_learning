package com.example.SimpleWebApp.controller;

import com.example.SimpleWebApp.model.Product;
import com.example.SimpleWebApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // GET all products
    @GetMapping
    public List<Product> getProducts() {
        return service.getProducts();
    }

    // POST - add new product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return service.addProduct(product);
    }

    // PUT - update existing product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    // DELETE - delete product by ID
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        service.deleteProduct(id);
        return "Product deleted successfully with ID: " + id;
    }
}
