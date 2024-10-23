package com.zoho.Servletnew.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zoho.Servletnew.Service.TaxService;
import com.zoho.Servletnew.dao.TaxDao;
import com.zoho.Servletnew.dao.TaxDaoImpl;
import com.zoho.Servletnew.bo.TaxBo;
import redis.clients.jedis.Jedis;
import com.zoho.Servletnew.utils.RedisUtil;
import java.util.ArrayList;
import java.util.List;

public class TaxServiceImpl implements TaxService{
    public Gson gson=new Gson();
    TaxDao dao=new TaxDaoImpl();
    public int createTax(TaxBo tax){
        int status = dao.createTax(tax);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();

            jedis.del("taxes");
            RedisUtil.closeConnection();
        }
        return status;
    }

    public ArrayList<TaxBo> getTaxes(){
        Jedis jedis=RedisUtil.getRedisConnection();
        String redisKey="taxes";
        String cachedBill=jedis.get(redisKey);

        if(cachedBill!=null){
            RedisUtil.closeConnection();
            return gson.fromJson(cachedBill,new TypeToken<ArrayList<TaxBo>>() {}.getType());
        }
        ArrayList<TaxBo> tax=dao.getTaxes();
        if(tax!=null){
            jedis.setex(redisKey,600, gson.toJson(tax));
        }
        RedisUtil.closeConnection();
        return tax;

    }
    public TaxBo getTaxById(int taxId){
        return dao.getTaxById(taxId);
    }
    public int editTax(TaxBo tax){
        int status = dao.editTax(tax);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();

            jedis.del("taxes");
            RedisUtil.closeConnection();
        }
        return status;
    }
    public int deleteTax(int taxId){
        TaxBo bill = getTaxById(taxId);
        int status = dao.deleteTax(taxId);
        if (status > 0 && bill != null) {
            Jedis jedis = RedisUtil.getRedisConnection();
            jedis.del("taxes");
           RedisUtil.closeConnection();
        }
        return status;
    }
}