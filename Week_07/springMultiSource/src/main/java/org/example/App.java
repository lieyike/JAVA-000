package org.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        Service service = context.getBean(Service.class);

        service.readData();
        service.insertData();
    }

}
