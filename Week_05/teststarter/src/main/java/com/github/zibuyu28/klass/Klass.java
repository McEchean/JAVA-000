package com.github.zibuyu28.klass;


import java.util.List;

public class Klass {
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;
    
    List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void dong(){
        System.out.println(this.getStudents());
    }
    
}
