package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import data.*;

public class ReportsFrame extends JFrame {

    public ReportsFrame() {

        setTitle("Reports");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("System Reports");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(310, 20, 300, 30);
        add(title);

        // ===== Summary Counts =====

        int totalEmp = DataStore.employees.size();
        int totalLeaves = DataStore.leaves.size();

        int approved = 0, rejected = 0, pending = 0;

        for (Leave l : DataStore.leaves) {

            if ("Approved".equals(l.status))
                approved++;
            else if ("Rejected".equals(l.status))
                rejected++;
            else
                pending++;
        }

        // ===== Summary Boxes =====

        add(box("Employees", totalEmp, 40, 80));
        add(box("Leaves", totalLeaves, 200, 80));
        add(box("Approved", approved, 360, 80));
        add(box("Rejected", rejected, 520, 80));
        add(box("Pending", pending, 680, 80));

        // ===== Leave Table =====

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Employee","Type","From","To","Status"}, 0);

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(80, 200, 680, 220);
        add(sp);

        // load table rows
        for (Leave l : DataStore.leaves) {
            model.addRow(new Object[]{
                    l.employee,
                    l.type,
                    l.fromDate,
                    l.toDate,
                    l.status
            });
        }

        // ===== Close Button =====

        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(360, 440, 120, 35);
        add(closeBtn);

        closeBtn.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ===== Summary Box Method =====

    JPanel box(String name, int value, int x, int y) {

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBounds(x, y, 140, 90);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        p.setBackground(Color.WHITE);

        JLabel l1 = new JLabel(name, SwingConstants.CENTER);
        l1.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel l2 = new JLabel(String.valueOf(value),
                SwingConstants.CENTER);
        l2.setFont(new Font("Arial", Font.BOLD, 22));
        l2.setForeground(new Color(40, 90, 160));

        p.add(l1, BorderLayout.NORTH);
        p.add(l2, BorderLayout.CENTER);

        return p;
    }
}
