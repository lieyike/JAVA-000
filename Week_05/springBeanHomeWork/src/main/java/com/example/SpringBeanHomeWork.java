package com.example;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanHomeWork {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Bean1 bean1 = (Bean1) context.getBean("Bean1");
        System.out.println(bean1.showName());
    }
}
