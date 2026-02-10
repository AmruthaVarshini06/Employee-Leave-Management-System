package screens;

import javax.swing.*;
import java.awt.*;

public class ApplyLeaveFrame extends JFrame {

    public ApplyLeaveFrame(String username) {

        setTitle("Apply Leave");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        // ===== Title =====
        JLabel title = new JLabel("Apply Leave Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(180, 20, 300, 30);
        add(title);

        // ===== Employee Name =====
        JLabel nameLbl = new JLabel("Employee:");
        nameLbl.setBounds(80, 80, 120, 25);
        add(nameLbl);

        JTextField nameField = new JTextField(username);
        nameField.setBounds(200, 80, 250, 30);
        nameField.setEditable(false);
        add(nameField);

        // ===== Leave Type =====
        JLabel typeLbl = new JLabel("Leave Type:");
        typeLbl.setBounds(80, 130, 120, 25);
        add(typeLbl);

        String types[] = {"Casual", "Sick", "Earned"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(200, 130, 250, 30);
        add(typeBox);

        // ===== From Date =====
        JLabel fromLbl = new JLabel("From Date:");
        fromLbl.setBounds(80, 180, 120, 25);
        add(fromLbl);

        JTextField fromField = new JTextField("YYYY-MM-DD");
        fromField.setBounds(200, 180, 250, 30);
        add(fromField);

        // ===== To Date =====
        JLabel toLbl = new JLabel("To Date:");
        toLbl.setBounds(80, 230, 120, 25);
        add(toLbl);

        JTextField toField = new JTextField("YYYY-MM-DD");
        toField.setBounds(200, 230, 250, 30);
        add(toField);

        // ===== Reason =====
        JLabel reasonLbl = new JLabel("Reason:");
        reasonLbl.setBounds(80, 280, 120, 25);
        add(reasonLbl);

        JTextArea reasonArea = new JTextArea();
        JScrollPane sp = new JScrollPane(reasonArea);
        sp.setBounds(200, 280, 250, 70);
        add(sp);

        // ===== Buttons =====
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(180, 370, 120, 35);
        add(submitBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(320, 370, 120, 35);
        add(cancelBtn);

        // ===== Button Actions =====

        submitBtn.addActionListener(e -> {

            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String reason = reasonArea.getText().trim();

            if(from.isEmpty() || to.isEmpty() || reason.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "All fields are required!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Leave Applied Successfully ✅",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
