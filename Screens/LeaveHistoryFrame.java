package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class LeaveHistoryFrame extends JFrame {

    private String username;
    private Connection con;

    public LeaveHistoryFrame(String username, Connection con) {

        this.username = username;
        this.con = con;

        setTitle("Leave Summary");
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Allow resizing & fullscreen
        setExtendedState(JFrame.NORMAL);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(240,245,250));

        // ================= HEADER =================
        JPanel header = new JPanel();
        header.setBackground(new Color(40,90,160));
        header.setBorder(new EmptyBorder(20,20,20,20));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("Leave Summary - " + username);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,24));

        header.add(title,BorderLayout.WEST);

        add(header,BorderLayout.NORTH);

        // ================= MAIN PANEL =================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240,245,250));
        add(mainPanel,BorderLayout.CENTER);

        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(30,40,30,40)
        ));

        mainPanel.add(card);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,10,15,10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI",Font.BOLD,16);

        JLabel totalLbl = new JLabel();
        JLabel pendingLbl = new JLabel();
        JLabel approvedLbl = new JLabel();
        JLabel rejectedLbl = new JLabel();
        JLabel remainingLbl = new JLabel();

        totalLbl.setFont(labelFont);
        pendingLbl.setFont(labelFont);
        approvedLbl.setFont(labelFont);
        rejectedLbl.setFont(labelFont);
        remainingLbl.setFont(labelFont);

        gbc.gridx=0; gbc.gridy=0;
        card.add(totalLbl,gbc);

        gbc.gridy++;
        card.add(pendingLbl,gbc);

        gbc.gridy++;
        card.add(approvedLbl,gbc);

        gbc.gridy++;
        card.add(rejectedLbl,gbc);

        gbc.gridy++;
        card.add(remainingLbl,gbc);

        // ================= DATABASE =================
        int MAX_CASUAL = 10;
        int MAX_SICK = 8;
        int MAX_EARNED = 15;

        int usedCasual = 0;
        int usedSick = 0;
        int usedEarned = 0;

        try{

            Statement stmt = con.createStatement();

            ResultSet rsTotal = stmt.executeQuery(
                    "SELECT COUNT(*) FROM leaves WHERE username='"+username+"'");
            rsTotal.next();
            int totalLeaves = rsTotal.getInt(1);
            totalLbl.setText("Total Leaves Applied : " + totalLeaves);

            ResultSet rsPending = stmt.executeQuery(
                    "SELECT COUNT(*) FROM leaves WHERE username='"+username+"' AND status='Pending'");
            rsPending.next();
            int pendingLeaves = rsPending.getInt(1);
            pendingLbl.setText("Pending Leaves : " + pendingLeaves);

            ResultSet rsApproved = stmt.executeQuery(
                    "SELECT COUNT(*) FROM leaves WHERE username='"+username+"' AND status='Approved'");
            rsApproved.next();
            int approvedLeaves = rsApproved.getInt(1);
            approvedLbl.setText("Approved Leaves : " + approvedLeaves);

            ResultSet rsRejected = stmt.executeQuery(
                    "SELECT COUNT(*) FROM leaves WHERE username='"+username+"' AND status='Rejected'");
            rsRejected.next();
            int rejectedLeaves = rsRejected.getInt(1);
            rejectedLbl.setText("Rejected Leaves : " + rejectedLeaves);

            ResultSet rsLeaves = stmt.executeQuery(
                    "SELECT leave_type, COUNT(*) FROM leaves WHERE username='"+username+"' AND status='Approved' GROUP BY leave_type");

            while(rsLeaves.next()){

                String type = rsLeaves.getString(1);
                int used = rsLeaves.getInt(2);

                if(type.equalsIgnoreCase("Casual"))
                    usedCasual = used;

                else if(type.equalsIgnoreCase("Sick"))
                    usedSick = used;

                else if(type.equalsIgnoreCase("Earned"))
                    usedEarned = used;
            }

            remainingLbl.setText("Remaining Leaves → Casual: "
                    +(MAX_CASUAL-usedCasual)
                    +" | Sick: "+(MAX_SICK-usedSick)
                    +" | Earned: "+(MAX_EARNED-usedEarned));

        }
        catch(Exception e){
            e.printStackTrace();
        }

        // ================= BUTTON =================
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(130,40));
        styleButton(closeBtn);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(240,245,250));
        bottom.add(closeBtn);

        add(bottom,BorderLayout.SOUTH);

        closeBtn.addActionListener(e->dispose());

        setVisible(true);
    }

    // ================= BUTTON STYLE =================
    private void styleButton(JButton btn){

        btn.setBackground(new Color(40,90,160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI",Font.BOLD,14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter(){

            public void mouseEntered(java.awt.event.MouseEvent evt){
                btn.setBackground(new Color(30,70,140));
            }

            public void mouseExited(java.awt.event.MouseEvent evt){
                btn.setBackground(new Color(40,90,160));
            }
        });
    }
}