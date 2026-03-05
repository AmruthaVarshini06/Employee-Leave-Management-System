package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // ========== HEADER ==========
        JLabel header = new JLabel("Leave Status for: " + username, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(40, 90, 160));
        header.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // ========== TABLE PANEL ==========
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(Color.WHITE);
        add(tablePanel, BorderLayout.CENTER);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };

        model.addColumn("Leave ID");
        model.addColumn("Type");
        model.addColumn("From Date");
        model.addColumn("To Date");
        model.addColumn("Reason");
        model.addColumn("Status");

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(40, 90, 160));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ========== FETCH DATA ==========
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

        // ========== STATUS COLOR + ALTERNATING ROWS ==========
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);

                // Alternating row colors
                if (!isSelected) {
                    if (row % 2 == 0) c.setBackground(new Color(245, 245, 245));
                    else c.setBackground(Color.WHITE);
                }

                // Status column coloring
                if (column == 5) {
                    String status = value.toString();
                    if (status.equalsIgnoreCase("Pending")) c.setForeground(new Color(255, 140, 0));
                    else if (status.equalsIgnoreCase("Approved")) c.setForeground(new Color(0, 128, 0));
                    else if (status.equalsIgnoreCase("Rejected")) c.setForeground(new Color(200, 50, 50));
                    else c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.BLACK);
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        tablePanel.add(scroll, BorderLayout.CENTER);

        // ========== CLOSE BUTTON ==========
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(140, 40));
        styleButton(closeBtn);
        bottomPanel.add(closeBtn);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ========== BUTTON STYLE ==========
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(40, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 70, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(40, 90, 160));
            }
        });
    }
}