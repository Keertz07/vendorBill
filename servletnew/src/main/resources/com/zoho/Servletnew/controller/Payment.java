package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.zoho.Servletnew.Service.BillService;
import com.zoho.Servletnew.Service.BillServiceImpl;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import com.zoho.Servletnew.bo.PaymentBo;
import com.zoho.Servletnew.Service.CustomerService;
import com.zoho.Servletnew.Service.CustomerServiceImpl;
import com.zoho.Servletnew.bo.CustomerBo;
import com.zoho.Servletnew.bo.BillBo;


@WebServlet("/payments/*")
public class Payment extends HttpServlet{
    public Gson gson = new Gson();
    public BillService service = new BillServiceImpl();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

            String customerIdParam = req.getParameter("customerId");

            String pathInfo = req.getPathInfo();

            List<PaymentBo> paymentList = new ArrayList<>();

            try {
                if (pathInfo == null ) {
                    paymentList = service.getPayments();

                } else if (customerIdParam != null) {

                    int customerId = Integer.parseInt(customerIdParam);
                    paymentList = service.getPaymentsByCustomerId(customerId);
                }
                res.setContentType("application/json");
                PrintWriter  out=res.getWriter();
                out.println(gson.toJson(paymentList));


            } catch (Exception e) {
                e.printStackTrace();
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
            }
        }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out=resp.getWriter();
        BillBo bill=new BillBo();
        BillService service=new BillServiceImpl();
        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);



            int customerId = jsonObject.get("customerId").getAsInt();
            bill.setCustomerId(customerId);
            int billId = jsonObject.get("billId").getAsInt();
            bill.setBillId(billId);

            double amount =jsonObject.get("amount").getAsDouble();
            bill.setAmount(amount);

            String paymentDate=jsonObject.get("paymentDate").getAsString();

            int status=service.recordPayment(bill,paymentDate);
            JsonObject response = new JsonObject();

            if(0<status){
                response.addProperty("status", "New Payment Done Successfully");
            }
            else{
                response.addProperty("status","New Payment Failed");
            }
            out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}