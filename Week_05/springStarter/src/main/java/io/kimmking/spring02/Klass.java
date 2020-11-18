package io.kimmking.spring02;

import io.kimmking.spring01.Student;
import lombok.Data;

import java.util.List;

@Data
public class Klass {

    List<Student> students;

    public Klass(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void dong() {
        System.out.println(this.getStudents());
    }


}
