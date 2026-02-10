package screens;

import javax.swing.*;
import java.awt.*;

public class EmployeeDashboard extends JFrame {

    public EmployeeDashboard(String username) {

        setTitle("Employee Dashboard");

        // ✅ window icon
        ImageIcon icon = new ImageIcon("src/common/appicon.jpg");
        setIconImage(icon.getImage());

        setLayout(null);

        // ================= HEADER =================

        JPanel header = new JPanel();
        header.setBounds(0, 0, 900, 90);
        header.setBackground(new Color(40, 90, 160));
        header.setLayout(null);
        add(header);

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setBounds(30, 20, 600, 40);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Arial", Font.BOLD, 26));
        header.add(welcome);

        // ================= BUTTON PANEL =================

        JButton applyBtn = new JButton("Apply Leave");
        applyBtn.setBounds(120, 120, 180, 45);
        styleButton(applyBtn);
        add(applyBtn);

        JButton statusBtn = new JButton("Leave Status");
        statusBtn.setBounds(360, 120, 180, 45);
        styleButton(statusBtn);
        add(statusBtn);

        JButton historyBtn = new JButton("Leave History");
        historyBtn.setBounds(600, 120, 180, 45);
        styleButton(historyBtn);
        add(historyBtn);

        // ================= LEAVE BALANCE PANEL =================

        JPanel balancePanel = new JPanel();
        balancePanel.setBounds(120, 210, 660, 220);
        balancePanel.setLayout(null);
        balancePanel.setBorder(BorderFactory.createTitledBorder("Leave Balance"));
        balancePanel.setBackground(Color.WHITE);
        add(balancePanel);

        JLabel casual = new JLabel("Casual Leave : 5");
        casual.setBounds(40, 40, 300, 30);
        casual.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(casual);

        JLabel sick = new JLabel("Sick Leave : 3");
        sick.setBounds(40, 90, 300, 30);
        sick.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(sick);

        JLabel earned = new JLabel("Earned Leave : 8");
        earned.setBounds(40, 140, 300, 30);
        earned.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(earned);

        // ================= LOGOUT BUTTON =================

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(720, 460, 120, 40);
        logoutBtn.setBackground(new Color(180, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        // ================= BUTTON ACTIONS =================

        applyBtn.addActionListener(e -> {
           new ApplyLeaveFrame(username);
        });


        statusBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Leave Status Screen Coming");
        });

        historyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Leave History Screen Coming");
        });

        // ================= FRAME SETTINGS =================

        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ================= BUTTON STYLE METHOD =================

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(40, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
