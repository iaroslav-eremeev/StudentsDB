package com.iaroslaveremeev.model;

import java.util.Objects;

public class Auto {

    private int id;
    private String brand;
    private int power;
    private int year;
    private int idStudent;

    public Auto() {
    }

    public Auto(int id, String brand, int power, int year, int idStudent) {
        this.id = id;
        this.brand = brand;
        this.power = power;
        this.year = year;
        this.idStudent = idStudent;
    }

    public Auto(String brand, int power, int year, int idStudent) {
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
        Auto auto = (Auto) o;
        return id == auto.id && power == auto.power && year == auto.year && idStudent == auto.idStudent && Objects.equals(brand, auto.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, power, year, idStudent);
    }

    @Override
    public String toString() {
        return "Auto{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", power=" + power +
                ", year=" + year +
                ", id_s=" + idStudent +
                '}';
    }
}
