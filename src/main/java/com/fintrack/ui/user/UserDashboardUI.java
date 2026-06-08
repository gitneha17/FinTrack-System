package com.fintrack.ui.user;

import javax.swing.*;

import com.fintrack.ui.auth.LoginUI;

import java.awt.*;

public class UserDashboardUI extends JFrame {

    public UserDashboardUI() {

        setTitle("FinTrack - User Dashboard");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // MAIN BACKGROUND
        JPanel background = new JPanel();
        background.setBackground(new Color(245, 247, 250));
        background.setLayout(null);
        setContentPane(background);

        // TITLE
        JLabel title = new JLabel("💰 Welcome to FinTrack");
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setBounds(280, 40, 500, 50);
        background.add(title);

        // CENTER CARD PANEL
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBounds(220, 130, 540, 350);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220),2));
        background.add(card);

        // BUTTON STYLE METHOD
        Font btnFont = new Font("SansSerif", Font.BOLD, 16);

        JButton personalBtn = createButton("🧾 Personal Expense",
                new Color(33,150,243), btnFont);

        JButton viewBtn = createButton("📋 View Expenses",
                new Color(30,136,229), btnFont);

        JButton analyticsBtn = createButton("📊 Analytics",
                new Color(103,58,183), btnFont);

        JButton groupBtn = createButton("👥 Group Expense",
                new Color(76,175,80), btnFont);

        JButton reportBtn = createButton("📑 View Reports",
                new Color(255,152,0), btnFont);

        JButton logoutBtn = createButton("🚪 Logout",
                new Color(229,57,53), btnFont);

        // BUTTON POSITIONS

        personalBtn.setBounds(60, 50, 180, 60);
        viewBtn.setBounds(300, 50, 180, 60);

        analyticsBtn.setBounds(60, 140, 180, 60);
        groupBtn.setBounds(300, 140, 180, 60);

        reportBtn.setBounds(60, 230, 180, 60);
        logoutBtn.setBounds(300, 230, 180, 60);

        // ADD BUTTONS
        card.add(personalBtn);
        card.add(viewBtn);
        card.add(analyticsBtn);
        card.add(groupBtn);
        card.add(reportBtn);
        card.add(logoutBtn);

        // BUTTON ACTIONS

personalBtn.addActionListener(e -> {
    new AddExpenseUI();
});

viewBtn.addActionListener(e -> {
    new ViewExpenseUI();
});

analyticsBtn.addActionListener(e -> {
    new ExpenseChartUI();
});

groupBtn.addActionListener(e -> {
    new GroupUI();
});

reportBtn.addActionListener(e -> {
    new ReportUI();
});

logoutBtn.addActionListener(e -> {

    int choice = JOptionPane.showConfirmDialog(
            this,
            "Do you want to logout?",
            "Logout",
            JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {
        dispose();
    }
});

        setVisible(true);
    }

    // BUTTON DESIGN METHOD
    private JButton createButton(String text, Color color, Font font) {

        JButton btn = new JButton(text);

        btn.setFont(font);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }
    // Blue Button
    private void styleBlueButton(JButton button) {

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
    }

    // Green Button
    private void styleGreenButton(JButton button) {

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);
    }

    // Orange Button
    private void styleOrangeButton(JButton button) {

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(255, 152, 0));
        button.setForeground(Color.WHITE);
    }

    // Purple Button
    private void stylePurpleButton(JButton button) {
        button.setBackground(new Color(111, 66, 193));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // Red Button
    private void styleRedButton(JButton button) {

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
    }
}