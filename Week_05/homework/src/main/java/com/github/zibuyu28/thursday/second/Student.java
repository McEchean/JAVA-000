package com.github.zibuyu28.thursday.second;


import org.springframework.stereotype.Component;

@Component("Student1111")
public class Student {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void say() {
        System.out.println("我是"+this.name);
    }
}
