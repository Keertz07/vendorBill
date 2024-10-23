package com.zoho.Servletnew.bo;
import com.zoho.Servletnew.bo.BilledItemsBo;

import java.util.ArrayList;

public class BillBo{
    private int billId;
    private String billNo;
    private int customerId;
    private String billDate;
    private String dueDate;
    private double amount;
    private double balance;
    private String status;
    private ArrayList<BilledItemsBo> billedItems;

    public ArrayList<BilledItemsBo> getBilledItems() {
        return billedItems;
    }

    public void setBilledItems(ArrayList<BilledItemsBo> billedItems) {
        this.billedItems = billedItems;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}