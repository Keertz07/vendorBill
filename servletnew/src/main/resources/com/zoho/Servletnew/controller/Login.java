package com.zoho.Servletnew.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import com.zoho.Servletnew.bo.UserBo;
import com.zoho.Servletnew.Service.UserService;
import com.zoho.Servletnew.Service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class Login extends HttpServlet {
    public Gson gson=new Gson();
    public UserService service=new UserServiceImpl();
    public void doPost(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("application/json");

        UserBo user=new UserBo();

        try{PrintWriter out =resp.getWriter();
            BufferedReader reader=req.getReader();
            JsonObject jsonObject=gson.fromJson(reader,JsonObject.class);

            String email=jsonObject.get("email").getAsString();
            user.setEmail(email);

            String password=jsonObject.get("password").getAsString();
            user.setPassword(password);

            boolean status = service.validateUser(email, password);
            JsonObject response = new JsonObject();

            if (status) {
                HttpSession session = req.getSession();
                session.setAttribute("email", email);
                session.setAttribute("password", password);

                response.addProperty("status","Login Success");
            }
            else{
                response.addProperty("status","Login failed");
            }
            out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}