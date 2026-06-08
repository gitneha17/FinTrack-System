package com.fintrack.ui.admin;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminReportsUI extends JFrame {

    JTextArea reportArea;

    public AdminReportsUI() {

        setTitle("Admin Reports");

        setSize(950, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== TITLE =====

        JLabel title = new JLabel(
                "SYSTEM REPORTS",
                JLabel.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        title.setOpaque(true);

        title.setBackground(
                new Color(15, 25, 60)
        );

        title.setForeground(Color.WHITE);

        title.setPreferredSize(
                new Dimension(100, 70)
        );

        add(title, BorderLayout.NORTH);

        // ===== REPORT AREA =====

        reportArea = new JTextArea();

        reportArea.setFont(
                new Font("Monospaced", Font.PLAIN, 18)
        );

        reportArea.setEditable(false);

        JScrollPane scrollPane =
                new JScrollPane(reportArea);

        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====

        JPanel bottomPanel = new JPanel();

        JButton refreshBtn =
                new JButton("Generate Report");

        JButton backBtn =
                new JButton("Back");

        bottomPanel.add(refreshBtn);

        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====

        refreshBtn.addActionListener(e ->
                loadReport());

        backBtn.addActionListener(e -> {

            dispose();

            new AdminDashboardUI()
                    .setVisible(true);
        });

        // ===== LOAD REPORT =====

        loadReport();

        setVisible(true);
    }

    // ===== LOAD REPORT =====

    private void loadReport() {

        try {

            Connection con =
                    DBConnection.getConnection();

            // ===== TOTAL USERS =====

            int totalUsers = 0;

            String q1 =
                    "SELECT COUNT(*) FROM users";

            PreparedStatement pst1 =
                    con.prepareStatement(q1);

            ResultSet rs1 =
                    pst1.executeQuery();

            if (rs1.next()) {

                totalUsers =
                        rs1.getInt(1);
            }

            // ===== PERSONAL TOTAL =====

            double personalTotal = 0;

            String q2 =
                    "SELECT SUM(amount) " +
                    "FROM personal_expenses";

            PreparedStatement pst2 =
                    con.prepareStatement(q2);

            ResultSet rs2 =
                    pst2.executeQuery();

            if (rs2.next()) {

                personalTotal =
                        rs2.getDouble(1);
            }

            // ===== GROUP TOTAL =====

            double groupTotal = 0;

            String q3 =
                    "SELECT SUM(amount) " +
                    "FROM group_expenses";

            PreparedStatement pst3 =
                    con.prepareStatement(q3);

            ResultSet rs3 =
                    pst3.executeQuery();

            if (rs3.next()) {

                groupTotal =
                        rs3.getDouble(1);
            }

            // ===== OVERALL =====

            double overall =
                    personalTotal + groupTotal;

            // ===== TOP CATEGORY =====

            String topCategory =
                    "N/A";

            String q4 =
                    "SELECT category, " +
                    "SUM(amount) total " +
                    "FROM personal_expenses " +
                    "GROUP BY category " +
                    "ORDER BY total DESC " +
                    "LIMIT 1";

            PreparedStatement pst4 =
                    con.prepareStatement(q4);

            ResultSet rs4 =
                    pst4.executeQuery();

            if (rs4.next()) {

                topCategory =
                        rs4.getString(
                                "category"
                        );
            }

            // ===== FINAL REPORT =====

            reportArea.setText(

                    "========== FINTRACK SYSTEM REPORT ==========\n\n" +

                    "Total Registered Users : " +
                    totalUsers + "\n\n" +

                    "Total Personal Expenses : ₹ " +
                    personalTotal + "\n\n" +

                    "Total Group Expenses : ₹ " +
                    groupTotal + "\n\n" +

                    "Overall System Expenses : ₹ " +
                    overall + "\n\n" +

                    "Top Spending Category : " +
                    topCategory + "\n\n" +

                    "============================================"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error Generating Report"
            );

            e.printStackTrace();
        }
    }

    // ===== MAIN =====

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new AdminReportsUI()
                    .setVisible(true);
        });
    }
}