package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.repository.AutoRepository;
import com.iaroslaveremeev.repository.StudentRepository;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Auto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/auto")
public class AutoServlet extends HttpServlet {

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
            AutoRepository autoRepository = new AutoRepository();
            if (id != null){
                try {
                    Auto auto = autoRepository.getById(Integer.parseInt(id));
                    if (auto == null) throw new NoSuchObjectException("There is no auto with such id!");
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(auto)));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
            else if (idStudent != null){
                try {
                    List<Auto> studentAutos = autoRepository.getByStudentId(Integer.parseInt(idStudent));
                    if (studentAutos == null) throw new NoSuchObjectException("There is no auto with such student id!");
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(studentAutos)));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
            else {
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(autoRepository.getAuto())));
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
                AutoRepository autoRepository = new AutoRepository();
                StudentRepository studentRepository = new StudentRepository();
                Auto auto = new Auto(brand, Integer.parseInt(power),
                        Integer.parseInt(year), Integer.parseInt(idStudent));
                // проверка наличия студента с указанным idStudent
                if (checkStudent(auto, studentRepository)){
                    // если такой студент есть, добавляем автомобиль в базу
                    autoRepository.add(auto);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(auto)));
                } else throw new NoSuchObjectException("There is no student with such id!");
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
        else{
            try (BufferedReader reader = req.getReader()) {
                Auto auto = objectMapper.readValue(reader, Auto.class);
                AutoRepository autoRepository = new AutoRepository();
                StudentRepository studentRepository = new StudentRepository();
                // check if there is a student with such idStudent
                if (checkStudent(auto, studentRepository)){
                    // add auto to repository if there is such student
                    autoRepository.add(auto);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(auto)));
                } else throw new NoSuchObjectException("There is no student with such id!");
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
    }

    private boolean checkStudent(Auto auto, StudentRepository studentRepository) {
        boolean studentFound = false;
        for (int i = 0; i < studentRepository.getStudents().size(); i++) {
            if (auto.getIdStudent() == studentRepository.getStudents().get(i).getId()){
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
            AutoRepository autoRepository = new AutoRepository();
            if(id != null){
                try {
                    Auto autoToDelete = autoRepository.getById(Integer.parseInt(id));
                    if (autoToDelete == null) throw new NoSuchObjectException("No auto with such id!");
                    autoRepository.delete(autoToDelete);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(autoToDelete)));
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
                AutoRepository autoRepository = new AutoRepository();
                StudentRepository studentRepository = new StudentRepository();
                Auto newAuto = new Auto(Integer.parseInt(id), brand, Integer.parseInt(power),
                        Integer.parseInt(year), Integer.parseInt(idStudent));
                // проверка наличия студента с указанным idStudent
                if (checkStudent(newAuto, studentRepository)){
                    autoRepository.update(newAuto);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(newAuto)));
                } else throw new NoSuchObjectException("There is no student with such id!");
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
    }

}
