package servlets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ResponseResult;
import model.Student;
import repository.StudentRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NoSuchObjectException;
import java.sql.SQLException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    /**
     * 3.	Для сущностей Student и Auto реализовать сервлеты, которые задают API для:
     * •	Добавления. Принимается json объект этой сущности без id
     * •	Удаления. Принимается id сущности
     * •	Обновления. Принимается json объект этой сущности
     * •	Получения данных (по id и для всех). Либо не принимаются параметры, либо принимается id сущности
     * 	Все методы API должны возвращать json объект ResponseResult
     */

    protected void setUnicode(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String id = req.getParameter("id");
        try {
            StudentRepository studentRepository = new StudentRepository();
            if (id != null){
                try {
                    Student student = studentRepository.getById(Integer.parseInt(id));
                    if (student == null) throw new NoSuchObjectException("There is no student with such id!");
                    ResponseResult<Student> responseResult = new ResponseResult<>(true, null, student);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                    resp.setStatus(400);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                }
            }
            else {
                resp.getWriter().println(studentRepository.getStudents());
            }
        } catch (SQLException | ClassNotFoundException e) {
            ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
            resp.setStatus(400);
            resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String fio = req.getParameter("fio");
        String age = req.getParameter("age");
        String num = req.getParameter("num");
        String salary = req.getParameter("salary");
        if(fio != null && age != null && num != null && salary != null) {
            try {
                StudentRepository studentRepository = new StudentRepository();
                Student student = new Student(fio, Integer.parseInt(age), Integer.parseInt(num), Double.parseDouble(salary));
                studentRepository.add(student);
                ResponseResult<Student> responseResult = new ResponseResult<>(true, null, student);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            } catch (SQLException | ClassNotFoundException | RuntimeException e) {
                resp.setStatus(400);
                ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            }
        }
        else{
            try (BufferedReader reader = req.getReader()) {
                Student student = objectMapper.readValue(reader, Student.class);
                StudentRepository studentRepository = new StudentRepository();
                studentRepository.add(student);
                ResponseResult<Student> responseResult = new ResponseResult<>(true, null, student);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            } catch (SQLException | ClassNotFoundException | RuntimeException e) {
                resp.setStatus(400);
                ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            }
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String id = req.getParameter("id");
        try {
            StudentRepository studentRepository = new StudentRepository();
            if(id != null){
                try {
                    Student studentToDelete = studentRepository.getById(Integer.parseInt(id));
                    if (studentToDelete == null) throw new NoSuchObjectException("No student with such id!");
                    studentRepository.delete(studentToDelete);
                    ResponseResult<Student> responseResult = new ResponseResult<>(true, null, studentToDelete);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                } catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
            resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
                ResponseResult<Student> responseResult = new ResponseResult<>(true, null, newStudent);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
                resp.setStatus(400);
                ResponseResult<Student> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            }
        }
    }
}
