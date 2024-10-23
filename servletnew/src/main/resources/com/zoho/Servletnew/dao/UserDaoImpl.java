package com.zoho.Servletnew.dao;
import com.zoho.Servletnew.dao.UserDao;
import com.zoho.Servletnew.bo.UserBo;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zoho.Servletnew.bo.UserBo;
import com.zoho.Servletnew.utils.DatabaseConnection;

import org.mindrot.jbcrypt.BCrypt;

public class UserDaoImpl implements UserDao {
    public int register(UserBo user) throws SQLException, NoSuchAlgorithmException {
        int status=0;
        int count = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {

            String sql1 = "SELECT COUNT(*) FROM user where email = ?";
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql1);
            statement.setString(1, user.getEmail());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            String hashtext = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());


            if (count == 0) {
                String sql = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, hashtext);
                System.out.print("user added");
                status = statement.executeUpdate();
                return status;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (statement != null) try {
                statement.close();
            } catch (SQLException ignored) {
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public boolean validateUser(String email, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT password FROM user WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
}
