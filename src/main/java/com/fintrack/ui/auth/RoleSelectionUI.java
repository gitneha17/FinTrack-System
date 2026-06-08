package com.fintrack.ui.auth;

import javax.swing.*;
import java.awt.*;

public class RoleSelectionUI extends JFrame {

    public RoleSelectionUI() {
        setTitle("FinTrack - Select Role");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Select Your Role", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton userBtn = new JButton("Continue as User");
        JButton adminBtn = new JButton("Continue as Admin");

        // Actions
        userBtn.addActionListener(e -> {
            dispose();
new LoginUI().setVisible(true);
        });

        adminBtn.addActionListener(e -> {
            dispose();
new LoginUI().setVisible(true);
        });

        panel.add(title);
        panel.add(userBtn);
        panel.add(adminBtn);

        add(panel);
        setVisible(true);
    }
}