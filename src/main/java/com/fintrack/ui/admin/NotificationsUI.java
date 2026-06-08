package com.fintrack.ui.admin;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotificationsUI extends JFrame {

    JTextArea messageArea;

    DefaultListModel<String> model;

    JList<String> notificationList;

    public NotificationsUI() {

        setTitle("Notifications");

        setSize(900, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== TITLE =====

        JLabel title = new JLabel(
                "SYSTEM NOTIFICATIONS",
                JLabel.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        title.setOpaque(true);

        title.setBackground(
                new Color(20, 30, 70)
        );

        title.setForeground(Color.WHITE);

        title.setPreferredSize(
                new Dimension(100, 70)
        );

        add(title, BorderLayout.NORTH);

        // ===== INPUT PANEL =====

        JPanel inputPanel =
                new JPanel(new BorderLayout());

        messageArea = new JTextArea(4, 20);

        messageArea.setFont(
                new Font("Arial", Font.PLAIN, 18)
        );

        JScrollPane areaScroll =
                new JScrollPane(messageArea);

        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Enter Notification"
                )
        );

        inputPanel.add(areaScroll);

        add(inputPanel, BorderLayout.NORTH);

        // ===== LIST =====

        model = new DefaultListModel<>();

        notificationList =
                new JList<>(model);

        notificationList.setFont(
                new Font("Arial", Font.PLAIN, 18)
        );

        JScrollPane listScroll =
                new JScrollPane(notificationList);

        add(listScroll, BorderLayout.CENTER);

        // ===== BUTTONS =====

        JPanel buttonPanel = new JPanel();

        JButton sendBtn =
                new JButton("Send Notification");

        JButton refreshBtn =
                new JButton("Refresh");

        JButton backBtn =
                new JButton("Back");

        buttonPanel.add(sendBtn);

        buttonPanel.add(refreshBtn);

        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====

        sendBtn.addActionListener(e -> sendNotification());
        refreshBtn.addActionListener(e ->

                loadNotifications());

        backBtn.addActionListener(e -> {

            dispose();

            new AdminDashboardUI()
                    .setVisible(true);
        });

        // ===== LOAD =====

        loadNotifications();

        setVisible(true);
    }

    // ===== SEND NOTIFICATION =====

private void sendNotification() {

    String userName = JOptionPane.showInputDialog(this,
            "Enter Username:");

    String message = messageArea.getText().trim();

    if (userName == null || userName.isEmpty() || message.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Username and Message required");
        return;
    }

    try {
        Connection con = DBConnection.getConnection();

        // Get user_id from username
        String getUserQuery =
                "SELECT user_id FROM users WHERE name = ?";

        PreparedStatement pstUser =
                con.prepareStatement(getUserQuery);

        pstUser.setString(1, userName);

        ResultSet rs = pstUser.executeQuery();

        if (rs.next()) {

            int userId = rs.getInt("user_id");

            // Insert notification
            String insertQuery =
                    "INSERT INTO notifications(user_id, message) VALUES (?, ?)";

            PreparedStatement pst =
                    con.prepareStatement(insertQuery);

            pst.setInt(1, userId);
            pst.setString(2, message);

            int rows = pst.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(this,
                        "Notification Sent Successfully");

                messageArea.setText("");

                loadNotifications();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Failed To Send");
            }

        } else {

            JOptionPane.showMessageDialog(this,
                    "User Not Found");
        }

        con.close();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
    }
}
    // ===== LOAD NOTIFICATIONS =====

    private void loadNotifications() {

        try {

            model.clear();

            Connection con =
                    DBConnection.getConnection();

                String query =
                "SELECT message, created_at, status " +
                "FROM notifications " +
                "ORDER BY created_at DESC";
            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

String msg =
        rs.getString("message");

String time =
        rs.getString("created_at");

String status =
        rs.getString("status");

model.addElement(
        "[" + time + "] [" + status + "]  " + msg
);    
        }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Notifications"
            );

            e.printStackTrace();
        }
    }

    // ===== MAIN =====

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new NotificationsUI()
                    .setVisible(true);
        });
    }
}