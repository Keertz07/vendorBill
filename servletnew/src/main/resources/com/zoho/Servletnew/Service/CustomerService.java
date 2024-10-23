package com.zoho.Servletnew.Service;
import com.zoho.Servletnew.bo.CustomerBo;
import java.util.ArrayList;
import java.util.List;
import com.zoho.Servletnew.bo.CustomerBills;

public interface CustomerService {
    public int createCustomer(CustomerBo customer);
    public ArrayList<CustomerBo> getCustomer();
    public CustomerBo getCustomerById(int customerId);
    public int updateCustomer(CustomerBo customer);
    public int deleteCustomer(int customerId);
    public List<CustomerBills> getCustomerBills();
}