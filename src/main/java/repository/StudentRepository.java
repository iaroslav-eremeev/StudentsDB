package repository;


import model.Student;
import util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements AutoCloseable {
    private Connection conn;

    public StudentRepository() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(Student student) {
        String sql = "insert into students(fio,age,num,salary) values (?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, student.getFio());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setInt(3, student.getNum());
            preparedStatement.setDouble(4, student.getSalary());
            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    student.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {}
        return false;
    }

    public List<Student> getStudents() {
        String sql = "select * from students";
        ArrayList<Student> students = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setFio(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setNum(resultSet.getInt(4));
                student.setSalary(resultSet.getDouble(5));

                students.add(student);
            }
        } catch (SQLException ignored) {}
        return students;
    }

    public Student getById(int id) {
        String sql = "select * from students where students.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Student trainer = new Student();
            trainer.setId(resultSet.getInt(1));
            trainer.setFio(resultSet.getString(2));
            trainer.setAge(resultSet.getInt(3));
            trainer.setNum(resultSet.getInt(4));
            trainer.setSalary(resultSet.getInt(5));
            return trainer;
        } catch (SQLException ignored) {}
        return null;
    }

    public boolean delete(Student student) {
        String sql = "delete from students where students.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, student.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {}
        return false;
    }

    public boolean update(Student student) {
        String sql = "update students set students.fio=?, students.age=?, students.num=?, students.salary=? where students.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getFio());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setInt(3, student.getNum());
            preparedStatement.setDouble(4, student.getSalary());
            preparedStatement.setInt(5, student.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public void close() throws Exception {
        if (this.conn != null)
            this.conn.close();
    }
}
