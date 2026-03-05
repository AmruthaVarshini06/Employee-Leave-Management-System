package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Set;

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
            //Load image from project folder.
            //Convert it into an ImageIcon.
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));

            //Set it as the window icon of your JFrame.
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

       
        // ================= BACKGROUND IMAGE =================
            JLabel bgLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    try {
                        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/common/bg.jpg.jpeg"));
                        Image img = bgIcon.getImage();

                        // Draw image full size of JFrame
                        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    } catch (Exception e) {
                        System.out.println("Background image not found");
                    }
                }
            };

            bgLabel.setLayout(new GridBagLayout());
        setContentPane(bgLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // ================= TITLE =================
            // ================= TITLE =================
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(30, 60, 120));
        bgLabel.add(title, gbc);

        gbc.gridwidth = 1;

        // ================= USERNAME =================
        gbc.gridy++;
        gbc.gridx = 0;
        bgLabel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField t1 = new JTextField(15);
        bgLabel.add(t1, gbc);

        // ================= PASSWORD =================
        gbc.gridy++;
        gbc.gridx = 0;
        bgLabel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField p1 = new JPasswordField(15);
        bgLabel.add(p1, gbc);

        // ================= ROLE =================
        gbc.gridy++;
        gbc.gridx = 0;
        bgLabel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> roleBox =
                new JComboBox<>(new String[]{"Employee", "Admin"});
        bgLabel.add(roleBox, gbc);

         // ================= BUTTONS =================
        gbc.gridy++;
        gbc.gridx = 0;

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(40, 90, 160));
        loginBtn.setForeground(Color.WHITE);
        bgLabel.add(loginBtn, gbc);

        gbc.gridx = 1;

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new Color(40, 90, 160));
        exitBtn.setForeground(Color.WHITE);
        bgLabel.add(exitBtn, gbc);

        exitBtn.addActionListener(e -> System.exit(0));
        // ================= LOGIN ACTION =================
        // Add ActionListener to login button (runs when button is clicked)
            loginBtn.addActionListener(e -> {

                // Get username from text field and remove extra spaces
                String user = t1.getText().trim();

                // Get password from password field (returns char[]), convert to String, remove extra spaces
                String pass = new String(p1.getPassword()).trim();

                // Get selected role (Admin/Employee etc.) from combo box
                String role = roleBox.getSelectedItem().toString();

                // Check if username field is empty
                if (user.isEmpty()) {

                    // Show message dialog if username is not entered
                    JOptionPane.showMessageDialog(this, "Please enter Username");

                    // Stop further execution
                    return;
                }

                // Check if password field is empty
                if (pass.isEmpty()) {

                    // Show message dialog if password is not entered
                    JOptionPane.showMessageDialog(this, "Please enter Password");

                    // Stop further execution
                    return;
                }

                // Try block to handle possible database errors
                try {

                    // SQL query to check if user exists with matching username, password and role
                    // ? are placeholders for values (to prevent SQL injection)
                    String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";

                    // Create PreparedStatement using database connection
                    PreparedStatement pst = con.prepareStatement(sql);

                    // Set first placeholder (?) with username
                    pst.setString(1, user);

                    // Set second placeholder (?) with password
                    pst.setString(2, pass);

                    // Set third placeholder (?) with role
                    pst.setString(3, role);

                    // Execute the query and store result in ResultSet
                    ResultSet rs = pst.executeQuery();

                    // If a matching record is found in database
                    if (rs.next()) {

                        // Show success message
                        JOptionPane.showMessageDialog(this, "Login Successful!");

                        // Close the current login window
                        dispose();

                        // Check if selected role is Employee
                        if (role.equals("Employee")) {

                            // Open Employee Dashboard and pass username and DB connection
                            new EmployeeDashboard(user, con);
                        }
                        else {

                            // Otherwise open Admin Dashboard and pass DB connection
                            new AdminDashboard(con);
                        }
                    }
                    else {

                        // If no matching record found, show error message
                        JOptionPane.showMessageDialog(this, "Invalid Username, Password, or Role");
                    }

                    // Close ResultSet to free memory
                    rs.close();

                    // Close PreparedStatement to free resources
                    pst.close();

                }
                catch (Exception ex) {  // Catch any exception that occurs

                    // Print full error details in console (for debugging)
                    ex.printStackTrace();

                    // Show error message in dialog box
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }

            }); // End of ActionListener
        // ================= FRAME SETTINGS =================
                     // Set the size of the window
            // 900 = width, 550 = height (in pixels)
            setSize(900, 550);

            // Set the window location to the center of the screen
            // Passing null means center relative to screen
            setLocationRelativeTo(null);

            // Specify what happens when user clicks the close (X) button
            // EXIT_ON_CLOSE means completely terminate the application
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Make the window visible on the screen
            // Without this, the frame will not appear
            setVisible(true);
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new LoginFrame(); // simply create instance; connection handled internally
    }
}