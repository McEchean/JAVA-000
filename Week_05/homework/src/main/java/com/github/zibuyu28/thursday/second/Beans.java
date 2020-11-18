package com.github.zibuyu28.thursday.second;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Beans {


    public static void main(String[] args) {
        Beans.assembleBean1();
        Beans.assembleBean2();
        Beans.assembleBean3();
    }

    private static void assembleBean1() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring1.xml");
        Student stu = ctx.getBean(Student.class);
        stu.say();
    }

    private static void assembleBean2() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring2.xml");
        Student stu = ctx.getBean(Student.class);
        stu.setName("柚子里");
        stu.say();
    }

    private static void assembleBean3() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
        Student stu = ctx.getBean(Student.class);
        stu.say();
    }
}
