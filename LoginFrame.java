package screens;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Employee Leave Management System");

        // ✅ icon load (file path)
        ImageIcon icon = new ImageIcon("src/common/appicon.jpg");
        setIconImage(icon.getImage());

       ImageIcon bgIcon = new ImageIcon("src/common/bg.jpg");

// scale image to frame size
Image img = bgIcon.getImage();
Image scaledImg = img.getScaledInstance(900, 550, Image.SCALE_SMOOTH);
ImageIcon scaledIcon = new ImageIcon(scaledImg);

JLabel bgLabel = new JLabel(scaledIcon);
bgLabel.setLayout(null);
setContentPane(bgLabel);


        // Title
        JLabel title = new JLabel("Login");
        title.setBounds(360, 60, 200, 40);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(30, 60, 120));
        bgLabel.add(title);

        // Username
        JLabel l1 = new JLabel("Username:");
        l1.setBounds(250, 150, 100, 25);
        l1.setFont(new Font("Arial", Font.BOLD, 14));
        bgLabel.add(l1);

        JTextField t1 = new JTextField();
        t1.setBounds(360, 150, 200, 30);
        bgLabel.add(t1);

        // Password
        JLabel l2 = new JLabel("Password:");
        l2.setBounds(250, 200, 100, 25);
        l2.setFont(new Font("Arial", Font.BOLD, 14));
        bgLabel.add(l2);

        JPasswordField p1 = new JPasswordField();
        p1.setBounds(360, 200, 200, 30);
        bgLabel.add(p1);

        // Role
        JLabel l3 = new JLabel("Role:");
        l3.setBounds(250, 250, 100, 25);
        l3.setFont(new Font("Arial", Font.BOLD, 14));
        bgLabel.add(l3);

        String roles[] = {"Employee", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setBounds(360, 250, 200, 30);
        bgLabel.add(roleBox);

        // Buttons
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

       loginBtn.addActionListener(e -> {

    String user = t1.getText().trim();
    String pass = new String(p1.getPassword()).trim();
    String role = roleBox.getSelectedItem().toString();

    // ✅ Empty validation
    if(user.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter Username",
                "Input Error",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if(pass.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter Password",
                "Input Error",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // ✅ Accept any credentials
    dispose(); // close login window

    if(role.equals("Employee")) {
        new EmployeeDashboard(user);
    } else {
        new AdminDashboard();
    }

});


        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
