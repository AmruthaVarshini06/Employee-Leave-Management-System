package data;

public class Leave {

    public String employee;
    public String type;
    public String fromDate;
    public String toDate;
    public String reason;
    public String status;

    public Leave(String e, String t, String f, String to, String r) {
        employee = e;
        type = t;
        fromDate = f;
        toDate = to;
        reason = r;
        status = "Pending";
    }
}
