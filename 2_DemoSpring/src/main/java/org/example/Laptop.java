package org.example;

public class Laptop implements Computer {

    public Laptop()
    {
        System.out.println("Laptop constructor....");
    }

    public void compile()
    {
      System.out.println("we are compiling here in laptop");
    }
}
