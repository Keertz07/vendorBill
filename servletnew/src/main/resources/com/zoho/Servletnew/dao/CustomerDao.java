package com.zoho.Servletnew.dao;

import com.zoho.Servletnew.bo.CustomerBo;

import java.util.ArrayList;
import com.zoho.Servletnew.bo.CustomerBills;
import java.util.List;

public interface CustomerDao {
    public int createCustomer(CustomerBo customer);
    public ArrayList<CustomerBo> getCustomer();
    public CustomerBo getCustomerById(int customerId);
    public int updateCustomer(CustomerBo customer);
    public int deleteCustomer(int customerId);
    public List<CustomerBills> getCustomerBills();
}