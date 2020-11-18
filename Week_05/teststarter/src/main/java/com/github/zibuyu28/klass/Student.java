package com.github.zibuyu28.klass;

import java.io.Serializable;

public class Student implements Serializable {
    
    private int id;
    private String name;

    public Student() {}

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public void init(){
        System.out.println("hello...........");
    }
    
    public Student create(){
        return new Student(101,"KK101");
    }
}
