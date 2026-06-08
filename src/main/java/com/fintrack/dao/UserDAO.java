package com.fintrack.dao;

import com.fintrack.config.DBConnection;
import com.fintrack.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // ✅ REGISTER USER
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());      // MUST be getName()
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            int result = ps.executeUpdate();

            return result > 0;

        } catch (Exception e) {
            System.out.println("❌ REGISTER ERROR:");
            e.printStackTrace();
            return false;
        }
    }

    // ✅ LOGIN USER
    public boolean loginUser(String name, String password, String role) {
        String sql = "SELECT * FROM users WHERE name=? AND password=? AND role=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);        // MUST match DB column "name"
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.out.println("❌ LOGIN ERROR:");
            e.printStackTrace();
            return false;
        }
    }
}