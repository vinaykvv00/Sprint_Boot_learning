package org.example;

public class Dev {

    private Laptop laptop;
    private Computer com;
    private int age;

    public Dev()
    {
         System.out.println("Dev constructor....");
    }
    public Dev(Laptop laptop) {
        this.laptop = laptop;
    }

    public Computer getCom() {
        return com;
    }

    public void setCom(Computer com) {
        this.com = com;
    }

    // getter method
    public int getAge() {
        return age;
    }

    public Dev(int age) {
        this.age = age;
    }

    // settoer method for private access modifier
    public void setAge(int age) {
        this.age = age;
    }

    public Laptop getLaptop() {
        return laptop;
    }


    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public void build()
    {
        System.out.println("this is the dev class without spring an boot");
       // laptop.compile();
        com.compile();
    }
}
