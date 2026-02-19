package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class ApplyLeaveFrame extends JFrame {

    private Connection con; // database connection

    public ApplyLeaveFrame(String username, Connection con) { // pass your JDBC connection
        this.con = con;

        // ================= WINDOW ICON =================
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        setLayout(null);
        setTitle("Apply Leave");
        setSize(650, 500);
        setLocationRelativeTo(null);

        // ================= HEADER =================
        JPanel header = new JPanel();
        header.setBounds(0, 0, 650, 70);
        header.setBackground(new Color(40, 90, 160));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 18));

        JLabel title = new JLabel("Apply Leave Form");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(title);
        add(header);

        // ================= FORM PANEL =================
        JPanel panel = new JPanel();
        panel.setBounds(60, 90, 520, 360);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.setLayout(null);
        add(panel);

        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // ===== Employee =====
        JLabel nameLbl = new JLabel("Employee:");
        nameLbl.setFont(labelFont);
        nameLbl.setBounds(30, 20, 120, 25);
        panel.add(nameLbl);

        JTextField nameField = new JTextField(username);
        nameField.setBounds(170, 20, 300, 30);
        nameField.setEditable(false);
        panel.add(nameField);

        // ===== Leave Type =====
        JLabel typeLbl = new JLabel("Leave Type:");
        typeLbl.setFont(labelFont);
        typeLbl.setBounds(30, 70, 120, 25);
        panel.add(typeLbl);

        String types[] = {"Casual", "Sick", "Earned"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(170, 70, 300, 30);
        panel.add(typeBox);

        // ===== From Date =====
        JLabel fromLbl = new JLabel("From Date:");
        fromLbl.setFont(labelFont);
        fromLbl.setBounds(30, 120, 120, 25);
        panel.add(fromLbl);

        JSpinner fromSpinner = new JSpinner(new SpinnerDateModel());
        fromSpinner.setBounds(170, 120, 300, 30);
        JSpinner.DateEditor fromEditor = new JSpinner.DateEditor(fromSpinner, "yyyy-MM-dd");
        fromSpinner.setEditor(fromEditor);
        panel.add(fromSpinner);

        // ===== To Date =====
        JLabel toLbl = new JLabel("To Date:");
        toLbl.setFont(labelFont);
        toLbl.setBounds(30, 170, 120, 25);
        panel.add(toLbl);

        JSpinner toSpinner = new JSpinner(new SpinnerDateModel());
        toSpinner.setBounds(170, 170, 300, 30);
        JSpinner.DateEditor toEditor = new JSpinner.DateEditor(toSpinner, "yyyy-MM-dd");
        toSpinner.setEditor(toEditor);
        panel.add(toSpinner);

        // ===== Reason =====
        JLabel reasonLbl = new JLabel("Reason:");
        reasonLbl.setFont(labelFont);
        reasonLbl.setBounds(30, 220, 120, 25);
        panel.add(reasonLbl);

        JTextArea reasonArea = new JTextArea();
        JScrollPane sp = new JScrollPane(reasonArea);
        sp.setBounds(170, 220, 300, 60);
        panel.add(sp);

        // ================= BUTTONS =================
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(170, 300, 120, 35);
        stylePrimaryButton(submitBtn);
        panel.add(submitBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(320, 300, 120, 35);
        styleDangerButton(cancelBtn);
        panel.add(cancelBtn);

        // ================= SAVE DATA =================
        submitBtn.addActionListener(e -> {

            String reason = reasonArea.getText().trim();
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter reason");
                return;
            }

            String type = typeBox.getSelectedItem().toString();
            Date fromDate = (Date) fromSpinner.getValue();
            Date toDate = (Date) toSpinner.getValue();

            // Convert to java.sql.Date for DB
            java.sql.Date from = new java.sql.Date(fromDate.getTime());
            java.sql.Date to = new java.sql.Date(toDate.getTime());

            try {
                // ===== Generate leave_id =====
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM leaves");
                rs.next();
                int count = rs.getInt("total") + 101; // start from L101
                String leaveId = "L" + count;
                rs.close();
                stmt.close();

                // ===== Insert into leaves table =====
                String sql = "INSERT INTO leaves (leave_id, username, leave_type, from_date, to_date, reason, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, leaveId);
                pst.setString(2, username);
                pst.setString(3, type);
                pst.setDate(4, from);
                pst.setDate(5, to);
                pst.setString(6, reason);

                int inserted = pst.executeUpdate();
                if (inserted > 0) {
                    JOptionPane.showMessageDialog(this, "Leave Applied Successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error! Could not apply leave.");
                }

                pst.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ================= BUTTON STYLES =================
    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(new Color(40, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void styleDangerButton(JButton btn) {
        btn.setBackground(new Color(200, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
