package com.zoho.Servletnew.dao;

import java.sql.*;
import java.util.ArrayList;

import com.zoho.Servletnew.bo.TaxBo;
import com.zoho.Servletnew.utils.DatabaseConnection;


public class TaxDaoImpl implements TaxDao{
    @Override
    public int createTax(TaxBo tax) {
        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "insert into tax(name,rate) values(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tax.getName());
            ps.setInt(2, tax.getRate());
            int status = ps.executeUpdate();

            if (0 < status) {
                return status;
            }

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }public ArrayList<TaxBo> getTaxes() {
        ArrayList<TaxBo> list = new ArrayList<TaxBo>();
        TaxBo tax;
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "select * from tax";
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()) {
                tax=new TaxBo();
                tax.setTaxId(resultSet.getInt("taxId"));
                tax.setName(resultSet.getString("name"));
                tax.setRate(resultSet.getInt("rate"));
                list.add(tax);
            }
            st.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    public TaxBo getTaxById(int taxId) {
        TaxBo tax=null;
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "select * from tax where taxId="+taxId;
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()) {
                tax=new TaxBo();
                tax.setTaxId(resultSet.getInt("taxId"));
                tax.setName(resultSet.getString("name"));
                tax.setRate(resultSet.getInt("rate"));
            }
            st.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tax;

    }
    public int editTax(TaxBo tax) {
        int status = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "update tax set name=?,rate=? where taxId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, tax.getName());
            ps.setInt(2, tax.getRate());
            ps.setInt(3, tax.getTaxId());
            status = ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }
    public int deleteTax(int taxId){
        try {
        Connection con = DatabaseConnection.getConnection();

        String sql = "delete from tax where taxId="+taxId;
        Statement st=con.createStatement();
        int status = st.executeUpdate(sql);

        if (0 < status) {
            System.out.println("tax deletion success!!!");
            return status;
        }

        st.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

        return 0;
}
}