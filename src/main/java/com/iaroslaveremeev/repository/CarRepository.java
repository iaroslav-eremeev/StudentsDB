package com.iaroslaveremeev.repository;

import com.iaroslaveremeev.model.Car;
import com.iaroslaveremeev.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CarRepository implements AutoCloseable {

    private Connection conn;

    public CarRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL,
                    Constants.USERNAME, Constants.PASSWORD);
        } catch (ClassNotFoundException | SQLException ignored) {}
    }

    public boolean add(Car car) {
        String sql = "insert into cars(brand, power, year, id_s) values (?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setInt(2, car.getPower());
            preparedStatement.setInt(3, car.getYear());
            preparedStatement.setInt(4, car.getIdStudent());

            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    car.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {}
        return false;
    }

    public List<Car> getAuto() {
        String sql = "select * from cars";
        ArrayList<Car> cars = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt(1));
                car.setBrand(resultSet.getString(2));
                car.setPower(resultSet.getInt(3));
                car.setYear(resultSet.getInt(4));
                car.setIdStudent(resultSet.getInt(5));

                cars.add(car);
            }
        } catch (SQLException ignored) {}
        return cars;
    }

    public Car getById(int id) {
        String sql = "select * from cars where cars.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Car carById = new Car();
            carById.setId(resultSet.getInt(1));
            carById.setBrand(resultSet.getString(2));
            carById.setPower(resultSet.getInt(3));
            carById.setYear(resultSet.getInt(4));
            carById.setIdStudent(resultSet.getInt(5));
            return carById;
        } catch (SQLException ignored) {}
        return null;
    }

    public List<Car> getByStudentId(int idStudent) {
        String sql = "select * from cars where cars.id_s=?";
        ArrayList<Car> studentCars = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idStudent);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Car carByStudentId = new Car();
                carByStudentId.setId(resultSet.getInt(1));
                carByStudentId.setBrand(resultSet.getString(2));
                carByStudentId.setPower(resultSet.getInt(3));
                carByStudentId.setYear(resultSet.getInt(4));
                carByStudentId.setIdStudent(resultSet.getInt(5));
                studentCars.add(carByStudentId);
            }
            return studentCars;
        } catch (SQLException ignored) {}
        return null;
    }

    public boolean delete(Car car) {
        String sql = "delete from cars where cars.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, car.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {}
        return false;
    }

    public boolean update(Car car) {
        String sql = "update cars set cars.brand=?, cars.power=?, cars.year=?, cars.id_s=? where cars.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setInt(2, car.getPower());
            preparedStatement.setInt(3, car.getYear());
            preparedStatement.setInt(4, car.getIdStudent());
            preparedStatement.setInt(5, car.getId());
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
