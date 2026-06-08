package com.fintrack.ui.auth;

import com.fintrack.ui.admin.AdminDashboardUI;
import com.fintrack.ui.user.UserDashboardUI;
import com.fintrack.utils.SessionManager;
import com.fintrack.config.DBConnection;


import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Color;
import java.awt.Font;
import com.fintrack.ui.auth.RoleSelectionUI;
public class LoginUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton loginButton;
    private JButton backButton;
    private JButton registerButton;

    public LoginUI() {

                                setTitle("FinTrack - Login");
                        setSize(900, 650);
                        setLocationRelativeTo(null);
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        setLayout(null);

                        getContentPane().setBackground(new Color(245, 247, 250));

                        JLabel titleLabel = new JLabel("FINTRACK LOGIN");
                        titleLabel.setBounds(300, 70, 500, 60);
                        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
                        titleLabel.setForeground(new Color(33, 37, 41));
                        add(titleLabel);

                        // ===== EMAIL =====
                        JLabel emailLabel = new JLabel("Email");
                        emailLabel.setBounds(230, 180, 120, 35);
                        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
                        add(emailLabel);

                        emailField = new JTextField();
                        emailField.setBounds(380, 180, 320, 45);
                        emailField.setFont(new Font("SansSerif", Font.PLAIN, 18));
                        add(emailField);

                        // ===== PASSWORD =====
                        JLabel passwordLabel = new JLabel("Password");
                        passwordLabel.setBounds(230, 260, 140, 35);
                        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
                        add(passwordLabel);

                        passwordField = new JPasswordField();
                        passwordField.setBounds(380, 260, 320, 45);
                        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 18));
                        add(passwordField);

                        // ===== ROLE =====
                        JLabel roleLabel = new JLabel("Role");
                        roleLabel.setBounds(230, 340, 120, 35);
                        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
                        add(roleLabel);

                        roleBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
                        roleBox.setBounds(380, 340, 320, 45);
                        roleBox.setFont(new Font("SansSerif", Font.PLAIN, 18));
                        add(roleBox);

                        // ===== LOGIN BUTTON =====
                        loginButton = new JButton("LOGIN");
                        loginButton.setBounds(300, 450, 160, 50);
                        loginButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                        loginButton.setBackground(new Color(0, 123, 255));
                        loginButton.setForeground(Color.WHITE);
                        loginButton.setFocusPainted(false);
                        add(loginButton);

                        // ===== BACK BUTTON =====
                        backButton = new JButton("BACK");
                        backButton.setBounds(500, 450, 160, 50);
                        backButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                        backButton.setBackground(new Color(108, 117, 125));
                        backButton.setForeground(Color.WHITE);
                        backButton.setFocusPainted(false);
                        add(backButton);
            
            
                        registerButton = new JButton("REGISTER");
                        registerButton.setBounds(640, 560, 200, 55);
                        registerButton.setFont(new Font("SansSerif", Font.BOLD, 22));
                        registerButton.setBackground(new Color(40, 167, 69));
                        registerButton.setForeground(Color.WHITE);
                        registerButton.setFocusPainted(false);
                        add(registerButton);

                        loginButton.addActionListener(e -> {
                              loginUser();
                                });

                        backButton.addActionListener(e -> {
                            dispose();
                            new RoleSelectionUI().setVisible(true);
                        });     
                        registerButton.addActionListener(e -> {
                        dispose();
                        new RegisterUI().setVisible(true);

});            
    }

    private void loginUser() {

        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please enter all fields");

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT * FROM users " +
                    "WHERE email=? AND password=? AND role=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, role);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("user_id");
                SessionManager.setUserId(userId);
                SessionManager.setCurrentUser(rs.getString("name"));

                String userName = rs.getString("name");

                SessionManager.setUserId(userId);

                JOptionPane.showMessageDialog(this,
                        "Login Successful");

                showUserNotifications(userId);

                dispose();

                if (role.equals("ADMIN")) {

                    AdminDashboardUI admin =
                            new AdminDashboardUI();

                    admin.setVisible(true);

                } else {

                    UserDashboardUI user =
                            new UserDashboardUI();

                    user.setVisible(true);
                }

            } else {

    JOptionPane.showMessageDialog(this,
            "Invalid Email or Password");
}            con.close();

        } catch (Exception e) {

    e.printStackTrace();

    JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage());
}
    }

    private void showUserNotifications(int userId) {

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT message FROM notifications " +
                    "WHERE user_id = ? AND status = 'UNREAD'";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String msg = rs.getString("message");

                JOptionPane.showMessageDialog(this,
                        msg,
                        "New Notification",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // MARK NOTIFICATIONS AS READ

            String updateQuery =
                    "UPDATE notifications " +
                    "SET status='READ' WHERE user_id=?";

            PreparedStatement pst2 =
                    con.prepareStatement(updateQuery);

            pst2.setInt(1, userId);

            pst2.executeUpdate();

            con.close();

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}