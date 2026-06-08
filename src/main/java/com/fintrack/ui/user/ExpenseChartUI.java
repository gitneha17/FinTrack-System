package com.fintrack.ui.user;

import com.fintrack.config.DBConnection;
import com.fintrack.utils.SessionManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExpenseChartUI extends JFrame {

    private JPanel chartContainer;

    public ExpenseChartUI() {

        setTitle("FinTrack - Expense Analytics");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 118, 210));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Expense Analytics Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        refreshBtn.addActionListener(e -> loadCharts());

        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== CHART PANEL =====
        chartContainer = new JPanel(new GridLayout(1, 2, 20, 20));
        chartContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        chartContainer.setBackground(new Color(245, 247, 250));

        add(chartContainer, BorderLayout.CENTER);

        loadCharts();

        setVisible(true);
    }

private void loadCharts() {

    chartContainer.removeAll();

    String currentUser = SessionManager.getCurrentUser();

    DefaultPieDataset pieDataset = new DefaultPieDataset();
    DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

    try {

        Connection con = DBConnection.getConnection();

        String query = """
    SELECT category, SUM(amount) as total
    FROM personal_expenses
    WHERE user_name = ?
    GROUP BY category
       """;

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, currentUser);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String category = rs.getString("category");
            double total = rs.getDouble("total");

            pieDataset.setValue(category, total);
            barDataset.addValue(total, "Amount", category);
        }

        // CHECK EMPTY DATA
        if (pieDataset.getItemCount() == 0) {

            JLabel noData = new JLabel(
                "No Expense Data Available",
                SwingConstants.CENTER
            );

            noData.setFont(new Font("Segoe UI", Font.BOLD, 22));

            chartContainer.setLayout(new BorderLayout());
            chartContainer.add(noData, BorderLayout.CENTER);

            chartContainer.revalidate();
            chartContainer.repaint();

            return;
        }

        // ================= PIE CHART =================

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Expense Distribution",
                pieDataset,
                true,
                true,
                false
        );

        PiePlot piePlot = (PiePlot) pieChart.getPlot();

        piePlot.setBackgroundPaint(Color.WHITE);
        piePlot.setOutlineVisible(false);
        piePlot.setCircular(true);

        ChartPanel piePanel = new ChartPanel(pieChart);

        piePanel.setPreferredSize(new Dimension(450, 400));

        // ================= BAR CHART =================

        JFreeChart barChart = ChartFactory.createBarChart(
                "Category Wise Expenses",
                "Category",
                "Amount",
                barDataset
        );

        CategoryPlot barPlot = barChart.getCategoryPlot();

        barPlot.setBackgroundPaint(Color.WHITE);
        barPlot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) barPlot.getRenderer();

        renderer.setSeriesPaint(0, new Color(102, 51, 255));

        ChartPanel barPanel = new ChartPanel(barChart);

        barPanel.setPreferredSize(new Dimension(450, 400));

        // ================= MAIN PANEL =================

        JPanel chartsPanel = new JPanel();

        chartsPanel.setLayout(new GridLayout(1, 2, 20, 20));

        chartsPanel.setBackground(Color.WHITE);

        chartsPanel.add(piePanel);
        chartsPanel.add(barPanel);

        chartContainer.setLayout(new BorderLayout());

        chartContainer.add(chartsPanel, BorderLayout.CENTER);

        chartContainer.revalidate();
        chartContainer.repaint();

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Error Loading Charts: " + e.getMessage()
        );
    }
}
}