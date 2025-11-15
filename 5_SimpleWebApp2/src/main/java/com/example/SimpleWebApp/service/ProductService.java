package com.example.SimpleWebApp.service;

import com.example.SimpleWebApp.model.Product;
import com.example.SimpleWebApp.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

//    private final List<Product> products = new ArrayList<>();
//
//    public ProductService() {
//        products.add(new Product(101, "iPhone", 50000));
//        products.add(new Product(102, "Samsung", 40000));
//        products.add(new Product(103, "XYZ", 20000));
//    }

    // READ - get all products
    public List<Product> getProducts() {
        return repo.findAll();
    }

    // CREATE - add new product
    public Product addProduct(Product product) {
        return repo.save(product);
    }

    // UPDATE - modify existing product
    public Product updateProduct(int id, Product updatedProduct) {
         return repo.save(updatedProduct);
    }

    // DELETE - remove product
    public void deleteProduct(int id) {
        repo.deleteById(id);
    }
}
