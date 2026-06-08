package com.fintrack.ui.user;

import com.fintrack.config.DBConnection;
import com.fintrack.dao.ExpenseDAO;
import com.fintrack.model.Expense;
import com.fintrack.utils.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.fintrack.config.DBConnection;


public class ViewExpenseUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewExpenseUI() {

        setTitle("Expense History");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // TABLE MODEL
        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Title");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("Date");

        // TABLE
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // BUTTONS
        JButton deleteBtn = new JButton("Delete Selected Expense");
        JButton editBtn = new JButton("Edit Expense");

        // LAYOUT
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // DELETE BUTTON
        deleteBtn.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(this,
                        "Please select an expense!");

                return;
            }

            int expenseId =
                    (int) model.getValueAt(selectedRow, 0);

            ExpenseDAO dao = new ExpenseDAO();

            if (dao.deleteExpense(expenseId)) {

                JOptionPane.showMessageDialog(this,
                        "Expense Deleted!");

                loadExpenses();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Delete Failed!");
            }
        });

        // EDIT BUTTON
        editBtn.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(this,
                        "Select an expense first!");

                return;
            }

            int expenseId =
                    (int) model.getValueAt(selectedRow, 0);

            String currentTitle =
                    model.getValueAt(selectedRow, 1).toString();

            double currentAmount =
                    Double.parseDouble(
                            model.getValueAt(selectedRow, 2).toString()
                    );

            String currentCategory =
                    model.getValueAt(selectedRow, 3).toString();

            // INPUT DIALOGS
            String newTitle = JOptionPane.showInputDialog(
                    this,
                    "Enter New Title:",
                    currentTitle
            );

            String amountInput = JOptionPane.showInputDialog(
                    this,
                    "Enter New Amount:",
                    currentAmount
            );

            String newCategory = JOptionPane.showInputDialog(
                    this,
                    "Enter New Category:",
                    currentCategory
            );

            try {

                double newAmount =
                        Double.parseDouble(amountInput);

                Expense expense = new Expense();

                expense.setExpenseId(expenseId);
                expense.setTitle(newTitle);
                expense.setAmount(newAmount);
                expense.setCategory(newCategory);

                ExpenseDAO dao = new ExpenseDAO();

                if (dao.updateExpense(expense)) {

                    JOptionPane.showMessageDialog(this,
                            "Expense Updated!");

                    loadExpenses();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Update Failed!");
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Invalid Input!");
            }
        });

        // LOAD DATA
        loadExpenses();

        setVisible(true);
    }

    // LOAD EXPENSES
private void loadExpenses() {

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT expense_id, title, amount, category, expense_date " +
                "FROM expenses WHERE user_id=?";

        PreparedStatement pst = con.prepareStatement(query);

        pst.setInt(1, SessionManager.getUserId());

        ResultSet rs = pst.executeQuery();

        DefaultTableModel model =
                (DefaultTableModel) table.getModel();

        model.setRowCount(0);

        while (rs.next()) {

            Object[] row = {
                    rs.getInt("expense_id"),
                    rs.getString("title"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    rs.getDate("expense_date")
            };

            model.addRow(row);
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error loading expenses: " + e.getMessage()
        );

        e.printStackTrace();
    }
}


    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ViewExpenseUI().setVisible(true);
        });
    }
}