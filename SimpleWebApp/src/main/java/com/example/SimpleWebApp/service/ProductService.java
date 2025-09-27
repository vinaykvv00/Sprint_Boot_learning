package com.example.SimpleWebApp.service;

import com.example.SimpleWebApp.model.Product;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    List<Product> products = Arrays.asList(new Product(101,"iphone",50000),
            new Product(102, "samsung", 40000),
            new Product(103, "xyz", 20000));

    public List<Product> getProducts()
    {
        return products;
    }
}
