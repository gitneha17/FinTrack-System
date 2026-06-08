package com.fintrack.ui.admin;

import javax.swing.*;
import java.awt.*;
import com.fintrack.ui.admin.ManageUsersUI;
import com.fintrack.ui.auth.LoginUI;
import com.fintrack.ui.admin.AllExpensesUI;
import com.fintrack.ui.admin.GroupMonitorUI;
import com.fintrack.ui.admin.AdminAnalyticsUI;
import com.fintrack.ui.admin.AdminReportsUI;
import com.fintrack.ui.admin.NotificationsUI;

public class AdminDashboardUI extends JFrame {

    JButton manageUsersBtn;
    JButton viewExpensesBtn;
    JButton groupMonitoringBtn;
    JButton analyticsBtn;
    JButton reportsBtn;
    JButton notificationsBtn;
    JButton logoutBtn;

    public AdminDashboardUI() {

        setTitle("FinTrack - Admin Dashboard");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== COLORS =====
        Color bgColor = new Color(15, 23, 42);
        Color cardColor = new Color(30, 41, 59);
        Color btnColor = new Color(59, 130, 246);
        Color textColor = Color.WHITE;

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgColor);
        mainPanel.setLayout(new BorderLayout());

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setBackground(bgColor);
        topPanel.setPreferredSize(new Dimension(1000, 100));

        JLabel title = new JLabel("ADMIN DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        topPanel.add(title);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(bgColor);
        centerPanel.setLayout(new GridLayout(3, 2, 25, 25));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        manageUsersBtn = createButton("👤 Manage Users", btnColor, textColor);
        viewExpensesBtn = createButton("💰 View Expenses", btnColor, textColor);
        groupMonitoringBtn = createButton("👥 Group Monitoring", btnColor, textColor);
        analyticsBtn = createButton("📊 Analytics", btnColor, textColor);
        reportsBtn = createButton("📑 Reports", btnColor, textColor);
        notificationsBtn = createButton("🔔 Notifications", btnColor, textColor);

        centerPanel.add(manageUsersBtn);
        centerPanel.add(viewExpensesBtn);
        centerPanel.add(groupMonitoringBtn);
        centerPanel.add(analyticsBtn);
        centerPanel.add(reportsBtn);
        centerPanel.add(notificationsBtn);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bgColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        logoutBtn = createButton("🚪 Logout", new Color(239, 68, 68), textColor);
        logoutBtn.setPreferredSize(new Dimension(180, 50));

        bottomPanel.add(logoutBtn);

        // ===== ADD COMPONENTS =====
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ===== BUTTON ACTIONS =====

        manageUsersBtn.addActionListener(e -> {

            new ManageUsersUI().setVisible(true);

            dispose();
        });

        viewExpensesBtn.addActionListener(e -> {

            new AllExpensesUI().setVisible(true);

            dispose();
        });

        groupMonitoringBtn.addActionListener(e -> {

            new GroupMonitorUI().setVisible(true);

            dispose();
        });
        analyticsBtn.addActionListener(e -> {

            new AdminAnalyticsUI().setVisible(true);

            dispose();
        });

        reportsBtn.addActionListener(e -> {

            new AdminReportsUI().setVisible(true);

            dispose();
        });

        notificationsBtn.addActionListener(e -> {

            new NotificationsUI().setVisible(true);

            dispose();
        });

        logoutBtn.addActionListener(e -> {

            dispose();

        new LoginUI().setVisible(true);
    });

        setVisible(true);
    }

    // ===== CUSTOM BUTTON METHOD =====
    private JButton createButton(String text, Color bg, Color fg) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBackground(bg);
        btn.setForeground(fg);

        btn.setFont(new Font("SansSerif", Font.BOLD, 18));

        btn.setPreferredSize(new Dimension(250, 100));

        btn.setBorder(BorderFactory.createEmptyBorder());

        return btn;
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new AdminDashboardUI();
        });
    }
}