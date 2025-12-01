package com.telusko.SpringSecEX;

public class Student {
    private int it;
    private String name;
    private int marks;

    public Student(int it, String name, int marks) {
        this.it = it;
        this.name = name;
        this.marks = marks;
    }

    public int getIt() {
        return it;
    }

    public void setIt(int it) {
        this.it = it;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "it=" + it +
                ", name='" + name + '\'' +
                ", marks=" + marks +
                '}';
    }
}
