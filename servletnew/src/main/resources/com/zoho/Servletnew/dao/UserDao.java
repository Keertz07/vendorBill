package com.zoho.Servletnew.dao;

import com.zoho.Servletnew.bo.UserBo;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface UserDao {
    public int register(UserBo user)throws SQLException, NoSuchAlgorithmException;
    public boolean validateUser(String email, String password) ;
}