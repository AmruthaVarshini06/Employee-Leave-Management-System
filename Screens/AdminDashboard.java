package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminDashboard extends JFrame {

    private Connection con; // JDBC connection

    public AdminDashboard(Connection con) { // pass connection from LoginFrame
        this.con = con;

        setTitle("Admin Dashboard");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ===== Title =====
        JLabel title = new JLabel("Admin Panel");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 40, 300, 40);
        add(title);

        // ===== Buttons =====
        JButton viewBtn = new JButton("View Leave Requests");
        viewBtn.setBounds(220, 140, 250, 40);
        styleButton(viewBtn);
        add(viewBtn);

        JButton manageBtn = new JButton("Manage Employees");
        manageBtn.setBounds(220, 200, 250, 40);
        styleButton(manageBtn);
        add(manageBtn);

        JButton reportsBtn = new JButton("Reports");
        reportsBtn.setBounds(220, 260, 250, 40);
        styleButton(reportsBtn);
        add(reportsBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(220, 320, 250, 40);
        logoutBtn.setBackground(new Color(180, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        add(logoutBtn);

        // ===== Actions =====
        viewBtn.addActionListener(e -> new AdminApprovalFrame(con).setVisible(true));
        manageBtn.addActionListener(e -> new ManageEmployeesFrame(con).setVisible(true));
        reportsBtn.addActionListener(e -> new ReportsFrame(con).setVisible(true)); 
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ===== Button style helper =====
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(40, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
    }
}
