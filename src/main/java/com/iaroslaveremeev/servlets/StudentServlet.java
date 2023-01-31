package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.repository.StudentRepository;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Student;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NoSuchObjectException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    protected void setUnicode(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        try {
            StudentRepository studentRepository = new StudentRepository();
            if (id != null){
                try {
                    Student student = studentRepository.getById(Integer.parseInt(id));
                    if (student == null) throw new NoSuchObjectException("There is no student with such id!");
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(student)));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
            else {
                resp.getWriter().println(studentRepository.getStudents());
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
        String fio = req.getParameter("fio");
        String age = req.getParameter("age");
        String num = req.getParameter("num");
        String salary = req.getParameter("salary");
        if(fio != null && age != null && num != null && salary != null) {
            try {
                StudentRepository studentRepository = new StudentRepository();
                Student student = new Student(fio, Integer.parseInt(age), Integer.parseInt(num), Double.parseDouble(salary));
                if (studentRepository.add(student)) {
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(student)));
                }
                else {
                    throw new RuntimeException("Student not added");
                }
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
        else{
            try (BufferedReader reader = req.getReader()) {
                Student student = objectMapper.readValue(reader, Student.class);
                StudentRepository studentRepository = new StudentRepository();
                studentRepository.add(student);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(student)));
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        try {
            StudentRepository studentRepository = new StudentRepository();
            if(id != null){
                try {
                    Student studentToDelete = studentRepository.getById(Integer.parseInt(id));
                    if (studentToDelete == null) throw new NoSuchObjectException("No student with such id!");
                    studentRepository.delete(studentToDelete);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(studentToDelete)));
                } catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
                }
            }
        } catch (Exception e) {
            resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String fio = req.getParameter("fio");
        String age = req.getParameter("age");
        String num = req.getParameter("num");
        String salary = req.getParameter("salary");
        if(id != null & fio != null && age != null && num != null && salary != null) {
            try {
                StudentRepository studentRepository = new StudentRepository();
                Student newStudent = new Student(Integer.parseInt(id), fio, Integer.parseInt(age),
                        Integer.parseInt(num), Double.parseDouble(salary));
                studentRepository.update(newStudent);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(newStudent)));
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter()
                        .println(objectMapper.writeValueAsString(new ResponseResult<>(e.getMessage())));
            }
        }
    }
}
