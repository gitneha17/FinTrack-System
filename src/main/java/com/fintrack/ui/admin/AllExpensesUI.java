package com.fintrack.ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.fintrack.config.DBConnection;

import java.awt.*;
import java.sql.*;
import com.fintrack.ui.admin.AdminDashboardUI;

public class AllExpensesUI extends JFrame {

    JTable expenseTable;

    DefaultTableModel model;

    JTextField searchField;

    JLabel totalLabel;

    JButton searchBtn;
    JButton refreshBtn;
    JButton backBtn;

    Connection con;

    public AllExpensesUI() {

        setTitle("FinTrack - All Expenses");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeDB();

        // ===== COLORS =====
        Color bgColor = new Color(15, 23, 42);
        Color panelColor = new Color(30, 41, 59);
        Color btnColor = new Color(59, 130, 246);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgColor);

        JLabel title = new JLabel("ALL EXPENSES", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        topPanel.add(title, BorderLayout.NORTH);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(panelColor);

        JLabel searchLabel = new JLabel("Search Category:");
        searchLabel.setForeground(Color.WHITE);

        searchField = new JTextField(20);

        searchBtn = createButton("Search", btnColor);
        refreshBtn = createButton("Refresh", btnColor);
        backBtn = createButton("Back", Color.GRAY);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        searchPanel.add(backBtn);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // ===== TABLE =====
        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{

                "Expense ID",
                "User Name",
                "Title",
                "Category",
                "Amount",
                "Expense Date",
        });

        expenseTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(expenseTable);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(panelColor);

        totalLabel = new JLabel("Total Expenses: ₹0");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        bottomPanel.add(totalLabel);

        // ===== ADD =====
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ===== LOAD DATA =====
        loadExpenses();

        // ===== BUTTON ACTIONS =====
        refreshBtn.addActionListener(e -> loadExpenses());

        searchBtn.addActionListener(e -> searchExpenses());

                backBtn.addActionListener(e -> {

            dispose();
            new AdminDashboardUI().setVisible(true);
        });

        setVisible(true);
    }

    // ===== DATABASE =====
    private void initializeDB() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/fintrack",
                    "root",
                    "Neha@1775"
            );

            System.out.println("Database Connected");

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Database Connection Failed");
        }
    }

    // ===== LOAD EXPENSES =====
    private void loadExpenses() {
        Connection con = DBConnection.getConnection();

        try {

            model.setRowCount(0);

            double total = 0;

            String query =

                "SELECT " +
                "expense_id, " +
                "user_name, " +
                "title, " +
                "category, " +
                "amount, " +
                "expense_date " +
                "FROM personal_expenses " +

                "UNION ALL " +

                "SELECT " +
                "expense_id, " +
                "paid_by AS user_name, " +
                "'GROUP EXPENSE' AS title, " +
                "'GROUP' AS category, " +
                "amount, " +
                "expense_date " +
                "FROM group_expenses " +

                "ORDER BY expense_date DESC";
            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                double amount = rs.getDouble("amount");

                total += amount;

                model.addRow(new Object[]{

                    rs.getString("expense_type"),
                    rs.getString("user_name"),
                    rs.getString("category"),
                    rs.getDouble("amount"),
                    rs.getDate("expense_date")

                });            }

            totalLabel.setText("Total Expenses: ₹" + total);

        } catch (Exception e) {

    System.out.println(e.getMessage());

}
    }

    // ===== SEARCH =====
    private void searchExpenses() {
        Connection con = DBConnection.getConnection();

        try {

            model.setRowCount(0);

            double total = 0;

            String keyword = searchField.getText();

            String query =

                "SELECT * FROM (" +

                "SELECT " +
                "expense_id, " +
                "user_name, " +
                "title, " +
                "category, " +
                "amount, " +
                "expense_date " +
                "FROM personal_expenses " +

                "UNION ALL " +

                "SELECT " +
                "expense_id, " +
                "paid_by AS user_name, " +
                "'GROUP EXPENSE' AS title, " +
                "'GROUP' AS category, " +
                "amount, " +
                "expense_date " +
                "FROM group_expenses " +

                ") AS all_expenses " +

                "WHERE category LIKE ?";
                
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                double amount = rs.getDouble("amount");

                total += amount;

                model.addRow(new Object[]{

                rs.getString("user_name"),
                rs.getString("category"),
                rs.getDouble("amount"),
                rs.getDate("expense_date")

            });            }

            totalLabel.setText("Total Expenses: ₹" + total);

        } catch (Exception e) {
    e.printStackTrace();

    JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage());
}
    }

    // ===== BUTTON =====
    private JButton createButton(String text, Color color) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);

        btn.setBackground(color);

        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("SansSerif", Font.BOLD, 14));

        return btn;
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new AllExpensesUI();
        });
    }
}