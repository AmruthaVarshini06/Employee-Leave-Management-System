package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminDashboard extends JFrame {

    private Connection con;

    public AdminDashboard(Connection con) {
        this.con = con;

        setTitle("Admin Dashboard");

        // ✅ Normal size
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        // ✅ IMPORTANT: DO NOT minimize here
        setExtendedState(JFrame.NORMAL);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== Window Icon =====
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ===== Main Background =====
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 245, 250));
        add(mainPanel);

        // ===== Card Panel =====
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(420, 380));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== Title =====
        JLabel title = new JLabel("Admin Panel", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(25, 60, 120));
        gbc.gridy = 0;
        cardPanel.add(title, gbc);

        // ===== Buttons =====
        JButton viewBtn = createButton("View Leave Requests", new Color(52, 152, 219));
        gbc.gridy = 1;
        cardPanel.add(viewBtn, gbc);

        JButton manageBtn = createButton("Manage Employees", new Color(155, 89, 182));
        gbc.gridy = 2;
        cardPanel.add(manageBtn, gbc);

        JButton reportsBtn = createButton("Reports", new Color(46, 204, 113));
        gbc.gridy = 3;
        cardPanel.add(reportsBtn, gbc);

        JButton logoutBtn = createButton("Logout", new Color(231, 76, 60));
        gbc.gridy = 4;
        cardPanel.add(logoutBtn, gbc);

        mainPanel.add(cardPanel);

        // ===== Actions =====
        viewBtn.addActionListener(e -> new AdminApprovalFrame(con));
        manageBtn.addActionListener(e -> new ManageEmployeesFrame(con));
        reportsBtn.addActionListener(e -> new ReportsFrame(con));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true); // ✅ must be at end
    }

    // ===== Modern Button Style =====
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}