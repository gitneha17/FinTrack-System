package com.fintrack.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/fintrack";
            String user = "root";
            String password = "Neha@1775";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database Connected Successfully");
            return conn;

        } catch (Exception e) {
            System.out.println("Database Connection Failed");
            e.printStackTrace();
            return null;
        }
    }
}