package com.zoho.Servletnew.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zoho.Servletnew.dao.ItemDao;
import com.zoho.Servletnew.dao.ItemDaoImpl;
import com.zoho.Servletnew.bo.ItemBo;
import redis.clients.jedis.Jedis;
import com.zoho.Servletnew.utils.RedisUtil;

import java.util.ArrayList;
import com.zoho.Servletnew.bo.ItemsSold;
import java.util.List;

public class ItemServiceImpl implements ItemService{
    private Gson gson = new Gson();
    ItemDao dao=new ItemDaoImpl();
    @Override
    public int createItems(ItemBo item) {
        int status = dao.createItems(item);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();

            jedis.del("items");
            RedisUtil.closeConnection();
        }
        return status;
    }

    public int editItem(ItemBo item){
        int status = dao.editItem(item);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();

            jedis.del("items");
            RedisUtil.closeConnection();
        }
        return status;

    }

    public ArrayList<ItemBo> getItems(){
        Jedis jedis=RedisUtil.getRedisConnection();
        String redisKey="items";
        String cachedBill=jedis.get(redisKey);

        if(cachedBill!=null){
            RedisUtil.closeConnection();
            return gson.fromJson(cachedBill,new TypeToken<ArrayList<ItemBo>>() {}.getType());
        }
        ArrayList<ItemBo> item=dao.getItems();
        if(item!=null){
            jedis.setex(redisKey,600, gson.toJson(item));
        }
        RedisUtil.closeConnection();
        return item;
    }
    public ItemBo getItemById(int itemId){
        return dao.getItemById(itemId);
    }
    public int deleteItem(int itemId){
        int status = dao.deleteItem(itemId);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();
            jedis.del("items");
            RedisUtil.closeConnection();
        }
        return status;
    }
    public List<ItemsSold>  getItemsSold(int month, int year){
        return dao.getItemsSold(month,year);
    }
    public List<ItemsSold> getItemSoldPerYear(int year){
        return dao.getItemsSoldPerYear(year);
    }
}