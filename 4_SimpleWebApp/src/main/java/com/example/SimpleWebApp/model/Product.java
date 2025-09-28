package com.example.SimpleWebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@Component
public class Product {

    private int prodId;
    private String prodName;
    private int price;

    public Product()
    {

    }
}
