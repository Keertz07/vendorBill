package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.zoho.Servletnew.bo.TaxBo;
import com.zoho.Servletnew.Service.TaxServiceImpl;
import com.zoho.Servletnew.Service.TaxService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/taxes/*")
public class Tax extends HttpServlet {
    public Gson gson = new Gson();
    public TaxService service=new TaxServiceImpl();
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String pathInfo=req.getPathInfo();
        if(pathInfo==null){
            ArrayList<TaxBo> taxes=service.getTaxes();
            res.setContentType("application/json");
            PrintWriter  out=res.getWriter();
            out.println(gson.toJson(taxes));
        }else if (pathInfo.matches("/\\d+")) {
            int taxId = Integer.parseInt(pathInfo.substring(1));
            TaxBo tax = service.getTaxById(taxId);
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            if (tax != null) {
                out.println(gson.toJson(tax));
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"status\":\"Customer not found\"}");
            }
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid request\"}");
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        PrintWriter out = resp.getWriter();


        TaxBo tax = new TaxBo();
        resp.setContentType("application/json");
        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);


            String name = jsonObject.get("name").getAsString();
            tax.setName(name);
            System.out.println(name);

            int rate = jsonObject.get("rate").getAsInt();
            tax.setRate(rate);

            int status = service.createTax(tax);
            JsonObject response = new JsonObject();

            if (0 < status) {
                response.addProperty("status", "new Tax created Successfully");
            } else {
                response.addProperty("status", "Tax creation failed ");
            }
            out.println(response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int taxId = Integer.parseInt(pathInfo.substring(1));
            TaxBo tax = service.getTaxById(taxId);
            if (tax != null) {
                BufferedReader reader = req.getReader();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);


                String name = jsonObject.get("name").getAsString();
                tax.setName(name);
                System.out.println(name);

                int rate = jsonObject.get("rate").getAsInt();
                tax.setRate(rate);

                int status = service.editTax(tax);
                JsonObject response = new JsonObject();

                if (0 < status) {
                    response.addProperty("status", "Tax Edit Successfully");
                } else {
                    response.addProperty("status", "Tax Edit failed ");
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
        public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                int taxId = Integer.parseInt(pathInfo.substring(1));
                int status = service.deleteTax(taxId);

                JsonObject response = new JsonObject();
                if (status > 0) {
                    response.addProperty("status", "tax deleted");
                } else {
                    response.addProperty("status", "taxdeletion failed");
                }
                res.setContentType("application/json");
                res.getWriter().println(response);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().println("{\"status\":\"Invalid tax ID\"}");
            }
        }

}
