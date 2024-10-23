package com.zoho.Servletnew.dao;
import com.zoho.Servletnew.bo.ItemBo;
import com.zoho.Servletnew.dao.ItemDao;

import java.sql.*;

import com.zoho.Servletnew.utils.DatabaseConnection;
import com.zoho.Servletnew.bo.ItemsSold;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDaoImpl implements ItemDao{
    @Override
    public int createItems(ItemBo item) {
        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "insert into items(name,sellingPrice,costPrice,quantity,taxId) values(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, item.getName());
            ps.setDouble(2, item.getSellingPrice());
            ps.setDouble(3, item.getCostPrice());
            ps.setInt(4, item.getQuantity());
            ps.setInt(5, item.getTaxId());

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
    }
    public ArrayList<ItemBo> getItems(){
        ArrayList<ItemBo> list=new ArrayList<ItemBo>();
        ItemBo item;
        try{
            Connection con=DatabaseConnection.getConnection();
            String query="select * from items";
            Statement st=con.createStatement();
            ResultSet resultSet=st.executeQuery(query);
            while(resultSet.next()){
                item=new ItemBo();
                item.setProductId(resultSet.getInt("productId"));
                item.setName(resultSet.getString("name"));
                item.setSellingPrice(resultSet.getDouble("sellingPrice"));
                item.setCostPrice(resultSet.getDouble("costPrice"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setTaxId(resultSet.getInt("taxId"));

                list.add(item);
            }
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public ItemBo getItemById(int itemId){
        ItemBo item=null;
        try{
            Connection con=DatabaseConnection.getConnection();
            String query="select * from items where productId="+itemId;
            Statement st=con.createStatement();
            ResultSet resultSet=st.executeQuery(query);
            while(resultSet.next()){
                item=new ItemBo();
                item.setProductId(resultSet.getInt("productId"));
                item.setName(resultSet.getString("name"));
                item.setSellingPrice(resultSet.getDouble("sellingPrice"));
                item.setCostPrice(resultSet.getDouble("costPrice"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setTaxId(resultSet.getInt("taxId"));

            }
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;

    }

    public int editItem(ItemBo item){
        int status=0;
        try{
            Connection con=DatabaseConnection.getConnection();
            String query="update items set name=?,sellingPrice=?,costPrice=?,quantity=?,taxId=? where productId=?";
            PreparedStatement ps= con.prepareStatement(query);

            ps.setString(1,item.getName());
            ps.setDouble(2, item.getSellingPrice());
            ps.setDouble(3,item.getCostPrice());
            ps.setInt(4,item.getQuantity());
            ps.setInt(5,item.getTaxId());
            ps.setInt(6,item.getProductId());
            status=ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    public int deleteItem(int itemId){
        try{
            Connection con = DatabaseConnection.getConnection();

            String sql = "delete from items where productId="+itemId;
            Statement st=con.createStatement();
            int status = st.executeUpdate(sql);

            if (0 < status) {
                System.out.println("Item deletion success!!!");
                return status;
            }

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<ItemsSold>  getItemsSold(int month, int year){
        List<ItemsSold> list =new ArrayList<>();
        try{
            Connection con=DatabaseConnection.getConnection();
            String sql="select i.productId as item_id, i.name as items_name,sum(bi.quantity) as no_of_items ,sum(bi.subTotal) as total_amount from bill b join billedItems bi on b.billId=bi.billId join items i on bi.productId=i.productId where month(billDate)="+month+" and year(billDate)="+year+" group by item_id ;";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            ItemsSold items=null;
            while(rs.next()){
                items=new ItemsSold();
                items.setItem_id(rs.getInt("item_id"));
                items.setItems_name(rs.getString("items_name"));
                items.setNo_of_items_sold(rs.getInt("no_of_items"));
                items.setTotal_amount(rs.getDouble("total_amount"));
                list.add(items);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ItemsSold> getItemsSoldPerYear(int year){
        List<ItemsSold> list =new ArrayList<>();
        try{
            Connection con=DatabaseConnection.getConnection();
            String sql="select i.productId as item_id, i.name as items_name,sum(bi.quantity) as no_of_items ,sum(bi.subTotal) as total_amount from bill b join billedItems bi on b.billId=bi.billId join items i on bi.productId=i.productId where year(billDate)="+year+" group by item_id ;";
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            ItemsSold items=null;
            while(rs.next()){
                items=new ItemsSold();
                items.setItem_id(rs.getInt("item_id"));
                items.setItems_name(rs.getString("items_name"));
                items.setNo_of_items_sold(rs.getInt("no_of_items"));
                items.setTotal_amount(rs.getDouble("total_amount"));
                list.add(items);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}