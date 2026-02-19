package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LeaveHistoryFrame extends JFrame {

    private String username;
    private Connection con;

    public LeaveHistoryFrame(String username, Connection con) {
        this.username = username;
        this.con = con;

        setTitle("Leave Summary");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 240, 240)); // light gray background
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ================= HEADER =================
        JLabel header = new JLabel("Leave Summary for: " + username);
        header.setBounds(50, 20, 500, 40);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(new Color(40, 70, 160));
        add(header);

        // ================= PANEL =================
        JPanel panel = new JPanel();
        panel.setBounds(50, 80, 500, 300);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        add(panel);

        // ================= LABELS =================
        JLabel totalLbl = createLabel(20, 20);
        JLabel pendingLbl = createLabel(20, 70);
        JLabel approvedLbl = createLabel(20, 120);
        JLabel rejectedLbl = createLabel(20, 170);
        JLabel remainingLbl = createLabel(20, 220);

        panel.add(totalLbl);
        panel.add(pendingLbl);
        panel.add(approvedLbl);
        panel.add(rejectedLbl);
        panel.add(remainingLbl);

        // ================= FETCH DATA =================
        try {
            Statement stmt = con.createStatement();

            // Total leaves applied
            ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) AS total FROM leaves WHERE username='" + username + "'");
            rsTotal.next();
            int totalLeaves = rsTotal.getInt("total");
            totalLbl.setText("Total Leaves Applied: " + totalLeaves);
            rsTotal.close();

            // Pending leaves
            ResultSet rsPending = stmt.executeQuery("SELECT COUNT(*) AS pending FROM leaves WHERE username='" + username + "' AND status='Pending'");
            rsPending.next();
            int pendingLeaves = rsPending.getInt("pending");
            pendingLbl.setText("Pending Leaves: " + pendingLeaves);
            rsPending.close();

            // Approved leaves
            ResultSet rsApproved = stmt.executeQuery("SELECT COUNT(*) AS approved FROM leaves WHERE username='" + username + "' AND status='Approved'");
            rsApproved.next();
            int approvedLeaves = rsApproved.getInt("approved");
            approvedLbl.setText("Approved Leaves: " + approvedLeaves);
            rsApproved.close();

            // Rejected leaves
            ResultSet rsRejected = stmt.executeQuery("SELECT COUNT(*) AS rejected FROM leaves WHERE username='" + username + "' AND status='Rejected'");
            rsRejected.next();
            int rejectedLeaves = rsRejected.getInt("rejected");
            rejectedLbl.setText("Rejected Leaves: " + rejectedLeaves);
            rsRejected.close();

            // Remaining leaves
            int remainingCasual = 5;
            int remainingSick = 3;
            int remainingEarned = 8;

            ResultSet rsLeaves = stmt.executeQuery("SELECT leave_type, COUNT(*) AS used FROM leaves WHERE username='" + username + "' AND status='Approved' GROUP BY leave_type");
            while (rsLeaves.next()) {
                String type = rsLeaves.getString("leave_type");
                int used = rsLeaves.getInt("used");
                if (type.equalsIgnoreCase("Casual")) remainingCasual -= used;
                if (type.equalsIgnoreCase("Sick")) remainingSick -= used;
                if (type.equalsIgnoreCase("Earned")) remainingEarned -= used;
            }
            rsLeaves.close();
            stmt.close();

            remainingLbl.setText("Remaining Leaves - Casual: " + remainingCasual +
                    ", Sick: " + remainingSick + ", Earned: " + remainingEarned);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }

        // ================= CLOSE BUTTON =================
        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(220, 370, 120, 35);
        styleButton(closeBtn);
        add(closeBtn);

        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ================= HELPER METHODS =================
    private JLabel createLabel(int x, int y) {
        JLabel lbl = new JLabel();
        lbl.setBounds(x, y, 450, 40);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(new Color(50, 50, 50));
        return lbl;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(40, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
