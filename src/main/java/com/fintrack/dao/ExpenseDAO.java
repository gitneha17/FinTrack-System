package com.fintrack.dao;

import com.fintrack.config.DBConnection;
import com.fintrack.model.Expense;

import javax.swing.JOptionPane;
import com.fintrack.utils.SessionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // ==============================
    // ADD EXPENSE
    // ==============================
    public boolean addExpense(Expense expense) {

        String sql = "INSERT INTO personal_expenses "
                + "(user_name, title, amount, category) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, expense.getUserName());
            ps.setString(2, expense.getTitle());
            ps.setDouble(3, expense.getAmount());
            ps.setString(4, expense.getCategory());

            int result = ps.executeUpdate();

            return result > 0;

        } catch (Exception e) {

            System.out.println("❌ EXPENSE ERROR:");
            e.printStackTrace();

            JOptionPane.showMessageDialog(null, e.getMessage());

            return false;
        }
    }

    // ==============================
    // DELETE EXPENSE
    // ==============================
public boolean deleteExpense(int expenseId) {

    String sql = "DELETE FROM personal_expenses WHERE expense_id=? AND user_name=?";

    try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
    ) {

        ps.setInt(1, expenseId);

        ps.setString(2, SessionManager.getCurrentUser());

        int result = ps.executeUpdate();

        return result > 0;

    } catch (Exception e) {

        System.out.println("❌ DELETE ERROR:");
        e.printStackTrace();

        return false;
    }
}
    // ==============================
    // UPDATE EXPENSE
    // ==============================
    public boolean updateExpense(Expense expense) {

                String sql = "UPDATE personal_expenses "
                            + "SET title=?, amount=?, category=? "
                            + "WHERE expense_id=? AND user_name=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, expense.getTitle());
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setInt(4, expense.getExpenseId());

            int result = ps.executeUpdate();

            return result > 0;

        } catch (Exception e) {

            System.out.println("❌ UPDATE ERROR:");
            e.printStackTrace();

            return false;
        }
    }

    // ==============================
    // GET EXPENSES BY USER
    // ==============================
    public List<Expense> getExpensesByUser(String userName) {

    List<Expense> expenseList = new ArrayList<>();

    String sql = "SELECT * FROM personal_expenses WHERE user_name=?";

    try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
    ) {

        ps.setString(1, userName);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Expense expense = new Expense();

            expense.setExpenseId(rs.getInt("expense_id"));
            expense.setUserName(rs.getString("user_name"));
            expense.setTitle(rs.getString("title"));
            expense.setAmount(rs.getDouble("amount"));
            expense.setCategory(rs.getString("category"));

            expenseList.add(expense);
        }

    } catch (Exception e) {

        System.out.println("❌ FETCH ERROR:");
        e.printStackTrace();
    }

    return expenseList;
}
}