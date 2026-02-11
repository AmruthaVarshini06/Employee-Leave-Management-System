package data;

public class Employee {

    public String id;
    public String name;
    public String department;
    public String email;
    public String mobile;
    public double salary;

    public Employee(String id, String name, String dept,
                    String email, String mobile, double salary) {

        this.id = id;
        this.name = name;
        this.department = dept;
        this.email = email;
        this.mobile = mobile;
        this.salary = salary;
    }
}
