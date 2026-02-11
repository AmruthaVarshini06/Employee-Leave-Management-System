package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import data.*;

public class AdminApprovalFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public AdminApprovalFrame() {

        setTitle("Admin — Leave Approval Panel");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setLayout(null);

        // ===== Title =====
        JLabel title = new JLabel("Leave Requests");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(300, 20, 300, 30);
        add(title);

        // ===== Table =====
        String cols[] = {
                "Employee",
                "Type",
                "From",
                "To",
                "Reason",
                "Status"
        };

        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false; // read-only table
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40, 70, 730, 300);
        add(sp);

        // load real data
        loadLeaves();

        // ===== Buttons =====

        JButton approveBtn = new JButton("Approve");
        approveBtn.setBounds(180, 400, 160, 40);
        approveBtn.setBackground(new Color(40, 150, 80));
        approveBtn.setForeground(Color.WHITE);
        add(approveBtn);

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBounds(360, 400, 160, 40);
        rejectBtn.setBackground(new Color(180, 50, 50));
        rejectBtn.setForeground(Color.WHITE);
        add(rejectBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(540, 400, 120, 40);
        add(refreshBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(40, 400, 120, 40);
        add(backBtn);

        // ===== Actions =====

        approveBtn.addActionListener(e -> approveSelected());
        rejectBtn.addActionListener(e -> rejectSelected());

        refreshBtn.addActionListener(e -> loadLeaves());

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ================= LOAD DATA =================

    private void loadLeaves() {

        model.setRowCount(0); // clear table

        for (Leave l : DataStore.leaves) {
            model.addRow(new Object[]{
                    l.employee,
                    l.type,
                    l.fromDate,
                    l.toDate,
                    l.reason,
                    l.status
            });
        }
    }

    // ================= APPROVE =================

    private void approveSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a leave row first");
            return;
        }

        DataStore.leaves.get(row).status = "Approved";
        loadLeaves();
    }

    // ================= REJECT =================

    private void rejectSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a leave row first");
            return;
        }

        DataStore.leaves.get(row).status = "Rejected";
        loadLeaves();
    }
}
