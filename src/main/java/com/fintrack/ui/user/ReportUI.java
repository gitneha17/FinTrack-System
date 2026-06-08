package com.fintrack.ui.user;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class ReportUI extends JFrame {

    private JTable expenseTable;

    private JLabel totalExpenseLabel;
    private JLabel highestExpenseLabel;
    private JLabel totalCategoryLabel;
    private JLabel totalRecordLabel;

    public ReportUI() {

        setTitle("FinTrack Reports");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(new Color(245, 247, 250));

        // ================= TITLE =================

        JLabel title = new JLabel("Expense Reports & Analytics");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(40, 20, 500, 40);
        add(title);

        // ================= SUMMARY CARDS =================

        JPanel totalCard = createCard("Total Expense", "₹0");
        totalCard.setBounds(40, 90, 250, 110);
        add(totalCard);

        JPanel highestCard = createCard("Highest Expense", "₹0");
        highestCard.setBounds(320, 90, 250, 110);
        add(highestCard);

        JPanel categoryCard = createCard("Categories", "0");
        categoryCard.setBounds(600, 90, 250, 110);
        add(categoryCard);

        JPanel recordCard = createCard("Records", "0");
        recordCard.setBounds(880, 90, 250, 110);
        add(recordCard);

        totalExpenseLabel = (JLabel) totalCard.getComponent(1);
        highestExpenseLabel = (JLabel) highestCard.getComponent(1);
        totalCategoryLabel = (JLabel) categoryCard.getComponent(1);
        totalRecordLabel = (JLabel) recordCard.getComponent(1);

        // ================= TABLE PANEL =================

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBounds(40, 240, 1090, 320);

        tablePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel tableTitle = new JLabel("Expense Records");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 20));

        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {
                "ID",
                "Title",
                "Category",
                "Amount",
                "Date"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0);

        expenseTable = new JTable(model);

        expenseTable.setRowHeight(28);
        expenseTable.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane =
                new JScrollPane(expenseTable);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel);

        // ================= BUTTONS =================

        JButton refreshBtn =
                new JButton("Refresh Data");

        refreshBtn.setBounds(40, 590, 180, 40);

        styleBlueButton(refreshBtn);

        add(refreshBtn);

        JButton closeBtn =
                new JButton("Close");

        closeBtn.setBounds(250, 590, 180, 40);

        styleRedButton(closeBtn);

        add(closeBtn);

        // ================= ACTIONS =================

        refreshBtn.addActionListener(e -> {
            loadReportData();
        });

        closeBtn.addActionListener(e -> {
            dispose();
        });

        // ================= LOAD DATA =================

        loadReportData();

        setVisible(true);
    }

    // ================= CREATE CARD =================

    private JPanel createCard(String title, String value) {

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(2, 1));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        JLabel titleLabel =
                new JLabel(title, JLabel.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        JLabel valueLabel =
                new JLabel(value, JLabel.CENTER);

        valueLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        valueLabel.setForeground(
                new Color(33, 150, 243)
        );

        panel.add(titleLabel);
        panel.add(valueLabel);

        return panel;
    }

    // ================= LOAD REPORT DATA =================

    private void loadReportData() {

        try {

            Connection con =
                    DBConnection.getConnection();

            // ================= TOTAL EXPENSE =================

            PreparedStatement ps1 =
                    con.prepareStatement(
                            "SELECT SUM(amount) AS total FROM personal_expenses"
                    );

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {

                double total =
                        rs1.getDouble("total");

                totalExpenseLabel.setText("₹" + total);
            }

            // ================= HIGHEST EXPENSE =================

            PreparedStatement ps2 =
                    con.prepareStatement(
                            "SELECT MAX(amount) AS highest FROM personal_expenses"
                    );

            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {

                double highest =
                        rs2.getDouble("highest");

                highestExpenseLabel.setText("₹" + highest);
            }

            // ================= TOTAL CATEGORIES =================

            PreparedStatement ps3 =
                    con.prepareStatement(
                            "SELECT COUNT(DISTINCT category) AS total_categories FROM personal_expenses"
                    );

            ResultSet rs3 = ps3.executeQuery();

            if (rs3.next()) {

                totalCategoryLabel.setText(
                        rs3.getString("total_categories")
                );
            }

            // ================= TOTAL RECORDS =================

            PreparedStatement ps4 =
                    con.prepareStatement(
                            "SELECT COUNT(*) AS total_records FROM personal_expenses"
                    );

            ResultSet rs4 = ps4.executeQuery();

            if (rs4.next()) {

                totalRecordLabel.setText(
                        rs4.getString("total_records")
                );
            }

            // ================= LOAD TABLE DATA =================

            DefaultTableModel model =
                    (DefaultTableModel) expenseTable.getModel();

            model.setRowCount(0);

            PreparedStatement ps5 =
                    con.prepareStatement(
                            "SELECT * FROM personal_expenses ORDER BY expense_date DESC"
                    );

            ResultSet rs5 = ps5.executeQuery();

            while (rs5.next()) {

                int id =
                        rs5.getInt("expense_id");

                String title =
                        rs5.getString("title");

                String category =
                        rs5.getString("category");

                double amount =
                        rs5.getDouble("amount");

                Timestamp date =
                        rs5.getTimestamp("expense_date");

                model.addRow(new Object[]{
                        id,
                        title,
                        category,
                        amount,
                        date
                });
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Reports"
            );
        }
    }

    // ================= BUTTON STYLE =================

    private void styleBlueButton(JButton button) {

        button.setFocusPainted(false);

        button.setBackground(
                new Color(33, 150, 243)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font("Arial", Font.BOLD, 14)
        );
    }

    private void styleRedButton(JButton button) {

        button.setFocusPainted(false);

        button.setBackground(
                new Color(220, 53, 69)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font("Arial", Font.BOLD, 14)
        );
    }
}