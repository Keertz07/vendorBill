package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.Date;
import java.util.ArrayList;
import com.zoho.Servletnew.bo.BillBo;
import com.zoho.Servletnew.bo.BilledItemsBo;
import com.zoho.Servletnew.Service.BillService;
import com.zoho.Servletnew.Service.BillServiceImpl;
import java.util.List;
import com.zoho.Servletnew.bo.PaymentBo;

@WebServlet("/bills/*")
public class Bill extends HttpServlet {
    public BillService service=new BillServiceImpl();
    public Gson gson = new Gson();
    public void doGet(HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter out = res.getWriter();
        String customerIdParam = req.getParameter("customerId");
        String dueDateParam=req.getParameter("dueDate");
        String statusParam=req.getParameter("status");

        if (customerIdParam != null && !customerIdParam.isEmpty()) {
            int customerId = Integer.parseInt(customerIdParam);
            ArrayList<BillBo> list = service.getBillByCustomerId(customerId);

            if (list != null && !list.isEmpty()) {
                String json = gson.toJson(list);
                out.println(json);
            } else {
                out.println(gson.toJson(new ArrayList<BillBo>()));
            }
            return;
        }
        if (dueDateParam != null && !dueDateParam.isEmpty()) {
            ArrayList<BillBo> list = service.getBillByDueDate(dueDateParam);

            if (list != null && !list.isEmpty()) {
                String json = gson.toJson(list);
                out.println(json);
            } else {
                out.println(gson.toJson(new ArrayList<PaymentBo>()));
            }
            return;
        }if (statusParam != null && !statusParam.isEmpty()) {
            ArrayList<BillBo> list = service.getBillByStatus(statusParam);

            if (list != null && !list.isEmpty()) {
                String json = gson.toJson(list);
                out.println(json);
            } else {
                out.println(gson.toJson(new ArrayList<PaymentBo>()));
            }
            return;
        }
        if (pathInfo == null) {
            List<BillBo> bills =service.getBills();
            System.out.println(bills);
            res.setContentType("application/json");
            out.println(gson.toJson(bills));
        } else if (pathInfo.matches("/\\d+")) {
            int billId = Integer.parseInt(pathInfo.substring(1));
            BillBo bill = service.getBillById(billId);
            res.setContentType("application/json");

            if (bill!= null) {
                out.println(gson.toJson(bill));
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"status\":\"Bill not found\"}");
            }

        }
        else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid request\"}");
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BillBo bill = new BillBo();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            int name = jsonObject.get("customer").getAsInt();
            bill.setCustomerId(name);

            String billNo = "BILL-" + jsonObject.get("billNo").getAsString();
            bill.setBillNo(billNo);

            String billDate = jsonObject.get("billDate").getAsString();
            bill.setBillDate(billDate);

            String dueDate = jsonObject.get("dueDate").getAsString();
            bill.setDueDate(dueDate);

            double amount = 0;



            bill.setStatus("UNPAID");

            JsonArray itemIds=jsonObject.get("itemId").getAsJsonArray();
            JsonArray quantities = jsonObject.get("quantity").getAsJsonArray();
            JsonArray prices = jsonObject.get("prices").getAsJsonArray();
            JsonArray taxId = jsonObject.get("taxId").getAsJsonArray();
            JsonArray taxes = jsonObject.get("tax").getAsJsonArray();
            JsonArray discounts = jsonObject.get("discount").getAsJsonArray();

            List<BilledItemsBo> billedItemsList = new ArrayList<>();

            for (int i = 0; i < itemIds.size(); i++) {
                BilledItemsBo billedItem = new BilledItemsBo();
                billedItem.setProductId(itemIds.get(i).getAsInt());
                billedItem.setQuantity(quantities.get(i).getAsInt());
                billedItem.setPricePerUnit(prices.get(i).getAsDouble());
                billedItem.setTaxId(taxId.get(i).getAsInt());
                billedItem.setTaxAmnt(taxes.get(i).getAsDouble() * (quantities.get(i).getAsDouble() * prices.get(i).getAsDouble() / 100));
                billedItem.setTaxRate(taxes.get(i).getAsInt());
                billedItem.setDiscount(discounts.get(i).getAsInt());
                billedItemsList.add(billedItem);
                double subtotal=(quantities.get(i).getAsInt()*prices.get(i).getAsInt());
                double discountAmount=subtotal*discounts.get(i).getAsInt()/100;
                subtotal-=discountAmount;
                double taxAmount=(subtotal*taxes.get(i).getAsInt())/100;
                amount+=subtotal+taxAmount;
            }
            bill.setAmount(amount);
            bill.setBalance(amount);

            int status = service.createBill(bill, billedItemsList);
            JsonObject response = new JsonObject();

            if(0<status){
                response.addProperty("status", "Bill creation Successfull");

            }
            else{
                response.addProperty("status","Bill Creation Failed");

            }
            out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doPut(HttpServletRequest req,HttpServletResponse resp) throws  ServletException,IOException{
        String pathInfo=req.getPathInfo();
        PrintWriter out=resp.getWriter();
        if(pathInfo !=null && pathInfo.matches("/\\d+")){
            int billId=Integer.parseInt(pathInfo.substring(1));
            BillBo bill=service.getBillById(billId);
            if(bill!=null){
                BufferedReader reader = req.getReader();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                bill.setBillId(billId);
                int name = jsonObject.get("customer").getAsInt();
                bill.setCustomerId(name);

                String billNo = "BILL-" + jsonObject.get("billNo").getAsString();
                bill.setBillNo(billNo);

                String billDate = jsonObject.get("billDate").getAsString();
                bill.setBillDate(billDate);

                String dueDate = jsonObject.get("dueDate").getAsString();
                bill.setDueDate(dueDate);

                double amount = 0;

                bill.setStatus("UNPAID");

                JsonArray itemIds=jsonObject.get("itemId").getAsJsonArray();
                JsonArray quantities = jsonObject.get("quantity").getAsJsonArray();
                JsonArray prices = jsonObject.get("prices").getAsJsonArray();
                JsonArray taxId = jsonObject.get("taxId").getAsJsonArray();
                JsonArray taxes = jsonObject.get("tax").getAsJsonArray();
                JsonArray discounts = jsonObject.get("discount").getAsJsonArray();

                List<BilledItemsBo> billedItemsList = new ArrayList<>();

                for (int i = 0; i < itemIds.size(); i++) {
                    BilledItemsBo billedItem = new BilledItemsBo();
                    billedItem.setProductId(itemIds.get(i).getAsInt());
                    billedItem.setQuantity(quantities.get(i).getAsInt());
                    billedItem.setPricePerUnit(prices.get(i).getAsDouble());
                    billedItem.setTaxId(taxId.get(i).getAsInt());
                    billedItem.setTaxAmnt(taxes.get(i).getAsDouble() * (quantities.get(i).getAsDouble() * prices.get(i).getAsDouble() / 100));
                    billedItem.setTaxRate(taxes.get(i).getAsInt());
                    billedItem.setDiscount(discounts.get(i).getAsInt());
                    billedItemsList.add(billedItem);
                    double subtotal=(quantities.get(i).getAsInt()*prices.get(i).getAsInt());
                    double discountAmount=subtotal*discounts.get(i).getAsInt()/100;
                    subtotal-=discountAmount;
                    double taxAmount=(subtotal*taxes.get(i).getAsInt())/100;
                    amount=subtotal+taxAmount;
                }
                bill.setAmount(amount);
                bill.setBalance(amount);
                int status=service.editBill(bill,billedItemsList);
                JsonObject response = new JsonObject();

                if(0<status){
                    response.addProperty("status", "Bill Edited Successfull");

                }
                else{
                    response.addProperty("status","Bill Edit Failed");
                }
                out.println(response);

            }

            }
        }
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int billId = Integer.parseInt(pathInfo.substring(1));
            int status = service.deleteBill(billId);

            JsonObject response = new JsonObject();
            if (status > 0) {
                response.addProperty("status", "Bill deleted");
            } else {
                response.addProperty("status", "Bill deletion failed");
            }
            res.setContentType("application/json");
            res.getWriter().println(response);
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid customer ID\"}");
        }
    }
    }
