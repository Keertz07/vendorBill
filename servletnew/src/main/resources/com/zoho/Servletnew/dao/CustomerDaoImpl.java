package com.zoho.Servletnew.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zoho.Servletnew.utils.DatabaseConnection;
import com.zoho.Servletnew.bo.CustomerBo;
import com.zoho.Servletnew.dao.CustomerDao;
import com.zoho.Servletnew.bo.CustomerBills;

public class CustomerDaoImpl implements  CustomerDao{
    @Override
    public int createCustomer(CustomerBo customer){
        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "insert into customer(name,customerType,email,phoneNo,state,city,pincode) values(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getCustomerType());
            ps.setString(3, customer.getEmail());
            ps.setLong(4, customer.getPhoneNo());
            ps.setString(5, customer.getState());
            ps.setString(6,customer.getCity());
            ps.setInt(7,customer.getPincode());
            int status = ps.executeUpdate();

            if (0 < status) {
                System.out.println("Customer table insertion success!!!");
                return status;
            }

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public ArrayList<CustomerBo> getCustomer(){
        ArrayList<CustomerBo> list=new ArrayList<CustomerBo>();
        CustomerBo customer;
        try{
            Connection con=DatabaseConnection.getConnection();
            String query="select * from customer";
            Statement st=con.createStatement();
            ResultSet resultSet=st.executeQuery(query);
            while(resultSet.next()){
                customer=new CustomerBo();
                customer.setCustomerId(resultSet.getInt("customerId"));
                customer.setName(resultSet.getString("name"));
                customer.setCustomerType(resultSet.getString("customerType"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhoneNo(resultSet.getLong("phoneNo"));
                customer.setState(resultSet.getString("state"));
                customer.setCity(resultSet.getString("city"));
                customer.setPincode(resultSet.getInt("pincode"));
                list.add(customer);
            }
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public CustomerBo getCustomerById(int customerId){

        CustomerBo customer=null;
        try{
            Connection con=DatabaseConnection.getConnection();
            String query="select * from customer where customerId= ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, customerId);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                customer=new CustomerBo();
                customer.setCustomerId(resultSet.getInt("customerId"));
                customer.setName(resultSet.getString("name"));
                customer.setCustomerType(resultSet.getString("customerType"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhoneNo(resultSet.getLong("phoneNo"));
                customer.setState(resultSet.getString("state"));
                customer.setCity(resultSet.getString("city"));
                customer.setPincode(resultSet.getInt("pincode"));

            }
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
    public int updateCustomer(CustomerBo customer) {
        int status = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "UPDATE customer SET name = ?, customerType = ?, email = ?, phoneNo = ?, state = ?, city = ?, pincode = ? WHERE customerId = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getCustomerType());
            ps.setString(3, customer.getEmail());
            ps.setLong(4, customer.getPhoneNo());
            ps.setString(5, customer.getState());
            ps.setString(6, customer.getCity());
            ps.setInt(7, customer.getPincode());
            ps.setInt(8, customer.getCustomerId());

            status = ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int deleteCustomer(int customerId){
        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "delete from customer where customerId="+customerId;
            Statement st=con.createStatement();
            int status = st.executeUpdate(sql);

            if (0 < status) {
                System.out.println("Customer deletion success!!!");
                return status;
            }

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public List<CustomerBills> getCustomerBills(){
        List<CustomerBills> customerBills=new ArrayList<>();
        try{
            Connection con=DatabaseConnection.getConnection();
            String sql="select c.customerId as customer_id,c.name as customer_name ,count(b.billId) as no_of_bills,round(sum(b.amount),2) as total_amount , round(sum(b.balance),2) as balance_amount from bill b join customer c on b.customerId=c.customerId group by customer_id order by total_amount desc;";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            CustomerBills bill=null;
            while (rs.next()) {
                bill=new CustomerBills();
                bill.setCustomer_id(rs.getInt("customer_id"));
                bill.setCustomer_name(rs.getString("customer_name"));
                bill.setNo_of_bills(rs.getInt("no_of_bills"));
                bill.setTotal_amount(rs.getDouble("total_amount"));
                bill.setBalance_amount(rs.getDouble("balance_amount"));
                customerBills.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerBills;
    }

}