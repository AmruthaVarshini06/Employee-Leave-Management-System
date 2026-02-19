package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private Connection con; // database connection

    public LoginFrame() {
        initializeConnection(); // initialize DB connection
        initializeUI();         // build UI
    }

    // ================== DATABASE CONNECTION ==================
    private void initializeConnection() {
        try {
            // Load MySQL driver (for MySQL 8+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employee_leave_db", 
                    "root",                                      
                    "root@123"                               
            );
            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed");
            System.exit(0); // exit if DB connection fails
        }
    }

    // ================== USER INTERFACE ==================
    private void initializeUI() {

        setTitle("Employee Leave Management System");

        // ================= APP ICON =================
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        // ================= BACKGROUND IMAGE =================
        JLabel bgLabel = new JLabel();
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource("/common/bg.jpg.jpeg"));
            Image img = bgIcon.getImage();
            Image scaledImg = img.getScaledInstance(900, 550, Image.SCALE_SMOOTH);
            bgLabel.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            System.out.println("Background image not found");
        }
        bgLabel.setLayout(null);
        setContentPane(bgLabel);

        // ================= TITLE =================
        JLabel title = new JLabel("Login");
        title.setBounds(360, 60, 200, 40);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(30, 60, 120));
        bgLabel.add(title);

        // ================= USERNAME =================
        JLabel l1 = new JLabel("Username:");
        l1.setBounds(250, 150, 100, 25);
        l1.setFont(new Font("Arial", Font.BOLD, 14));
        bgLabel.add(l1);

        JTextField t1 = new JTextField();
        t1.setBounds(360, 150, 200, 30);
        bgLabel.add(t1);

        // ================= PASSWORD =================
        JLabel l2 = new JLabel("Password:");
        l2.setBounds(250, 200, 100, 25);
        l2.setFont(new Font("Arial", Font.BOLD, 14));
        bgLabel.add(l2);

        JPasswordField p1 = new JPasswordField();
        p1.setBounds(360, 200, 200, 30);
        bgLabel.add(p1);

        // ================= ROLE =================
        JLabel l3 = new JLabel("Role:");
        l3.setBounds(250, 250, 100, 25);
        l3.setFont(new Font("Arial", Font.BOLD, 14));
        bgLabel.add(l3);

        String roles[] = {"Employee", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setBounds(360, 250, 200, 30);
        bgLabel.add(roleBox);

        // ================= BUTTONS =================
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(320, 320, 120, 40);
        loginBtn.setBackground(new Color(40, 90, 160));
        loginBtn.setForeground(Color.WHITE);
        bgLabel.add(loginBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(460, 320, 120, 40);
        exitBtn.setBackground(new Color(40, 90, 160));
        exitBtn.setForeground(Color.WHITE);
        bgLabel.add(exitBtn);

        exitBtn.addActionListener(e -> System.exit(0));

        // ================= LOGIN ACTION =================
        loginBtn.addActionListener(e -> {
            String user = t1.getText().trim();
            String pass = new String(p1.getPassword()).trim();
            String role = roleBox.getSelectedItem().toString();

            if (user.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Username");
                return;
            }
            if (pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Password");
                return;
            }

            try {
                String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, user);
                pst.setString(2, pass);
                pst.setString(3, role);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    dispose();
                    if (role.equals("Employee")) {
                         new EmployeeDashboard(user, con); // user = username, con = JDBC connection
                     }
                    else {
                        new AdminDashboard(con);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username, Password, or Role");
                }

                rs.close();
                pst.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // ================= FRAME SETTINGS =================
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new LoginFrame(); // simply create instance; connection handled internally
    }
}
