package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EmployeeDashboard extends JFrame {

    private String username;
    private Connection con;

    private JLabel casualLabel;
    private JLabel sickLabel;
    private JLabel earnedLabel;

    private final int MAX_CASUAL = 10;
    private final int MAX_SICK = 8;
    private final int MAX_EARNED = 15;

    public EmployeeDashboard(String username, Connection con) {
        this.username = username;
        this.con = con;

        setTitle("Employee Dashboard - " + username);

        // ✅ Start in normal window (NOT minimized)
        setSize(1100, 650);
        setLocationRelativeTo(null);

        // ✅ If you want full screen, use this instead:
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Optional icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        getContentPane().setBackground(new Color(240, 245, 250));

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 60, 120));
        header.setPreferredSize(new Dimension(100, 90));

        JLabel welcome = new JLabel("  Welcome, " + username);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));

        header.add(welcome, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ================= CENTER PANEL =================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 245, 250));
        add(centerPanel, BorderLayout.CENTER);

        JPanel card = new JPanel(new BorderLayout(20, 20));
        card.setPreferredSize(new Dimension(900, 480));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        centerPanel.add(card);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton applyBtn = createButton("Apply Leave", new Color(52, 152, 219));
        JButton statusBtn = createButton("Leave Status", new Color(155, 89, 182));
        JButton historyBtn = createButton("Leave History", new Color(46, 204, 113));

        buttonPanel.add(applyBtn);
        buttonPanel.add(statusBtn);
        buttonPanel.add(historyBtn);

        card.add(buttonPanel, BorderLayout.NORTH);

        // ================= LEAVE BALANCE PANEL =================
        JPanel balancePanel = new JPanel(new GridLayout(3, 1, 15, 15));
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Leave Balance",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(25, 60, 120)
        ));

        casualLabel = createBalanceLabel(new Color(52, 152, 219));
        sickLabel = createBalanceLabel(new Color(155, 89, 182));
        earnedLabel = createBalanceLabel(new Color(46, 204, 113));

        balancePanel.add(casualLabel);
        balancePanel.add(sickLabel);
        balancePanel.add(earnedLabel);

        card.add(balancePanel, BorderLayout.CENTER);

        // ================= LOGOUT =================
        JButton logoutBtn = createButton("Logout", new Color(231, 76, 60));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(logoutBtn);

        card.add(bottomPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        applyBtn.addActionListener(e -> new ApplyLeaveFrame(username, con).setVisible(true));
        statusBtn.addActionListener(e -> new LeaveStatusFrame(username, con).setVisible(true));
        historyBtn.addActionListener(e -> new LeaveHistoryFrame(username, con).setVisible(true));

        loadLeaveBalance();

        // ✅ Make visible at end
        setVisible(true);
    }

    // ================= LOAD LEAVE BALANCE =================
    private void loadLeaveBalance() {

        int usedCasual = 0;
        int usedSick = 0;
        int usedEarned = 0;

        try {
            String sql = "SELECT leave_type, COUNT(*) as total FROM leaves " +
                    "WHERE username=? AND status='Approved' GROUP BY leave_type";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String type = rs.getString("leave_type");
                int count = rs.getInt("total");

                if (type.equalsIgnoreCase("Casual"))
                    usedCasual = count;
                else if (type.equalsIgnoreCase("Sick"))
                    usedSick = count;
                else if (type.equalsIgnoreCase("Earned"))
                    usedEarned = count;
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        casualLabel.setText("Casual Leave Remaining : " + (MAX_CASUAL - usedCasual));
        sickLabel.setText("Sick Leave Remaining : " + (MAX_SICK - usedSick));
        earnedLabel.setText("Earned Leave Remaining : " + (MAX_EARNED - usedEarned));
    }

    // ================= HELPER METHODS =================
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createBalanceLabel(Color color) {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbl.setForeground(color);
        return lbl;
    }
}