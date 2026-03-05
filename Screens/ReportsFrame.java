package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsFrame extends JFrame {

    private Connection con;

    private JLabel empLbl, leaveLbl, appLbl, rejLbl, pendLbl;
    private DefaultTableModel model;

    public ReportsFrame(Connection con) {
        this.con = con;
        initializeUI();
        loadData();
    }

    private void initializeUI() {

        setTitle("Reports");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(new Color(240, 245, 250));
        setLayout(new BorderLayout(15, 15));

        // ===== Window Icon =====
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ================= TITLE =================
        JLabel title = new JLabel("System Reports", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(25, 60, 120));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(title, BorderLayout.NORTH);

        // ================= CENTER PANEL =================
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(240, 245, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(centerPanel, BorderLayout.CENTER);

        // ===== Stats Panel =====
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        statsPanel.setBackground(new Color(240, 245, 250));

        empLbl = statBox(statsPanel, "Employees", new Color(52, 152, 219));
        leaveLbl = statBox(statsPanel, "Leaves", new Color(155, 89, 182));
        appLbl = statBox(statsPanel, "Approved", new Color(46, 204, 113));
        rejLbl = statBox(statsPanel, "Rejected", new Color(231, 76, 60));
        pendLbl = statBox(statsPanel, "Pending", new Color(241, 196, 15));

        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel(
                new String[]{"ID", "Username", "Type", "From", "To", "Reason", "Status"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(25, 60, 120));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 245, 250));

        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        styleButton(refreshBtn, new Color(52, 152, 219));
        styleButton(closeBtn, new Color(120, 120, 120));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadData());
        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ===== Stat Box =====
    private JLabel statBox(JPanel parent, String title, Color color) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel name = new JLabel(title, SwingConstants.CENTER);
        name.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel value = new JLabel("0", SwingConstants.CENTER);
        value.setFont(new Font("Segoe UI", Font.BOLD, 22));
        value.setForeground(color);

        panel.add(name, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);

        parent.add(panel);
        return value;
    }

    // ===== Button Style =====
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    // ================= LOAD DATA =================
    private void loadData() {

        int totalEmp = 0, totalLeaves = 0;
        int approved = 0, rejected = 0, pending = 0;

        model.setRowCount(0);

        try {
            Statement st = con.createStatement();

            ResultSet rsEmp = st.executeQuery("SELECT COUNT(*) FROM users WHERE role='Employee'");
            if (rsEmp.next()) totalEmp = rsEmp.getInt(1);
            rsEmp.close();

            ResultSet rs = st.executeQuery("SELECT * FROM leaves");

            while (rs.next()) {
                totalLeaves++;

                String status = rs.getString("status");
                if (status == null) status = "Pending";

                if (status.equalsIgnoreCase("Approved")) approved++;
                else if (status.equalsIgnoreCase("Rejected")) rejected++;
                else pending++;

                model.addRow(new Object[]{
                        rs.getString("leave_id"),
                        rs.getString("username"),
                        rs.getString("leave_type"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getString("reason"),
                        status
                });
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }

        empLbl.setText(String.valueOf(totalEmp));
        leaveLbl.setText(String.valueOf(totalLeaves));
        appLbl.setText(String.valueOf(approved));
        rejLbl.setText(String.valueOf(rejected));
        pendLbl.setText(String.valueOf(pending));
    }
}