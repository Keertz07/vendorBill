package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zoho.Servletnew.Service.CustomerService;
import com.zoho.Servletnew.bo.CustomerBills;
import com.zoho.Servletnew.Service.CustomerServiceImpl;
import com.zoho.Servletnew.bo.CustomerBo;

@WebServlet("/customers/*")
public class Customer extends HttpServlet {
    public Gson gson = new Gson();
    public CustomerService customerService=new CustomerServiceImpl();
    public void doGet(HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        String pathInfo=req.getPathInfo();
        if(pathInfo==null){
            ArrayList<CustomerBo> customers=customerService.getCustomer();
            res.setContentType("application/json");
            PrintWriter  out=res.getWriter();
            out.println(gson.toJson(customers));
        }else if (pathInfo.matches("/\\d+")) {
            int customerId = Integer.parseInt(pathInfo.substring(1));
            CustomerBo customer = customerService.getCustomerById(customerId);
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            if (customer != null) {
                out.println(gson.toJson(customer));
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"status\":\"Customer not found\"}");
            }

        }
        else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid request\"}");
        }
    }
    public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
        CustomerBo customer=new CustomerBo();
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            String name = jsonObject.get("name").getAsString();
            customer.setName(name);

            String customerType = jsonObject.get("customerType").getAsString();
            customer.setCustomerType(customerType);

            String email = jsonObject.get("email").getAsString();
            customer.setEmail(email);

            long phoneNo = jsonObject.get("phoneNo").getAsLong();
            customer.setPhoneNo(phoneNo);

            String state = jsonObject.get("state").getAsString();
            customer.setState(state);

            String city = jsonObject.get("city").getAsString();
            customer.setCity(city);

            int pincode = jsonObject.get("pincode").getAsInt();
            customer.setPincode(pincode);


            int status=customerService.createCustomer(customer);
            JsonObject response = new JsonObject();

            if(0<status){
                response.addProperty("status", "Customer added");

            }
            else{
                response.addProperty("status","Customer Created Failed");

            }
            out.println(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int customerId = Integer.parseInt(pathInfo.substring(1));
            CustomerBo customer = customerService.getCustomerById(customerId);
            if (customer != null) {
                BufferedReader reader = req.getReader();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                customer.setName(jsonObject.get("name").getAsString());
                customer.setCustomerType(jsonObject.get("customerType").getAsString());
                customer.setEmail(jsonObject.get("email").getAsString());
                customer.setPhoneNo(jsonObject.get("phoneNo").getAsLong());
                customer.setState(jsonObject.get("state").getAsString());
                customer.setCity(jsonObject.get("city").getAsString());
                customer.setPincode(jsonObject.get("pincode").getAsInt());

                int status = customerService.updateCustomer(customer);
                JsonObject response = new JsonObject();
                if (status > 0) {
                    response.addProperty("status", "Customer updated");
                } else {
                    response.addProperty("status", "Customer update failed");
                }
                res.setContentType("application/json");
                res.getWriter().println(response);
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().println("{\"status\":\"Customer not found\"}");
            }
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid customer ID\"}");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int customerId = Integer.parseInt(pathInfo.substring(1));
            int status = customerService.deleteCustomer(customerId);

            JsonObject response = new JsonObject();
            if (status > 0) {
                response.addProperty("status", "Customer deleted");
            } else {
                response.addProperty("status", "Customer deletion failed");
            }
            res.setContentType("application/json");
            res.getWriter().println(response);
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid customer ID\"}");
        }
    }
}

