package com.zoho.Servletnew.bo;
public class ItemsSold {
    private int item_id;
    private String items_name;
    private int no_of_items_sold;
    private double total_amount;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItems_name() {
        return items_name;
    }

    public void setItems_name(String items_name) {
        this.items_name = items_name;
    }

    public int getNo_of_items_sold() {
        return no_of_items_sold;
    }

    public void setNo_of_items_sold(int no_of_items_sold) {
        this.no_of_items_sold = no_of_items_sold;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }
}