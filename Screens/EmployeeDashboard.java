package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

public class EmployeeDashboard extends JFrame {

    private String username;
    private Connection con; // JDBC connection

    public EmployeeDashboard(String username, Connection con) {
        this.username = username;
        this.con = con;

        setTitle("Employee Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 240, 240));
        setLayout(null);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ================= HEADER =================
        JPanel header = new JPanel();
        header.setBounds(0, 0, 900, 100);
        header.setBackground(new Color(40, 90, 160));
        header.setLayout(null);
        add(header);

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setBounds(30, 30, 600, 40);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Arial", Font.BOLD, 28));
        header.add(welcome);

        // ================= BUTTON PANEL =================
        JButton applyBtn = createDashboardButton("Apply Leave", 120, 130);
        JButton statusBtn = createDashboardButton("Leave Status", 360, 130);
        JButton historyBtn = createDashboardButton("Leave History", 600, 130);

        add(applyBtn);
        add(statusBtn);
        add(historyBtn);

        // ================= LEAVE BALANCE PANEL =================
        JPanel balancePanel = new JPanel();
        balancePanel.setBounds(120, 230, 660, 220);
        balancePanel.setLayout(null);
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                "Leave Balance", 0, 0, new Font("Arial", Font.BOLD, 18), new Color(40, 90, 160)));
        add(balancePanel);

        // Leave labels
        JLabel casual = createBalanceLabel("Casual Leave : 5", 40, 40, new Color(70, 130, 180));
        JLabel sick = createBalanceLabel("Sick Leave : 3", 40, 90, new Color(60, 179, 113));
        JLabel earned = createBalanceLabel("Earned Leave : 8", 40, 140, new Color(218, 165, 32));

        balancePanel.add(casual);
        balancePanel.add(sick);
        balancePanel.add(earned);

        // ================= LOGOUT BUTTON =================
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(720, 480, 120, 40);
        styleButton(logoutBtn, new Color(180, 50, 50));
        add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        // ================= BUTTON ACTIONS =================
        applyBtn.addActionListener(e -> new ApplyLeaveFrame(username, con));
        statusBtn.addActionListener(e -> new LeaveStatusFrame(username, con).setVisible(true));
        historyBtn.addActionListener(e -> new LeaveHistoryFrame(username, con).setVisible(true));

        setVisible(true);
    }

    // ================= HELPER METHODS =================
    private JButton createDashboardButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 180, 50);
        styleButton(btn, new Color(40, 90, 160));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(30, 70, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(40, 90, 160));
            }
        });
        return btn;
    }

    private JLabel createBalanceLabel(String text, int x, int y, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 400, 30);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(color);
        return lbl;
    }
    
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
