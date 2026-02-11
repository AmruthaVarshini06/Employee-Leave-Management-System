package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import data.*;

public class ManageEmployeesFrame extends JFrame {

    DefaultTableModel model;
    JTable table;

    JTextField idField, nameField, depField, emailField, mobileField, salaryField;

    public ManageEmployeesFrame() {

        setTitle("Manage Employees");
        setSize(950, 580);
        setLocationRelativeTo(null);
        setLayout(null);

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

        // ===== Table (NON EDITABLE) =====
        model = new DefaultTableModel(
                new String[]{"ID","Name","Dept","Email","Mobile","Salary"}, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 95, 820, 220);
        add(sp);

        loadEmployees();

        // ===== Row Select → Autofill =====
        table.getSelectionModel().addListSelectionListener(e -> {

            if(e.getValueIsAdjusting()) return;

            int r = table.getSelectedRow();
            if(r == -1) return;

            idField.setText(String.valueOf(table.getValueAt(r,0)));
            nameField.setText(String.valueOf(table.getValueAt(r,1)));
            depField.setText(String.valueOf(table.getValueAt(r,2)));
            emailField.setText(String.valueOf(table.getValueAt(r,3)));
            mobileField.setText(String.valueOf(table.getValueAt(r,4)));
            salaryField.setText(String.valueOf(table.getValueAt(r,5)));
        });

        // ===== Fields =====
        idField = field("ID:", 60, 340);
        nameField = field("Name:", 260, 340);
        depField = field("Dept:", 500, 340);

        emailField = field("Email:", 60, 380);
        emailField.setBounds(120,380,200,30);

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

        // ===== Search =====
        searchBtn.addActionListener(e -> {

            String key = searchField.getText().trim().toLowerCase();

            if(key.isEmpty()) {
                loadEmployees();
                return;
            }

            model.setRowCount(0);
            boolean found = false;

            for(Employee emp : DataStore.employees) {

                if(emp.id.toLowerCase().contains(key) ||
                   emp.name.toLowerCase().contains(key) ||
                   emp.department.toLowerCase().contains(key) ||
                   emp.email.toLowerCase().contains(key)) {

                    addRow(emp);
                    found = true;
                }
            }

            if(!found) JOptionPane.showMessageDialog(this,"No employee found");
        });

        resetBtn.addActionListener(e -> {
            searchField.setText("");
            loadEmployees();
        });

        // ===== Add =====
        addBtn.addActionListener(e -> {

            Employee emp = buildEmployeeFromFields();
            if(emp == null) return;

            for(Employee ex : DataStore.employees) {
                if(ex.id.equals(emp.id)) {
                    JOptionPane.showMessageDialog(this,"ID already exists");
                    return;
                }
            }

            DataStore.employees.add(emp);
            loadEmployees();
            clearFields();
        });

        // ===== Update =====
        updateBtn.addActionListener(e -> {

            int row = table.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this,"Select employee row first");
                return;
            }

            Employee updated = buildEmployeeFromFields();
            if(updated == null) return;

            for(int i=0;i<DataStore.employees.size();i++) {
                if(DataStore.employees.get(i).id.equals(updated.id)) {
                    DataStore.employees.set(i, updated);
                    break;
                }
            }

            loadEmployees();
            clearFields();
            JOptionPane.showMessageDialog(this,"Employee Updated ✅");
        });

        // ===== Delete =====
        deleteBtn.addActionListener(e -> {

            int row = table.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this,"Select employee");
                return;
            }

            String empId = table.getValueAt(row,0).toString();
            DataStore.employees.removeIf(emp -> emp.id.equals(empId));

            loadEmployees();
            clearFields();
        });

        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ===== Helpers =====

    JTextField field(String label, int x, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 80, 25);
        add(l);

        JTextField f = new JTextField();
        f.setBounds(x+60, y, 140, 30);
        add(f);
        return f;
    }

    Employee buildEmployeeFromFields() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String dept = depField.getText().trim();
        String email = emailField.getText().trim();
        String mobile = mobileField.getText().trim();
        String salText = salaryField.getText().trim();

        if(id.isEmpty()||name.isEmpty()||dept.isEmpty()
                ||email.isEmpty()||mobile.isEmpty()||salText.isEmpty()) {
            JOptionPane.showMessageDialog(this,"All fields required");
            return null;
        }

        if(!mobile.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,"Mobile must be 10 digits");
            return null;
        }

        double salary;
        try {
            salary = Double.parseDouble(salText);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,"Invalid salary");
            return null;
        }

        return new Employee(id,name,dept,email,mobile,salary);
    }

    void addRow(Employee e) {
        model.addRow(new Object[]{
                e.id, e.name, e.department,
                e.email, e.mobile, e.salary
        });
    }

    void loadEmployees() {
        model.setRowCount(0);
        for(Employee e : DataStore.employees) addRow(e);
    }

    void clearFields() {
        idField.setText("");
        nameField.setText("");
        depField.setText("");
        emailField.setText("");
        mobileField.setText("");
        salaryField.setText("");
    }
}
