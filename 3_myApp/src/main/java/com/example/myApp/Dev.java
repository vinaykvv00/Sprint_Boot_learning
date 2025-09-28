package com.example.myApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
//dependcy injection spring is injecting this object to main class

//di - field , constrctor, setter injection
@Component
public class Dev {

    //so this is the autowire it connect the this dev class and laptop class;
    @Autowired //field injection
    @Qualifier("laptop") //it can pick this class, if cnfucion is there,then we need to do this
    private Computer comp;

//    //this is contructor injection done
//    public Dev(Laptop laptop)
//    {
//        this.laptop = laptop;
//    }
//
//    @Autowired  //setter injection
//    public void setLaptop(Laptop laptop)
//    {
//        this.laptop = laptop;
//    }

    public void build()
    {
        comp.compile();
        System.out.println("working on building projects");
    }
}
