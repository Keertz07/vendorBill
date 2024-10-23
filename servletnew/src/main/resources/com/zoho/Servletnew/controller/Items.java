package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.zoho.Servletnew.Service.ItemService;
import com.zoho.Servletnew.Service.ItemServiceImpl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zoho.Servletnew.bo.ItemBo;
import com.zoho.Servletnew.bo.ItemsSold;


@WebServlet("/items/*")
public class Items extends HttpServlet {
    public Gson gson = new Gson();
    public ItemService service = new ItemServiceImpl();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo=req.getPathInfo();

        if(pathInfo==null){
            ArrayList<ItemBo> items=service.getItems();
            resp.setContentType("application/json");
            PrintWriter out= resp.getWriter();
            out.println(gson.toJson(items));
        }
        else if(pathInfo.matches("/\\d+")){
            int itemId=Integer.parseInt(pathInfo.substring(1));
            ItemBo item=service.getItemById(itemId);
            resp.setContentType("application/json");
            PrintWriter out=resp.getWriter();
            if(item!=null){
                out.println(gson.toJson(item));
            }
            else{
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"status\":\"Customer not found\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"status\":\"Invalid request\"}");
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ItemBo item = new ItemBo();

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);


            String name = jsonObject.get("name").getAsString();
            item.setName(name);


            double sellingPrice = jsonObject.get("sellingPrice").getAsDouble();
            item.setSellingPrice(sellingPrice);


            double costPrice = jsonObject.get("costPrice").getAsDouble();
            item.setCostPrice(costPrice);

            int quantity = jsonObject.get("quantity").getAsInt();
            item.setQuantity(quantity);

            int taxId=jsonObject.get("taxId").getAsInt();
            item.setTaxId(taxId);


            int status = service.createItems(item);
            JsonObject response = new JsonObject();

            if(0<status){
                response.addProperty("status", "Item added Successfully");
            }
            else{
                response.addProperty("status","Item not added");
            }
            out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void doPut(HttpServletRequest req,HttpServletResponse res) throws ServletException ,IOException{
        String pathInfo=req.getPathInfo();
        if(pathInfo!=null && pathInfo.matches("/\\d+")){
            int itemId=Integer.parseInt(pathInfo.substring(1));
            ItemBo item = service.getItemById(itemId);

            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
           if(item!=null){
                BufferedReader reader = req.getReader();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                String name = jsonObject.get("name").getAsString();
                item.setName(name);

                double sellingPrice = jsonObject.get("sellingPrice").getAsDouble();
                item.setSellingPrice(sellingPrice);

                double costPrice = jsonObject.get("costPrice").getAsDouble();
                item.setCostPrice(costPrice);

                int quantity = jsonObject.get("quantity").getAsInt();
                item.setQuantity(quantity);

                int taxId=jsonObject.get("taxId").getAsInt();
                item.setTaxId(taxId);


                int status = service.editItem(item);
                JsonObject response = new JsonObject();

                if(0<status){
                    response.addProperty("status", "Item updated Successfully");
                }
                else{
                    response.addProperty("status","Item not updated");
                }
                out.println(response);
            }
        }
    }
    public void doDelete(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
        String pathInfo=req.getPathInfo();
        if(pathInfo!=null && pathInfo.matches("/\\d+")){
            int itemId=Integer.parseInt(pathInfo.substring(1));
            int status=service.deleteItem(itemId);

            JsonObject response = new JsonObject();
            if (status > 0) {
                response.addProperty("status", "Item deleted");
            } else {
                response.addProperty("status", "Item deletion failed");
            }
            res.setContentType("application/json");
            res.getWriter().println(response);
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println("{\"status\":\"Invalid Item ID\"}");
        }
        }
    }



