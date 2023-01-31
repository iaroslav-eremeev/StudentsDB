package com.iaroslaveremeev.repository;

import com.iaroslaveremeev.model.Auto;
import com.iaroslaveremeev.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AutoRepository implements AutoCloseable {

    private Connection conn;

    public AutoRepository() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Получение класса, который мы не создавали - из библиотеки (регистрация класса)
        this.conn = DriverManager.getConnection(Constants.DB_URL,
                Constants.USERNAME, Constants.PASSWORD);
    }

    public boolean add(Auto auto) {
        String sql = "insert into auto(brand, power, year, id_s) values (?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, auto.getBrand());
            preparedStatement.setInt(2, auto.getPower());
            preparedStatement.setInt(3, auto.getYear());
            preparedStatement.setInt(4, auto.getIdStudent());

            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    auto.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {
            //ignored.printStackTrace();
        }
        return false;
    }

    public List<Auto> getAuto() {
        String sql = "select * from auto";
        ArrayList<Auto> autos = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Auto auto = new Auto();
                auto.setId(resultSet.getInt(1));
                auto.setBrand(resultSet.getString(2));
                auto.setPower(resultSet.getInt(3));
                auto.setYear(resultSet.getInt(4));
                auto.setIdStudent(resultSet.getInt(5));

                autos.add(auto);
            }
        } catch (SQLException e) {
        }
        return autos;
    }

    public Auto getById(int id) {
        String sql = "select * from auto where auto.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Auto autoById = new Auto();
            autoById.setId(resultSet.getInt(1));
            autoById.setBrand(resultSet.getString(2));
            autoById.setPower(resultSet.getInt(3));
            autoById.setYear(resultSet.getInt(4));
            autoById.setIdStudent(resultSet.getInt(5));
            return autoById;
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean delete(Auto auto) {
        String sql = "delete from auto where auto.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, auto.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean update(Auto auto) {
        String sql = "update auto set auto.brand=?, auto.power=?, auto.year=?, auto.id_s=? where auto.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, auto.getBrand());
            preparedStatement.setInt(2, auto.getPower());
            preparedStatement.setInt(3, auto.getYear());
            preparedStatement.setInt(4, auto.getIdStudent());
            preparedStatement.setInt(5, auto.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {

        }
        return false;
    }

    @Override
    public void close() throws Exception {
        if (this.conn != null)
            this.conn.close();
    }

}
