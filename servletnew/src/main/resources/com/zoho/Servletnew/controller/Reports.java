package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.zoho.Servletnew.bo.CustomerBills;
import java.io.IOException;
import java.io.PrintWriter;
import com.zoho.Servletnew.bo.ItemsSold;
import java.util.List;
import com.zoho.Servletnew.Service.ItemService;
import com.zoho.Servletnew.Service.ItemServiceImpl;
import com.zoho.Servletnew.Service.CustomerService;
import com.zoho.Servletnew.Service.CustomerServiceImpl;

@WebServlet("/reports/*")
public class Reports extends HttpServlet {
    public Gson gson = new Gson();
    public ItemService service = new ItemServiceImpl();
    public CustomerService customerService=new CustomerServiceImpl();

    public void doGet(HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        String monthParam=req.getParameter("month");
        String yearParam=req.getParameter("year");
        PrintWriter out = res.getWriter();
        if (pathInfo.equals("/items-sales-summary")) {
            if (yearParam == null && monthParam != null) {
                out.println("To get reports 'year' should be Entered");
                return;
            }
            if (monthParam != null && yearParam != null && !monthParam.isEmpty() && !yearParam.isEmpty()) {
                int month = Integer.parseInt(monthParam);
                int year = Integer.parseInt(yearParam);
                List<ItemsSold> itemsSold = service.getItemsSold(month, year);
                if (itemsSold != null) {
                    res.setContentType("application/json");
                    String json = gson.toJson(itemsSold);

                    out.println(json);
                    return;
                }

            }
            if (yearParam != null && monthParam == null) {
                int year = Integer.parseInt(yearParam);
                List<ItemsSold> itemsSold = service.getItemSoldPerYear(year);
                if (itemsSold != null) {
                    res.setContentType("application/json");
                    String json = gson.toJson(itemsSold);

                    out.println(json);
                    return;
                }
            }
        }
        else if(pathInfo.matches("/customer-bill-summary")){
            List<CustomerBills> customerBills=customerService.getCustomerBills();
            res.setContentType("application/json");

            if(customerBills!=null){
                out.println(gson.toJson(customerBills));
            }else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"status\":\"Customer Bills are none\"}");
            }
        }



    }


}