package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminApprovalFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private Connection con;

    public AdminApprovalFrame(Connection con) {
        this.con = con;

        setTitle("Admin — Leave Approval Panel");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ===== Main Background Panel =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        add(mainPanel);

        // ===== Title =====
        JLabel title = new JLabel("Leave Approval Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 60, 120));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        mainPanel.add(title, BorderLayout.NORTH);

        // ===== Table Panel (Card Style) =====
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String cols[] = {"Leave ID", "Employee", "Type", "From", "To", "Reason", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(30, 60, 120));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableCard, BorderLayout.CENTER);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JButton approveBtn = createButton("Approve", new Color(46, 204, 113));
        JButton rejectBtn = createButton("Reject", new Color(231, 76, 60));
        JButton refreshBtn = createButton("Refresh", new Color(52, 152, 219));
        JButton backBtn = createButton("Back", new Color(120, 120, 120));

        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(approveBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(rejectBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(refreshBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== Load Data =====
        loadLeaves();

        // ===== Actions =====
        approveBtn.addActionListener(e -> updateSelectedLeave("Approved"));
        rejectBtn.addActionListener(e -> updateSelectedLeave("Rejected"));
        refreshBtn.addActionListener(e -> loadLeaves());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard(con);
        });

        setVisible(true);
    }

    // ===== Styled Button Method =====
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(130, 40));
        return button;
    }

    // ================= Load Leaves =================
    private void loadLeaves() {
        model.setRowCount(0);

        try {
            String sql = "SELECT leave_id, username, leave_type, from_date, to_date, reason, status " +
                         "FROM leaves WHERE status='Pending'";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("leave_id"),
                        rs.getString("username"),
                        rs.getString("leave_type"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getString("reason"),
                        rs.getString("status")
                });
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // ================= Update Leave =================
    private void updateSelectedLeave(String newStatus) {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a leave row first");
            return;
        }

        String leaveId = table.getValueAt(row, 0).toString();

        try {
            String sql = "UPDATE leaves SET status=? WHERE leave_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, newStatus);
            pst.setString(2, leaveId);
            int updated = pst.executeUpdate();
            pst.close();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Leave " + newStatus + " ✅");
                loadLeaves();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating leave: " + e.getMessage());
        }
    }
}