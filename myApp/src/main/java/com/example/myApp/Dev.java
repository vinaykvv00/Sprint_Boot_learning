package com.example.myApp;

import org.springframework.stereotype.Component;
//dependcy injection spring is injecting this object to main class
@Component
public class Dev {

    public void build()
    {
        System.out.println("working on building projects");
    }
}
