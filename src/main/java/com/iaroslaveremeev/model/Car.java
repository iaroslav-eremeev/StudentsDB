package com.iaroslaveremeev.model;

import java.util.Objects;

public class Car {

    private int id;
    private String brand;
    private int power;
    private int year;
    private int idStudent;

    public Car() {
    }

    public Car(int id, String brand, int power, int year, int idStudent) {
        this.id = id;
        this.brand = brand;
        this.power = power;
        this.year = year;
        this.idStudent = idStudent;
    }

    public Car(String brand, int power, int year, int idStudent) {
        this.brand = brand;
        this.power = power;
        this.year = year;
        this.idStudent = idStudent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && power == car.power && year == car.year && idStudent == car.idStudent && Objects.equals(brand, car.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, power, year, idStudent);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", power=" + power +
                ", year=" + year +
                ", id_s=" + idStudent +
                '}';
    }
}
