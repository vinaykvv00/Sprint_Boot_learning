package com.example.SimpleWebApp.service;

import com.example.SimpleWebApp.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product(101, "iPhone", 50000));
        products.add(new Product(102, "Samsung", 40000));
        products.add(new Product(103, "XYZ", 20000));
    }

    // READ - get all products
    public List<Product> getProducts() {
        return products;
    }

    // CREATE - add new product
    public Product addProduct(Product product) {
        products.add(product);
        return product;
    }

    // UPDATE - modify existing product
    public Product updateProduct(int id, Product updatedProduct) {
        Optional<Product> existing = products.stream()
                .filter(p -> p.getProdId() == id)
                .findFirst();

        if (existing.isPresent()) {
            Product p = existing.get();
            p.setProdName(updatedProduct.getProdName());
            p.setPrice(updatedProduct.getPrice());
            return p;
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    // DELETE - remove product
    public void deleteProduct(int id) {
        products.removeIf(p -> p.getProdId() == id);
    }
}
