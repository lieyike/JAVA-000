package io.kimmking.spring01;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString

public class Student implements Serializable {
    
    private int id;
    private String name;

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
