package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.model.Car;
import com.iaroslaveremeev.repository.CarRepository;
import com.iaroslaveremeev.repository.StudentRepository;
import com.iaroslaveremeev.dto.ResponseResult;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NoSuchObjectException;
import java.util.List;

@WebServlet("/cars")
public class CarServlet extends HttpServlet {

    protected void setUnicode(HttpServletRequest req, HttpServletResponse resp)
            throws UnsupportedEncodingException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String idStudent = req.getParameter("id_s");
        try {
            CarRepository carRepository = new CarRepository();
            if (id != null){
                try {
                    Car car = carRepository.getById(Integer.parseInt(id));
                    if (car == null) throw new NoSuchObjectException("There is no car with such id!");
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(car)));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
            else if (idStudent != null){
                try {
                    List<Car> studentCars = carRepository.getByStudentId(Integer.parseInt(idStudent));
                    if (studentCars == null) throw new NoSuchObjectException("There is no car with such student id!");
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(studentCars)));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
            else {
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(carRepository.getAuto())));
            }
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter()
                    .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String brand = req.getParameter("brand");
        String power = req.getParameter("power");
        String year = req.getParameter("year");
        String idStudent = req.getParameter("id_s");
        if(brand != null && power != null && year != null & idStudent != null) {
            try {
                CarRepository carRepository = new CarRepository();
                StudentRepository studentRepository = new StudentRepository();
                Car car = new Car(brand, Integer.parseInt(power),
                        Integer.parseInt(year), Integer.parseInt(idStudent));
                // проверка наличия студента с указанным idStudent
                if (checkStudent(car, studentRepository)){
                    // если такой студент есть, добавляем автомобиль в базу
                    carRepository.add(car);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(car)));
                } else throw new NoSuchObjectException("There is no student with such id!");
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
        else{
            try (BufferedReader reader = req.getReader()) {
                Car car = objectMapper.readValue(reader, Car.class);
                CarRepository carRepository = new CarRepository();
                StudentRepository studentRepository = new StudentRepository();
                // check if there is a student with such idStudent
                if (checkStudent(car, studentRepository)){
                    // add car to repository if there is such student
                    carRepository.add(car);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(car)));
                } else throw new NoSuchObjectException("There is no student with such id!");
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
    }

    private boolean checkStudent(Car car, StudentRepository studentRepository) {
        boolean studentFound = false;
        for (int i = 0; i < studentRepository.getStudents().size(); i++) {
            if (car.getIdStudent() == studentRepository.getStudents().get(i).getId()){
                studentFound = true;
                break;
            }
        }
        return studentFound;
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        try {
            CarRepository carRepository = new CarRepository();
            if(id != null){
                try {
                    Car carToDelete = carRepository.getById(Integer.parseInt(id));
                    if (carToDelete == null) throw new NoSuchObjectException("No car with such id!");
                    carRepository.delete(carToDelete);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(carToDelete)));
                } catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
        } catch (Exception e) {
            resp.getWriter()
                    .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String brand = req.getParameter("brand");
        String power = req.getParameter("power");
        String year = req.getParameter("year");
        String idStudent = req.getParameter("id_s");
        if(brand != null && power != null && year != null & idStudent != null) {
            try {
                CarRepository carRepository = new CarRepository();
                StudentRepository studentRepository = new StudentRepository();
                Car newCar = new Car(Integer.parseInt(id), brand, Integer.parseInt(power),
                        Integer.parseInt(year), Integer.parseInt(idStudent));
                // проверка наличия студента с указанным idStudent
                if (checkStudent(newCar, studentRepository)){
                    carRepository.update(newCar);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(newCar)));
                } else throw new NoSuchObjectException("There is no student with such id!");
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
    }

}
