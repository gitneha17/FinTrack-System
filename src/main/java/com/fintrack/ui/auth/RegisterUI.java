package com.fintrack.ui.auth;

import com.fintrack.config.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterUI extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton registerButton;
    private JButton backButton;

    public RegisterUI() {

        setTitle("FinTrack - Register");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setBounds(180, 40, 400, 50);
        add(titleLabel);

        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameLabel.setBounds(120, 140, 180, 40);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(300, 140, 250, 40);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        add(nameField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        emailLabel.setBounds(120, 220, 180, 40);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(300, 220, 250, 40);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        passwordLabel.setBounds(120, 300, 180, 40);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(300, 300, 250, 40);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        add(passwordField);

        registerButton = new JButton("REGISTER");
        registerButton.setBounds(170, 400, 180, 50);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        add(registerButton);

        backButton = new JButton("BACK");
        backButton.setBounds(380, 400, 180, 50);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        backButton.setBackground(Color.GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        add(backButton);

        registerButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e -> {

            dispose();

            new LoginUI().setVisible(true);

        });
    }

    private void registerUser() {

        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill all fields");

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            String query =
                    "INSERT INTO users(name, email, password, role) VALUES (?, ?, ?, ?)";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setString(4, "USER");

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Registration Successful");

            dispose();

            new LoginUI().setVisible(true);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Registration Failed");

            e.printStackTrace();
        }
    }
}