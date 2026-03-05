package screens;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class ApplyLeaveFrame extends JFrame {

    private Connection con;

    public ApplyLeaveFrame(String username, Connection con) {

        this.con = con;

        setTitle("Apply Leave - " + username);

        setSize(900,600);
        setLocationRelativeTo(null);

        // Start minimized
        setExtendedState(JFrame.ICONIFIED);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(240,245,250));

        // Window Icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch(Exception e){
            System.out.println("Icon not found");
        }

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(100,80));
        header.setBackground(new Color(25,60,120));

        JLabel title = new JLabel("  Apply Leave Form");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,24));

        header.add(title,BorderLayout.WEST);
        add(header,BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240,245,250));
        add(centerPanel,BorderLayout.CENTER);

        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(650,420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        centerPanel.add(card);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI",Font.BOLD,15);

        // Employee
        gbc.gridx=0; gbc.gridy=0;
        JLabel nameLbl = new JLabel("Employee:");
        nameLbl.setFont(labelFont);
        card.add(nameLbl,gbc);

        gbc.gridx=1;
        JTextField nameField = new JTextField(username);
        nameField.setEditable(false);
        nameField.setPreferredSize(new Dimension(250,35));
        card.add(nameField,gbc);

        // Leave Type
        gbc.gridx=0; gbc.gridy++;
        JLabel typeLbl = new JLabel("Leave Type:");
        typeLbl.setFont(labelFont);
        card.add(typeLbl,gbc);

        gbc.gridx=1;
        String types[]={"Casual","Sick","Earned"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setPreferredSize(new Dimension(250,35));
        card.add(typeBox,gbc);

        // From Date
        gbc.gridx=0; gbc.gridy++;
        JLabel fromLbl = new JLabel("From Date:");
        fromLbl.setFont(labelFont);
        card.add(fromLbl,gbc);

        gbc.gridx=1;
        JSpinner fromSpinner = new JSpinner(new SpinnerDateModel());
        fromSpinner.setEditor(new JSpinner.DateEditor(fromSpinner,"yyyy-MM-dd"));
        fromSpinner.setPreferredSize(new Dimension(250,35));
        card.add(fromSpinner,gbc);

        // To Date
        gbc.gridx=0; gbc.gridy++;
        JLabel toLbl = new JLabel("To Date:");
        toLbl.setFont(labelFont);
        card.add(toLbl,gbc);

        gbc.gridx=1;
        JSpinner toSpinner = new JSpinner(new SpinnerDateModel());
        toSpinner.setEditor(new JSpinner.DateEditor(toSpinner,"yyyy-MM-dd"));
        toSpinner.setPreferredSize(new Dimension(250,35));
        card.add(toSpinner,gbc);

        // Reason (Normal field like others)
        gbc.gridx=0; gbc.gridy++;
        JLabel reasonLbl = new JLabel("Reason:");
        reasonLbl.setFont(labelFont);
        card.add(reasonLbl,gbc);

        gbc.gridx=1;
        JTextField reasonField = new JTextField();
        reasonField.setPreferredSize(new Dimension(250,35));
        card.add(reasonField,gbc);

        // Buttons
        gbc.gridx=0;
        gbc.gridy++;
        gbc.gridwidth=2;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10));
        btnPanel.setBackground(Color.WHITE);

        JButton submitBtn = createButton("Submit",new Color(52,152,219));
        JButton cancelBtn = createButton("Cancel",new Color(231,76,60));

        btnPanel.add(submitBtn);
        btnPanel.add(cancelBtn);

        card.add(btnPanel,gbc);

        // Submit Action
        submitBtn.addActionListener(e -> {

            String reason = reasonField.getText().trim();

            if(reason.isEmpty()){
                JOptionPane.showMessageDialog(this,"Please enter reason");
                return;
            }

            String type = typeBox.getSelectedItem().toString();

            Date fromDate=(Date)fromSpinner.getValue();
            Date toDate=(Date)toSpinner.getValue();

            Date today = new Date();

            if(fromDate.before(today)){
                JOptionPane.showMessageDialog(this, "From Date cannot be before today's date");
                return;
            }

            if(toDate.before(fromDate)){
                JOptionPane.showMessageDialog(this,"To Date cannot be before From Date");
                return;
            }

            java.sql.Date from=new java.sql.Date(fromDate.getTime());
            java.sql.Date to=new java.sql.Date(toDate.getTime());

            try{

                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT MAX(leave_id) FROM leaves");

                int next=101;

                if(rs.next() && rs.getString(1)!=null){
                    String last=rs.getString(1).substring(1);
                    next=Integer.parseInt(last)+1;
                }

                String leaveId="L"+next;

                String sql="INSERT INTO leaves VALUES(?,?,?,?,?,?,'Pending')";
                PreparedStatement pst=con.prepareStatement(sql);

                pst.setString(1,leaveId);
                pst.setString(2,username);
                pst.setString(3,type);
                pst.setDate(4,from);
                pst.setDate(5,to);
                pst.setString(6,reason);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Leave Applied Successfully!");

                dispose();

            }catch(Exception ex){
                ex.printStackTrace();
            }

        });

        cancelBtn.addActionListener(e->dispose());

        setVisible(true);
    }

    private JButton createButton(String text,Color bg){
        JButton btn=new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI",Font.BOLD,15));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130,40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}