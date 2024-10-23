package com.zoho.Servletnew.Service;
import com.google.gson.Gson;
import com.zoho.Servletnew.Service.CustomerService;
import com.zoho.Servletnew.dao.CustomerDao;
import com.zoho.Servletnew.dao.CustomerDaoImpl;
import com.zoho.Servletnew.bo.CustomerBo;
import redis.clients.jedis.Jedis;
import com.zoho.Servletnew.utils.RedisUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.zoho.Servletnew.bo.CustomerBills;

public class CustomerServiceImpl implements CustomerService{
    public Gson gson=new Gson();
    CustomerDao dao=new CustomerDaoImpl();
     @Override public int createCustomer(CustomerBo customer){
        int status=dao.createCustomer(customer);
        if(status>0){
            Jedis jedis=RedisUtil.getRedisConnection();
            jedis.del("customers");
            RedisUtil.closeConnection();
        }
        return status;
     }
     @Override public ArrayList<CustomerBo> getCustomer(){
         Jedis jedis=RedisUtil.getRedisConnection();
         String redisKey="customers";
         String cachedBill=jedis.get(redisKey);
         if(cachedBill!=null){
             RedisUtil.closeConnection();
             return gson.fromJson(cachedBill,new TypeToken<ArrayList<CustomerBo>>() {}.getType());
         }
         ArrayList<CustomerBo> customer=dao.getCustomer();
         if(customer!=null){
             jedis.setex(redisKey,600,gson.toJson(customer));
         }
         RedisUtil.closeConnection();
         return customer;

     }
     public CustomerBo getCustomerById(int customerId){
         return dao.getCustomerById(customerId);
     }

     public int updateCustomer(CustomerBo customer){
         int status=dao.updateCustomer(customer);
         if(status>0){
             Jedis jedis=RedisUtil.getRedisConnection();
             jedis.del("customers");

             RedisUtil.closeConnection();
         }
         return status;
     }

     public int deleteCustomer(int customerId){
        CustomerBo customer=getCustomerById(customerId);
        int status=dao.deleteCustomer(customerId);
        if(status>0 && customer!=null){
            Jedis jedis=RedisUtil.getRedisConnection();
            jedis.del("customers");

            RedisUtil.closeConnection();
        }return status;
     }
    public List<CustomerBills> getCustomerBills(){
         return dao.getCustomerBills();
    }

}