package com.zoho.Servletnew.Service;
import com.zoho.Servletnew.Service.UserService;
import  com.zoho.Servletnew.bo.UserBo;
import com.zoho.Servletnew.dao.UserDao;
import com.zoho.Servletnew.dao.UserDaoImpl;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{
    public UserDao dao=new UserDaoImpl();
    public int register(UserBo user)throws SQLException, NoSuchAlgorithmException{
        return dao.register(user);
    }
    public boolean validateUser(String email, String password) {
        return dao.validateUser(email,password);
    }
}