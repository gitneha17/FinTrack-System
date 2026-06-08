package com.fintrack.ui.admin;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageUsersUI extends JFrame {

    JTable userTable;

    DefaultTableModel model;

    JTextField searchField;

    JButton searchBtn;
    JButton deleteBtn;
    JButton refreshBtn;
    JButton backBtn;

    public ManageUsersUI() {

        setTitle("Manage Users");

        setSize(1000, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== TOP PANEL =====

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel heading = new JLabel(
                "USER MANAGEMENT",
                JLabel.CENTER
        );

        heading.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        heading.setOpaque(true);

        heading.setBackground(
                new Color(20, 30, 70)
        );

        heading.setForeground(Color.WHITE);

        heading.setPreferredSize(
                new Dimension(100, 70)
        );

        topPanel.add(heading, BorderLayout.NORTH);

        // ===== SEARCH PANEL =====

        JPanel searchPanel = new JPanel();

        searchField = new JTextField(20);

        searchBtn = new JButton("Search");

        refreshBtn = new JButton("Refresh");

        deleteBtn = new JButton("Delete User");

        backBtn = new JButton("Back");

        searchPanel.add(new JLabel("Search Name:"));

        searchPanel.add(searchField);

        searchPanel.add(searchBtn);

        searchPanel.add(refreshBtn);

        searchPanel.add(deleteBtn);

        searchPanel.add(backBtn);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====

        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{

                "User ID",
                "Name",
                "Email",
                "Role"
        });

        userTable = new JTable(model);

        JScrollPane scrollPane =
                new JScrollPane(userTable);

        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON ACTIONS =====

        searchBtn.addActionListener(e -> searchUsers());

        refreshBtn.addActionListener(e -> loadUsers());

        deleteBtn.addActionListener(e -> deleteUser());

        backBtn.addActionListener(e -> {

            dispose();

            new AdminDashboardUI().setVisible(true);
        });

        // ===== LOAD USERS =====

        loadUsers();
    }

    // ===== LOAD USERS =====

    private void loadUsers() {

        try {

            Connection con =
                    DBConnection.getConnection();

            model.setRowCount(0);

            String query =
                    "SELECT * FROM users";

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{

                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Users"
            );

            e.printStackTrace();
        }
    }

    // ===== SEARCH USERS =====

    private void searchUsers() {

        try {

            Connection con =
                    DBConnection.getConnection();

            model.setRowCount(0);

            String keyword =
                    searchField.getText();

            String query =
                    "SELECT * FROM users " +
                    "WHERE name LIKE ?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(
                    1,
                    "%" + keyword + "%"
            );

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{

                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search Failed"
            );

            e.printStackTrace();
        }
    }

    // ===== DELETE USER =====

    private void deleteUser() {

        int row =
                userTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select User First"
            );

            return;
        }

        int userId =
                (int) model.getValueAt(row, 0);

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete Selected User?"
                );

        if (confirm != 0) {
            return;
        }

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "DELETE FROM users " +
                    "WHERE user_id = ?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, userId);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "User Deleted Successfully"
            );

            loadUsers();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Delete Failed"
            );

            e.printStackTrace();
        }
    }

    // ===== MAIN =====

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ManageUsersUI().setVisible(true);
        });
    }
}