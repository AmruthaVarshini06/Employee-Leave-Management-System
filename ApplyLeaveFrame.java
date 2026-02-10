package screens;

import javax.swing.*;
import javax.swing.SpinnerDateModel;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import data.*;

public class ApplyLeaveFrame extends JFrame {

    public ApplyLeaveFrame(String username) {

        setTitle("Apply Leave");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        // ===== Title =====
        JLabel title = new JLabel("Apply Leave Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(160, 20, 320, 30);
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

        SpinnerDateModel fromModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner fromSpinner = new JSpinner(fromModel);
        fromSpinner.setBounds(200, 180, 250, 30);
        fromSpinner.setEditor(new JSpinner.DateEditor(fromSpinner, "yyyy-MM-dd"));
        add(fromSpinner);

        // ===== To Date =====
        JLabel toLbl = new JLabel("To Date:");
        toLbl.setBounds(80, 230, 120, 25);
        add(toLbl);

        SpinnerDateModel toModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner toSpinner = new JSpinner(toModel);
        toSpinner.setBounds(200, 230, 250, 30);
        toSpinner.setEditor(new JSpinner.DateEditor(toSpinner, "yyyy-MM-dd"));
        add(toSpinner);

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

        // ===== Submit Action =====
        submitBtn.addActionListener(e -> {

            Date fromDate = (Date) fromSpinner.getValue();
            Date toDate = (Date) toSpinner.getValue();
            String reason = reasonArea.getText().trim();
            String type = (String) typeBox.getSelectedItem();

            if (type == null) {
                JOptionPane.showMessageDialog(this, "Select leave type");
                return;
            }

            if (reason.length() < 3) {
                JOptionPane.showMessageDialog(this, "Reason too short");
                return;
            }

            if (toDate.before(fromDate)) {
                JOptionPane.showMessageDialog(this,
                        "To Date cannot be before From Date");
                return;
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Leave leave = new Leave(
                    username,
                    type,
                    df.format(fromDate),
                    df.format(toDate),
                    reason
            );

            DataStore.leaves.add(leave);   // ✅ save shared list

            JOptionPane.showMessageDialog(this,
                    "Leave Applied Successfully ✅");

            submitBtn.setEnabled(false); // prevent double submit
            dispose();
        });

        // ===== Cancel =====
        cancelBtn.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
