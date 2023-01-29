package servlets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ResponseResult;
import model.Auto;
import model.Student;
import repository.AutoRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.NoSuchObjectException;
import java.sql.SQLException;

@WebServlet("/auto")
public class AutoServlet extends HttpServlet {

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
            AutoRepository autoRepository = new AutoRepository();
            if (id != null){
                try {
                    Auto auto = autoRepository.getById(Integer.parseInt(id));
                    if (auto == null) throw new NoSuchObjectException("There is no auto with such id!");
                    ResponseResult<Auto> responseResult = new ResponseResult<>(true, null, student);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                }
                catch (RuntimeException | NoSuchObjectException e) {
                    ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                    resp.setStatus(400);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                }
            }
            else {
                resp.getWriter().println(autoRepository.getAuto());
            }
        } catch (SQLException | ClassNotFoundException e) {
            ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
            resp.setStatus(400);
            resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String brand = req.getParameter("brand");
        String power = req.getParameter("power");
        String year = req.getParameter("year");
        String idStudent = req.getParameter("idStudent");
        if(brand != null && power != null && year != null & idStudent != null) {
            try {
                AutoRepository autoRepository = new AutoRepository();
                Auto auto = new Auto(brand, Integer.parseInt(power),
                        Integer.parseInt(year), Integer.parseInt(idStudent));
                autoRepository.add(auto);
                ResponseResult<Auto> responseResult = new ResponseResult<>(true, null, auto);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            } catch (SQLException | ClassNotFoundException | RuntimeException e) {
                resp.setStatus(400);
                ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            }
        }
        else{
            try (BufferedReader reader = req.getReader()) {
                Auto auto = objectMapper.readValue(reader, Auto.class);
                AutoRepository autoRepository = new AutoRepository();
                autoRepository.add(auto);
                ResponseResult<Auto> responseResult = new ResponseResult<>(true, null, auto);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            } catch (SQLException | ClassNotFoundException | RuntimeException e) {
                resp.setStatus(400);
                ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
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
            AutoRepository autoRepository = new AutoRepository();
            if(id != null){
                try {
                    Auto autoToDelete = autoRepository.getById(Integer.parseInt(id));
                    if (autoToDelete == null) throw new NoSuchObjectException("No auto with such id!");
                    autoRepository.delete(autoToDelete);
                    ResponseResult<Auto> responseResult = new ResponseResult<>(true, null, autoToDelete);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                } catch (RuntimeException | NoSuchObjectException e) {
                    resp.setStatus(400);
                    ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                    resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
            resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String id = req.getParameter("id");
        String brand = req.getParameter("brand");
        String power = req.getParameter("power");
        String year = req.getParameter("year");
        String idStudent = req.getParameter("idStudent");
        if(brand != null && power != null && year != null & idStudent != null) {
            try {
                AutoRepository autoRepository = new AutoRepository();
                Auto newAuto = new Auto(Integer.parseInt(id), brand, Integer.parseInt(power),
                        Integer.parseInt(year), Integer.parseInt(idStudent));
                autoRepository.update(newAuto);
                ResponseResult<Auto> responseResult = new ResponseResult<>(true, null, newAuto);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
                resp.setStatus(400);
                ResponseResult<Auto> responseResult = new ResponseResult<>(false, e.getMessage(), null);
                resp.getWriter().println(objectMapper.writeValueAsString(responseResult));
            }
        }
    }
}
