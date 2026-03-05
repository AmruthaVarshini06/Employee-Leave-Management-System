package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminApprovalFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private Connection con; // JDBC connection

    public AdminApprovalFrame(Connection con) {
        this.con = con;

        setTitle("Admin — Leave Approval Panel");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ===== Title =====
        JLabel title = new JLabel("Leave Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(250, 20, 350, 30);
        add(title);

        // ===== Table =====
        String cols[] = {"Leave ID", "Employee", "Type", "From", "To", "Reason", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // make table read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 70, 780, 300);
        add(sp);

        // ===== Buttons =====
        JButton approveBtn = new JButton("Approve");
        approveBtn.setBounds(180, 400, 150, 40);
        approveBtn.setBackground(new Color(40, 150, 80));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFont(new Font("Arial", Font.BOLD, 14));
        add(approveBtn);

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBounds(360, 400, 150, 40);
        rejectBtn.setBackground(new Color(180, 50, 50));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFont(new Font("Arial", Font.BOLD, 14));
        add(rejectBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(540, 400, 120, 40);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        add(refreshBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(30, 400, 120, 40);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        add(backBtn);

        // ===== Load Data =====
        loadLeaves();

        // ===== Button Actions =====
        approveBtn.addActionListener(e -> updateSelectedLeave("Approved"));
        rejectBtn.addActionListener(e -> updateSelectedLeave("Rejected"));
        refreshBtn.addActionListener(e -> loadLeaves());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard(con); // ✅ pass JDBC connection
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ================= Load Leaves from DB =================
    private void loadLeaves() {
        model.setRowCount(0); // clear table

        try {
            String sql = "SELECT leave_id, username, leave_type, from_date, to_date, reason, status FROM leaves";
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // ================= Approve / Reject Selected Leave =================
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating leave: " + e.getMessage());
        }
    }
}
