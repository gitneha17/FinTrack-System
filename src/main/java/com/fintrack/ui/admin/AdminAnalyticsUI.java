package com.fintrack.ui.admin;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminAnalyticsUI extends JFrame {

    JLabel totalUsersLabel;
    JLabel personalExpenseLabel;
    JLabel groupExpenseLabel;
    JLabel overallExpenseLabel;
    JLabel topCategoryLabel;

    public AdminAnalyticsUI() {

        setTitle("Admin Analytics Dashboard");

        setSize(1100, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== TITLE =====

        JLabel title = new JLabel(
                "ADMIN ANALYTICS DASHBOARD",
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
                new Dimension(100, 80)
        );

        add(title, BorderLayout.NORTH);

        // ===== MAIN PANEL =====

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new GridLayout(
                2,
                3,
                20,
                20
        ));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );

        // ===== CARDS =====

        totalUsersLabel = createCard(
                mainPanel,
                "Total Users"
        );

        personalExpenseLabel = createCard(
                mainPanel,
                "Personal Expenses"
        );

        groupExpenseLabel = createCard(
                mainPanel,
                "Group Expenses"
        );

        overallExpenseLabel = createCard(
                mainPanel,
                "Overall Expenses"
        );

        topCategoryLabel = createCard(
                mainPanel,
                "Top Category"
        );

        add(mainPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====

        JPanel bottomPanel = new JPanel();

        JButton refreshBtn =
                new JButton("Refresh");

        JButton backBtn =
                new JButton("Back");

        bottomPanel.add(refreshBtn);

        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====

        refreshBtn.addActionListener(e ->
                loadAnalytics());

        backBtn.addActionListener(e -> {

            dispose();

            new AdminDashboardUI().setVisible(true);
        });

        // ===== LOAD DATA =====

        loadAnalytics();

        setVisible(true);
    }

    // ===== CREATE CARD =====

    private JLabel createCard(
            JPanel panel,
            String title
    ) {

        JPanel card = new JPanel();

        card.setLayout(new BorderLayout());

        card.setBackground(
                new Color(240, 245, 255)
        );

        card.setBorder(
                BorderFactory.createLineBorder(
                        new Color(180, 180, 180),
                        2
                )
        );

        JLabel heading = new JLabel(
                title,
                JLabel.CENTER
        );

        heading.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        heading.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        10,
                        10
                )
        );

        JLabel value = new JLabel(
                "Loading...",
                JLabel.CENTER
        );

        value.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        value.setForeground(
                new Color(20, 60, 120)
        );

        card.add(heading, BorderLayout.NORTH);

        card.add(value, BorderLayout.CENTER);

        panel.add(card);

        return value;
    }

    // ===== LOAD ANALYTICS =====

    private void loadAnalytics() {

        try {

            Connection con =
                    DBConnection.getConnection();

            // ===== TOTAL USERS =====

            String q1 =
                    "SELECT COUNT(*) FROM users";

            PreparedStatement pst1 =
                    con.prepareStatement(q1);

            ResultSet rs1 =
                    pst1.executeQuery();

            if (rs1.next()) {

                totalUsersLabel.setText(
                        String.valueOf(
                                rs1.getInt(1)
                        )
                );
            }

            // ===== PERSONAL EXPENSES =====

            String q2 =
                    "SELECT SUM(amount) " +
                    "FROM personal_expenses";

            PreparedStatement pst2 =
                    con.prepareStatement(q2);

            ResultSet rs2 =
                    pst2.executeQuery();

            double personal = 0;

            if (rs2.next()) {

                personal =
                        rs2.getDouble(1);

                personalExpenseLabel.setText(
                        "₹ " + personal
                );
            }

            // ===== GROUP EXPENSES =====

            String q3 =
                    "SELECT SUM(amount) " +
                    "FROM group_expenses";

            PreparedStatement pst3 =
                    con.prepareStatement(q3);

            ResultSet rs3 =
                    pst3.executeQuery();

            double group = 0;

            if (rs3.next()) {

                group =
                        rs3.getDouble(1);

                groupExpenseLabel.setText(
                        "₹ " + group
                );
            }

            // ===== OVERALL =====

            double overall =
                    personal + group;

            overallExpenseLabel.setText(
                    "₹ " + overall
            );

            // ===== TOP CATEGORY =====

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

                topCategoryLabel.setText(
                        rs4.getString(
                                "category"
                        )
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Analytics"
            );

            e.printStackTrace();
        }
    }

    // ===== MAIN =====

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new AdminAnalyticsUI()
                    .setVisible(true);
        });
    }
}