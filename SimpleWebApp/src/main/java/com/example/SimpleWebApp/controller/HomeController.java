package com.example.SimpleWebApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String greet()
    {
        return "welcome to simple spring boot";

    }

    @RequestMapping("/about")
    public String about()
    {
        return " somthing we are doinng to understant the end points";
    }
}
