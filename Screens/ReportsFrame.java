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
        setSize(950, 560);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("System Reports");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(330, 15, 300, 30);
        add(title);

        empLbl = box("Employees", 40, 70);
        leaveLbl = box("Leaves", 200, 70);
        appLbl = box("Approved", 360, 70);
        rejLbl = box("Rejected", 520, 70);
        pendLbl = box("Pending", 680, 70);

        // ===== Leave Table =====
        model = new DefaultTableModel(
                new String[]{"ID", "Username", "Type", "From", "To", "Reason", "Status"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40, 180, 850, 250);
        add(sp);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(300, 460, 120, 35);
        add(refreshBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(450, 460, 120, 35);
        add(closeBtn);

        refreshBtn.addActionListener(e -> loadData());
        closeBtn.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JLabel box(String title, int x, int y) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBounds(x, y, 140, 80);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel name = new JLabel(title, SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel value = new JLabel("0", SwingConstants.CENTER);
        value.setFont(new Font("Arial", Font.BOLD, 22));
        value.setForeground(new Color(40, 90, 160));

        p.add(name, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        add(p);

        return value;
    }

    // ================= LOAD REPORT DATA =================
    private void loadData() {

        int totalEmp = 0, totalLeaves = 0;
        int approved = 0, rejected = 0, pending = 0;

        model.setRowCount(0);

        try {
            Statement st = con.createStatement();

            // ===== Employee Count =====
            ResultSet rsEmp = st.executeQuery("SELECT COUNT(*) FROM users WHERE role='Employee'");
            if (rsEmp.next()) totalEmp = rsEmp.getInt(1);
            rsEmp.close();

            // ===== Leaves =====
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }

        empLbl.setText(String.valueOf(totalEmp));
        leaveLbl.setText(String.valueOf(totalLeaves));
        appLbl.setText(String.valueOf(approved));
        rejLbl.setText(String.valueOf(rejected));
        pendLbl.setText(String.valueOf(pending));
    }
}
