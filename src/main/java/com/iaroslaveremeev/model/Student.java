package com.iaroslaveremeev.model;

import java.util.Objects;

public class Student {
    private int id;
    private String fio;
    private int age;
    private int num;
    private double salary;

    public Student() {
    }

    public Student(int id, String fio, int age, int num, double salary) {
        this.id = id;
        this.fio = fio;
        this.age = age;
        this.num = num;
        this.salary = salary;
    }

    public Student(String fio, int age, int num, double salary) {
        this.fio = fio;
        this.age = age;
        this.num = num;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && num == student.num && salary == student.salary && Objects.equals(fio, student.fio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fio, age, num, salary);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", age=" + age +
                ", num=" + num +
                ", salary=" + salary +
                '}';
    }
}
