package screens;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(700,450);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Admin Panel");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250,40,300,40);
        add(title);

        JButton viewBtn = new JButton("View Leave Requests");
        viewBtn.setBounds(220,140,250,40);
        add(viewBtn);

        JButton manageBtn = new JButton("Manage Employees");
        manageBtn.setBounds(220,200,250,40);
        add(manageBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(220,260,250,40);
        add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
