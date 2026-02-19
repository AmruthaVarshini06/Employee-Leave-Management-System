package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LeaveStatusFrame extends JFrame {

    private String username;
    private Connection con;

    public LeaveStatusFrame(String username, Connection con) {
        this.username = username;
        this.con = con;

        setTitle("Leave Status");
        setSize(750, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(10, 10));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ================= HEADER =================
        JLabel header = new JLabel("Current Leave Status for: " + username, SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(new Color(40, 70, 160));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // ================= PANEL =================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        add(mainPanel, BorderLayout.CENTER);

        // ================= TABLE =================
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Leave ID");
        model.addColumn("Type");
        model.addColumn("From Date");
        model.addColumn("To Date");
        model.addColumn("Reason");
        model.addColumn("Status");

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(40, 90, 160));
        table.getTableHeader().setForeground(Color.WHITE);

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ================= FETCH DATA =================
        try {
            String sql = "SELECT leave_id, leave_type, from_date, to_date, reason, status FROM leaves WHERE username=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("leave_id"),
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

        // ================= STATUS COLOR =================
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);

                if (column == 5) { // Status column
                    String status = value.toString();
                    if (status.equalsIgnoreCase("Pending")) c.setForeground(new Color(255, 140, 0));
                    else if (status.equalsIgnoreCase("Approved")) c.setForeground(new Color(0, 128, 0));
                    else if (status.equalsIgnoreCase("Rejected")) c.setForeground(new Color(200, 50, 50));
                    else c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.BLACK);
                }

                if (isSelected) {
                    c.setBackground(new Color(200, 220, 255));
                } else {
                    c.setBackground(Color.WHITE);
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // ================= CLOSE BUTTON =================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(120, 35));
        styleButton(closeBtn);
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ================= BUTTON STYLE =================
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(40, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
