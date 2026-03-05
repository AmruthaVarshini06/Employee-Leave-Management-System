package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageEmployeesFrame extends JFrame {

    private Connection con;
    private DefaultTableModel model;
    private JTable table;

    private JTextField idField, nameField, depField, emailField, mobileField, salaryField, dateField;

    public ManageEmployeesFrame(Connection con) {
        this.con = con;

        setTitle("Manage Employees");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/common/appicon.jpg.jpeg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("App icon not found");
        }

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        add(mainPanel);

        // ===== Title =====
        JLabel title = new JLabel("Employee Management Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 60, 120));
        mainPanel.add(title, BorderLayout.NORTH);

        // ===== Center Container =====
        JPanel centerContainer = new JPanel(new BorderLayout(15, 15));
        centerContainer.setOpaque(false);
        mainPanel.add(centerContainer, BorderLayout.CENTER);

        // ===== Search Panel =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JTextField searchField = new JTextField(20);
        JButton searchBtn = createButton("Search", new Color(52,152,219));
        JButton resetBtn = createButton("Show All", new Color(120,120,120));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(resetBtn);

        centerContainer.add(searchPanel, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel(
                new String[]{"Emp ID", "Name", "Dept", "Email", "Mobile", "Salary", "Date Of Joining"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(30,60,120));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        centerContainer.add(sp, BorderLayout.CENTER);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridLayout(3, 3, 20, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        idField = createField(formPanel, "Emp ID:");
        nameField = createField(formPanel, "Name:");
        depField = createField(formPanel, "Department:");
        emailField = createField(formPanel, "Email:");
        mobileField = createField(formPanel, "Mobile:");
        salaryField = createField(formPanel, "Salary:");
        dateField = createField(formPanel, "Date Of Joining (YYYY-MM-DD):");

        centerContainer.add(formPanel, BorderLayout.SOUTH);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240,245,250));

        JButton addBtn = createButton("Add", new Color(46,204,113));
        JButton updateBtn = createButton("Update", new Color(241,196,15));
        JButton deleteBtn = createButton("Delete", new Color(231,76,60));
        JButton closeBtn = createButton("Close", new Color(120,120,120));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== Row Selection =====
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
            dateField.setText(table.getValueAt(r, 6).toString());
        });

        loadEmployeesFromDB();

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

    private JTextField createField(JPanel panel, String label) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        JLabel l = new JLabel(label);
        JTextField f = new JTextField();

        container.add(l, BorderLayout.NORTH);
        container.add(f, BorderLayout.CENTER);

        panel.add(container);
        return f;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

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
                        rs.getDouble("salary"),
                        rs.getString("date_of_joining")
                });
            }

            rs.close();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employees from DB");
        }
    }

    private void addEmployee() {
        try {
            String mobile = mobileField.getText().trim();
            if (!isValidPhone(mobile)) {
                JOptionPane.showMessageDialog(this, "Invalid Phone Number! Enter 10 digits only.");
                return;
            }

            String sql = "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, idField.getText().trim());
            pst.setString(2, nameField.getText().trim());
            pst.setString(3, depField.getText().trim());
            pst.setString(4, emailField.getText().trim());
            pst.setString(5, mobile);
            pst.setDouble(6, Double.parseDouble(salaryField.getText().trim()));
            pst.setString(7, dateField.getText().trim());

            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Employee Added ✅");
            clearFields();
            loadEmployeesFromDB();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            String mobile = mobileField.getText().trim();
            if (!isValidPhone(mobile)) {
                JOptionPane.showMessageDialog(this, "Invalid Phone Number! Enter 10 digits only.");
                return;
            }

            String sql = "UPDATE employees SET name=?, department=?, email=?, mobile=?, salary=?, date_of_joining=? WHERE emp_id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, nameField.getText().trim());
            pst.setString(2, depField.getText().trim());
            pst.setString(3, emailField.getText().trim());
            pst.setString(4, mobile);
            pst.setDouble(5, Double.parseDouble(salaryField.getText().trim()));
            pst.setString(6, dateField.getText().trim());
            pst.setString(7, idField.getText().trim());

            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Employee Updated ✅");
            clearFields();
            loadEmployeesFromDB();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating employee");
        }
    }

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
            JOptionPane.showMessageDialog(this, "Error deleting employee");
        }
    }

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
                        rs.getDouble("salary"),
                        rs.getString("date_of_joining")
                });
                found = true;
            }

            if (!found) JOptionPane.showMessageDialog(this, "No employee found");

            rs.close();
            pst.close();

        } catch (Exception e) {
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
        dateField.setText("");
    }
}