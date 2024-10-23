package com.zoho.Servletnew.dao;
import com.zoho.Servletnew.bo.ItemBo;

import java.util.ArrayList;
import java.util.List;

import com.zoho.Servletnew.bo.ItemsSold;

public interface ItemDao {
    public int createItems(ItemBo item);
    public ItemBo getItemById(int itemId);
    public int editItem(ItemBo item);
    public int deleteItem(int itemId);
    public ArrayList<ItemBo> getItems();
    public List<ItemsSold> getItemsSold(int month, int year);
    public List<ItemsSold> getItemsSoldPerYear(int year);
}