package com.fintrack.ui.user;

import com.fintrack.dao.ExpenseDAO;
import com.fintrack.model.Expense;
import com.fintrack.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

public class AddExpenseUI extends JFrame {

    private JTextField titleField;
    private JTextField amountField;
    private JComboBox<String> categoryBox;

    public AddExpenseUI() {

        setTitle("Add Expense");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleField = new JTextField();
        amountField = new JTextField();

        String[] categories = {
                "Food",
                "Travel",
                "Shopping",
                "Bills",
                "Entertainment"
        };

        categoryBox = new JComboBox<>(categories);

        JButton addBtn = new JButton("Add Expense");

        panel.add(new JLabel("Title:"));
        panel.add(titleField);

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);

        panel.add(new JLabel(""));
        panel.add(addBtn);

        add(panel);

        // BUTTON ACTION
        addBtn.addActionListener(e -> {

            try {

                String title = titleField.getText().trim();

                String amountText = amountField.getText().trim();

                // VALIDATION
                if (title.isEmpty() || amountText.isEmpty()) {

                    JOptionPane.showMessageDialog(this,
                            "Please fill all fields!");

                    return;
                }

                double amount = Double.parseDouble(amountText);

                String category =
                        categoryBox.getSelectedItem().toString();

                String userName =
                        SessionManager.getCurrentUser();

                // CREATE OBJECT
                Expense expense = new Expense(
                        userName,
                        title,
                        amount,
                        category
                );

                // SAVE
                ExpenseDAO dao = new ExpenseDAO();

                boolean success = dao.addExpense(expense);

                if (success) {

                    JOptionPane.showMessageDialog(this,
                            "Expense Added Successfully!");

                    titleField.setText("");
                    amountField.setText("");

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Failed to Add Expense!");

                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric amount!");

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage());
            }

        });

        setVisible(true);
    }
}