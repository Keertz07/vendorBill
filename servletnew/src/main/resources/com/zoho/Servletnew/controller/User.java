package com.zoho.Servletnew.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.zoho.Servletnew.bo.UserBo;
import com.zoho.Servletnew.Service.UserService;
import com.zoho.Servletnew.Service.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class User extends HttpServlet{
    public Gson gson=new Gson();
    public UserService service=new UserServiceImpl();

    public void doPost (HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
        resp.setContentType("application/json");
        PrintWriter out =resp.getWriter();
        UserBo user=new UserBo();

        try{
            BufferedReader reader=req.getReader();
            JsonObject jsonObject=gson.fromJson(reader,JsonObject.class);

            String username=jsonObject.get("username").getAsString();
            user.setUsername(username);

            String email=jsonObject.get("email").getAsString();
            user.setEmail(email);

            String password=jsonObject.get("password").getAsString();
            user.setPassword(password);

            int status=service.register(user);
            JsonObject response=new JsonObject();
            if(status>0){
              response.addProperty("status","User created Successfully");
            }
            else{
                response.addProperty("status","User creation failed");
            }
            out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}