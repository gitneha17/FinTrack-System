package com.fintrack.ui.admin;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GroupMonitorUI extends JFrame {

    JTable groupTable;

    DefaultTableModel model;

    JTextField searchField;

    JLabel totalExpenseLabel;

    JButton searchBtn;
    JButton refreshBtn;
    JButton backBtn;

    public GroupMonitorUI() {

        setTitle("Group Expense Monitor");

        setSize(1100, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== TITLE =====

        JLabel title = new JLabel(
                "GROUP EXPENSE MONITORING",
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

        // ===== TOP PANEL =====

        JPanel topPanel = new JPanel();

        searchField = new JTextField(20);

        searchBtn = new JButton("Search");

        refreshBtn = new JButton("Refresh");

        backBtn = new JButton("Back");

        topPanel.add(new JLabel("Search User:"));

        topPanel.add(searchField);

        topPanel.add(searchBtn);

        topPanel.add(refreshBtn);

        topPanel.add(backBtn);

        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // ===== TABLE =====

        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{

                "Expense ID",
                "Group ID",
                "Paid By",
                "Amount",
                "Description",
                "Expense Date"
        });

        groupTable = new JTable(model);

        JScrollPane scrollPane =
                new JScrollPane(groupTable);

        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====

        JPanel bottomPanel = new JPanel();

        JLabel totalLabel =
                new JLabel("Total Group Expense: ");

        totalLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        totalExpenseLabel =
                new JLabel("₹ 0");

        totalExpenseLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        bottomPanel.add(totalLabel);

        bottomPanel.add(totalExpenseLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====

        searchBtn.addActionListener(e ->
                searchExpenses());

        refreshBtn.addActionListener(e ->
                loadExpenses());

        backBtn.addActionListener(e -> {

            dispose();

            new AdminDashboardUI()
                    .setVisible(true);
        });

        // ===== LOAD DATA =====

        loadExpenses();

        setVisible(true);
    }

    // ===== LOAD GROUP EXPENSES =====

    private void loadExpenses() {

        try {

            Connection con =
                    DBConnection.getConnection();

            model.setRowCount(0);

            double total = 0;

            String query =
                    "SELECT * FROM group_expenses";

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                double amount =
                        rs.getDouble("amount");

                total += amount;

                model.addRow(new Object[]{

                        rs.getInt("expense_id"),
                        rs.getInt("group_id"),
                        rs.getString("paid_by"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getDate("expense_date")
                });
            }

            totalExpenseLabel.setText(
                    "₹ " + total
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Group Expenses"
            );

            e.printStackTrace();
        }
    }

    // ===== SEARCH =====

    private void searchExpenses() {

        try {

            Connection con =
                    DBConnection.getConnection();

            model.setRowCount(0);

            String keyword =
                    searchField.getText();

            String query =
                    "SELECT * FROM group_expenses " +
                    "WHERE paid_by LIKE ?";

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

                        rs.getInt("expense_id"),
                        rs.getInt("group_id"),
                        rs.getString("paid_by"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getDate("expense_date")
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

    // ===== MAIN =====

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new GroupMonitorUI()
                    .setVisible(true);
        });
    }
}