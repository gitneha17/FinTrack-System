package com.fintrack.ui.user;

import com.fintrack.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GroupUI extends JFrame {

    JTextField groupNameField;
    JTextField memberField;
    JTextField amountField;
    JTextField descField;

    JComboBox<String> groupBox;

    JTextArea summaryArea;

    DefaultTableModel tableModel;

public GroupUI() {

    setTitle("FinTrack - Group Expense Manager");
    setSize(1000, 650);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel mainPanel = new JPanel(new BorderLayout(15,15));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    mainPanel.setBackground(new Color(245,247,250));

    // ===== TITLE =====

    JLabel title = new JLabel("Group Expense Manager");
    title.setFont(new Font("Segoe UI", Font.BOLD, 28));
    title.setForeground(new Color(33,37,41));

    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    titlePanel.setBackground(new Color(245,247,250));
    titlePanel.add(title);

    mainPanel.add(titlePanel, BorderLayout.NORTH);

    // ===== CENTER PANEL =====

    JPanel centerPanel = new JPanel(new GridLayout(1,2,20,20));
    centerPanel.setBackground(new Color(245,247,250));

    // ===== LEFT FORM PANEL =====

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridLayout(12,1,10,10));
    formPanel.setBorder(BorderFactory.createTitledBorder("Manage Group"));
    formPanel.setBackground(Color.WHITE);

    groupNameField = new JTextField();
    memberField = new JTextField();
    amountField = new JTextField();
    descField = new JTextField();

    groupBox = new JComboBox<>();

    JButton createGroupBtn = new JButton("Create Group");
    JButton addMemberBtn = new JButton("Add Member");
    JButton addExpenseBtn = new JButton("Add Shared Expense");
    JButton summaryBtn = new JButton("Settlement Summary");

    styleButton(createGroupBtn, new Color(52,152,219));
    styleButton(addMemberBtn, new Color(46,204,113));
    styleButton(addExpenseBtn, new Color(155,89,182));
    styleButton(summaryBtn, new Color(230,126,34));

    formPanel.add(new JLabel("Group Name"));
    formPanel.add(groupNameField);

    formPanel.add(createGroupBtn);

    formPanel.add(new JLabel("Member Name"));
    formPanel.add(memberField);

    formPanel.add(addMemberBtn);

    formPanel.add(new JLabel("Select Group"));
    formPanel.add(groupBox);

    formPanel.add(new JLabel("Amount"));
    formPanel.add(amountField);

    formPanel.add(new JLabel("Description"));
    formPanel.add(descField);

    centerPanel.add(formPanel);

    // ===== RIGHT PANEL =====

    JPanel rightPanel = new JPanel(new BorderLayout(10,10));
    rightPanel.setBorder(BorderFactory.createTitledBorder("Expenses & Summary"));
    rightPanel.setBackground(Color.WHITE);

    tableModel = new DefaultTableModel();

    JTable table = new JTable(tableModel);

    table.setRowHeight(25);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    tableModel.addColumn("Expense ID");
    tableModel.addColumn("Group");
    tableModel.addColumn("Amount");
    tableModel.addColumn("Description");

    JScrollPane tableScroll = new JScrollPane(table);

    rightPanel.add(tableScroll, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new BorderLayout(10,10));
    bottomPanel.setBackground(Color.WHITE);

    summaryArea = new JTextArea(5,20);
    summaryArea.setEditable(false);
    summaryArea.setFont(new Font("Segoe UI", Font.BOLD, 14));

    JScrollPane summaryScroll = new JScrollPane(summaryArea);

    JPanel btnPanel = new JPanel(new GridLayout(1,2,10,10));
    btnPanel.setBackground(Color.WHITE);

    btnPanel.add(addExpenseBtn);
    btnPanel.add(summaryBtn);

    bottomPanel.add(btnPanel, BorderLayout.NORTH);
    bottomPanel.add(summaryScroll, BorderLayout.CENTER);

    rightPanel.add(bottomPanel, BorderLayout.SOUTH);

    centerPanel.add(rightPanel);

    mainPanel.add(centerPanel, BorderLayout.CENTER);

    add(mainPanel);

    // ===== ACTIONS =====

    createGroupBtn.addActionListener(e -> createGroup());

    addMemberBtn.addActionListener(e -> addMember());

    addExpenseBtn.addActionListener(e -> addExpense());

    summaryBtn.addActionListener(e -> loadSummary());

    loadGroups();
    loadExpenses();

    setVisible(true);
}
    
    private void styleButton(JButton button, Color color) {

    button.setBackground(color);
    button.setForeground(Color.WHITE);

    button.setFocusPainted(false);

    button.setFont(new Font("Segoe UI", Font.BOLD, 14));

    button.setPreferredSize(new Dimension(200,40));
}
    private void createGroup() {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO user_groups(group_name, created_by) VALUES(?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, groupNameField.getText());
            ps.setString(2, "admin");

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Group Created");

            loadGroups();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addMember() {

        try {

            Connection con = DBConnection.getConnection();

            int groupId = getSelectedGroupId();

            String sql = "INSERT INTO group_members(group_id, member_name) VALUES(?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, groupId);
            ps.setString(2, memberField.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Member Added");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addExpense() {

        try {

            Connection con = DBConnection.getConnection();

            int groupId = getSelectedGroupId();

            String sql = "INSERT INTO group_expenses(group_id, paid_by, amount, description, expense_date) VALUES(?,?,?,?,CURDATE())";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, groupId);
            ps.setString(2, "admin");
            ps.setDouble(3, Double.parseDouble(amountField.getText()));
            ps.setString(4, descField.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Expense Added");

            loadExpenses();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGroups() {

        try {

            groupBox.removeAllItems();

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM user_groups";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                groupBox.addItem(
                        rs.getInt("group_id") + " - " +
                        rs.getString("group_name")
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private int getSelectedGroupId() {

        String selected = groupBox.getSelectedItem().toString();

        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private void loadExpenses() {

        try {

            tableModel.setRowCount(0);

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT g.expense_id, u.group_name, g.amount, g.description " +
                    "FROM group_expenses g " +
                    "JOIN user_groups u ON g.group_id=u.group_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                tableModel.addRow(new Object[] {
                        rs.getInt("expense_id"),
                        rs.getString("group_name"),
                        rs.getDouble("amount"),
                        rs.getString("description")
                });
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSummary() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT SUM(amount) as total FROM group_expenses";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                double total = rs.getDouble("total");

                summaryArea.setText(
                        "Total Group Expense = ₹" + total
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}