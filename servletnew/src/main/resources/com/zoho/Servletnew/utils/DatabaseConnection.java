package com.zoho.Servletnew.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vendor", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;

    }
}