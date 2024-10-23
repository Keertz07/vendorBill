package  com.zoho.Servletnew.bo;

public class CustomerBills {
    private int customer_id;
    private String customer_name;
    private int no_of_bills;
    private double total_amount;
    private double balance_amount;

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getNo_of_bills() {
        return no_of_bills;
    }

    public void setNo_of_bills(int no_of_bills) {
        this.no_of_bills = no_of_bills;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(double balance_amount) {
        this.balance_amount = balance_amount;
    }
}