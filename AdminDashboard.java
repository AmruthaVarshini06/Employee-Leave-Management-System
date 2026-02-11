package screens;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        // ===== Header =====
        JPanel header = new JPanel();
        header.setBounds(0, 0, 800, 90);
        header.setBackground(new Color(40, 90, 160));
        header.setLayout(null);
        add(header);

        JLabel title = new JLabel("Admin Panel");
        title.setBounds(300, 25, 300, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        header.add(title);

        // ===== Buttons =====

        JButton viewBtn = new JButton("View Leave Requests");
        viewBtn.setBounds(260, 140, 280, 45);
        styleBtn(viewBtn);
        add(viewBtn);

        JButton manageBtn = new JButton("Manage Employees");
        manageBtn.setBounds(260, 210, 280, 45);
        styleBtn(manageBtn);
        add(manageBtn);

        JButton reportBtn = new JButton("Reports");
        reportBtn.setBounds(260, 280, 280, 45);
        styleBtn(reportBtn);
        add(reportBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(620, 390, 140, 40);
        logoutBtn.setBackground(new Color(180, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        add(logoutBtn);

        // ===== Button Actions =====

        // ✅ Open Admin Leave Approval Panel
        viewBtn.addActionListener(e ->
                new AdminApprovalFrame());

        // ✅ Open Manage Employees Module
        manageBtn.addActionListener(e ->
                new ManageEmployeesFrame());

        // ✅ Reports (placeholder for now)
        reportBtn.addActionListener(e ->
        new ReportsFrame());

        // ✅ Logout
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ===== Button Style Method =====

    private void styleBtn(JButton b) {
        b.setBackground(new Color(40, 90, 160));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 15));
        b.setFocusPainted(false);
    }
}
