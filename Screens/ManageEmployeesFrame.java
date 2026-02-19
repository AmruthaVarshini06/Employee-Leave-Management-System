package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageEmployeesFrame extends JFrame {

    private Connection con;
    private DefaultTableModel model;
    private JTable table;

    private JTextField idField, nameField, depField, emailField, mobileField, salaryField;

    public ManageEmployeesFrame(Connection con) {
        this.con = con;

        setTitle("Manage Employees");
        setSize(950, 580);
        setLocationRelativeTo(null);
        setLayout(null);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }
        
        JLabel title = new JLabel("Manage Employees");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(360, 20, 250, 30);
        add(title);

        // ===== Search =====
        JLabel searchLbl = new JLabel("Search:");
        searchLbl.setBounds(50, 55, 60, 25);
        add(searchLbl);

        JTextField searchField = new JTextField();
        searchField.setBounds(110, 55, 200, 30);
        add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(320, 55, 100, 30);
        add(searchBtn);

        JButton resetBtn = new JButton("Show All");
        resetBtn.setBounds(430, 55, 120, 30);
        add(resetBtn);

        // ===== Table =====
        model = new DefaultTableModel(
                new String[]{"Emp ID", "Name", "Dept", "Email", "Mobile", "Salary"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 95, 820, 220);
        add(sp);

        // ===== Row select =====
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = table.getSelectedRow();
            if (r == -1) return;

            idField.setText(table.getValueAt(r, 0).toString());
            nameField.setText(table.getValueAt(r, 1).toString());
            depField.setText(table.getValueAt(r, 2).toString());
            emailField.setText(table.getValueAt(r, 3).toString());
            mobileField.setText(table.getValueAt(r, 4).toString());
            salaryField.setText(table.getValueAt(r, 5).toString());
        });

        // ===== Fields =====
        idField = field("Emp ID:", 60, 340);
        nameField = field("Name:", 260, 340);
        depField = field("Dept:", 500, 340);
        emailField = field("Email:", 60, 380);
        mobileField = field("Mobile:", 350, 380);
        salaryField = field("Salary:", 590, 380);

        // ===== Buttons =====
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(180, 450, 120, 35);
        add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(320, 450, 120, 35);
        add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(460, 450, 120, 35);
        add(deleteBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(600, 450, 120, 35);
        add(closeBtn);

        // ===== Load Data =====
        loadEmployeesFromDB();

        // ===== Actions =====
        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        closeBtn.addActionListener(e -> dispose());

        searchBtn.addActionListener(e -> searchEmployees(searchField.getText().trim()));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            loadEmployeesFromDB();
        });

        setVisible(true);
    }

    private JTextField field(String label, int x, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 80, 25);
        add(l);

        JTextField f = new JTextField();
        f.setBounds(x + 60, y, 140, 30);
        add(f);
        return f;
    }

    // ===== LOAD =====
    private void loadEmployeesFromDB() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM employees";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("emp_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        rs.getDouble("salary")
                });
            }

            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employees from DB");
        }
    }

    // ===== ADD =====
    private void addEmployee() {

        try {
            String sql = "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, idField.getText().trim());
            pst.setString(2, nameField.getText().trim());
            pst.setString(3, depField.getText().trim());
            pst.setString(4, emailField.getText().trim());
            pst.setString(5, mobileField.getText().trim());
            pst.setDouble(6, Double.parseDouble(salaryField.getText().trim()));

            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Employee Added ✅");
            clearFields();
            loadEmployeesFromDB();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // ===== UPDATE =====
    private void updateEmployee() {

        try {
            String sql = "UPDATE employees SET name=?, department=?, email=?, mobile=?, salary=? WHERE emp_id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, nameField.getText().trim());
            pst.setString(2, depField.getText().trim());
            pst.setString(3, emailField.getText().trim());
            pst.setString(4, mobileField.getText().trim());
            pst.setDouble(5, Double.parseDouble(salaryField.getText().trim()));
            pst.setString(6, idField.getText().trim());

            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Employee Updated ✅");
            clearFields();
            loadEmployeesFromDB();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating employee");
        }
    }

    // ===== DELETE =====
    private void deleteEmployee() {

        try {
            String sql = "DELETE FROM employees WHERE emp_id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, idField.getText().trim());
            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Employee Deleted ✅");
            clearFields();
            loadEmployeesFromDB();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting employee");
        }
    }

    // ===== SEARCH =====
    private void searchEmployees(String key) {

        model.setRowCount(0);

        try {
            String sql = "SELECT * FROM employees WHERE emp_id LIKE ? OR department LIKE ? OR email LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);

            String like = "%" + key + "%";
            pst.setString(1, like);
            pst.setString(2, like);
            pst.setString(3, like);

            ResultSet rs = pst.executeQuery();

            boolean found = false;
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("emp_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        rs.getDouble("salary")
                });
                found = true;
            }

            if (!found) JOptionPane.showMessageDialog(this, "No employee found");

            rs.close();
            pst.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching employees");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        depField.setText("");
        emailField.setText("");
        mobileField.setText("");
        salaryField.setText("");
    }
}
