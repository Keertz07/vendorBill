package com.zoho.Servletnew.Service;

import com.zoho.Servletnew.Service.BillService;
import com.zoho.Servletnew.bo.BillBo;
import com.zoho.Servletnew.bo.BilledItemsBo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zoho.Servletnew.dao.BillDao;
import com.zoho.Servletnew.dao.BillDaoImpl;
import com.zoho.Servletnew.bo.PaymentBo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;
import com.zoho.Servletnew.utils.RedisUtil;

public class BillServiceImpl implements BillService{
    BillDao dao=new BillDaoImpl();
    private Gson gson = new Gson();
    @Override
    public int createBill(BillBo bill, List<BilledItemsBo> billedItemsList) {
        int status = dao.createBill(bill, billedItemsList);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();

            jedis.del("bills");

            RedisUtil.closeConnection();
        }
        return status;
    }

    public List<BillBo> getBills(){

        Jedis jedis=RedisUtil.getRedisConnection();
        String redisKey="bills";
        String cachedBill=jedis.get(redisKey);

        if(cachedBill!=null){
            RedisUtil.closeConnection();
            return gson.fromJson(cachedBill,new TypeToken<List<BillBo>>() {}.getType());
        }
        List<BillBo> bill=dao.getBills();
        if(bill!=null){
            jedis.setex(redisKey,600, gson.toJson(bill));
        }
        RedisUtil.closeConnection();
        return bill;

    }
    public BillBo getBillById(int billId){
        Jedis jedis=RedisUtil.getRedisConnection();
        String redisKey="bill_by_id:"+billId;
        String cachedBill=jedis.get(redisKey);

        if(cachedBill!=null){
            RedisUtil.closeConnection();
            return gson.fromJson(cachedBill,new TypeToken<List<BillBo>>() {}.getType());
        }
        BillBo bill=dao.getBillById(billId);
        if(bill!=null){
            jedis.setex(redisKey,600, gson.toJson(bill));
        }
        RedisUtil.closeConnection();
        return bill;

    }
    public int editBill(BillBo bill,List<BilledItemsBo> billedItemsBoList){
        int status = dao.editBill(bill, billedItemsBoList);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();

            jedis.del("bills");

            RedisUtil.closeConnection();
        }
        return status;
    }
    public int deleteBill(int billId){
        BillBo bill = getBillById(billId);
        int status = dao.deleteBill(billId);
        if (status > 0 && bill != null) {
            Jedis jedis = RedisUtil.getRedisConnection();
            jedis.del("bills");

            RedisUtil.closeConnection();
        }
        return status;
    }
    public ArrayList<BillBo> getBillByCustomerId(int id){

        Jedis jedis = RedisUtil.getRedisConnection();

        String cachedBills = jedis.get("bills");
        ArrayList<BillBo> bills = new ArrayList<>();

        if (cachedBills != null) {
            bills = gson.fromJson(cachedBills, new TypeToken<ArrayList<BillBo>>() {}.getType());
            System.out.println("Cached data");

            bills = (ArrayList<BillBo>) bills.stream()
                    .filter(bill -> bill.getCustomerId()==id)
                    .collect(Collectors.toList());


            if (!bills.isEmpty()) {
                RedisUtil.closeConnection();
                return bills;
            }
        }

        bills = dao.getBillByCustomerId(id);

        RedisUtil.closeConnection();
        return bills;

    }

    public int recordPayment(BillBo bill,String paymentDate){
        int status = dao.recordPayment(bill, paymentDate);
        if (status > 0) {
            Jedis jedis = RedisUtil.getRedisConnection();
            jedis.del("bills");
            RedisUtil.closeConnection();

        }
        return status;
    }
    public ArrayList<BillBo> getBillByStatus(String status){
        Jedis jedis = RedisUtil.getRedisConnection();

        String cachedBills = jedis.get("bills");
        ArrayList<BillBo> bills = new ArrayList<>();

        if (cachedBills != null) {
            bills = gson.fromJson(cachedBills, new TypeToken<ArrayList<BillBo>>() {}.getType());
            System.out.println("Cached data");

            bills = (ArrayList<BillBo>) bills.stream()
                    .filter(bill -> bill.getStatus().equals(status))
                    .collect(Collectors.toList());


            if (!bills.isEmpty()) {
                RedisUtil.closeConnection();
                return bills;
            }
        }

        bills = dao.getBillByStatus(status);

        RedisUtil.closeConnection();
        return bills;
    }
    public ArrayList<BillBo> getBillByDueDate(String date) {
        Jedis jedis = RedisUtil.getRedisConnection();

        String cachedBills = jedis.get("bills");
        ArrayList<BillBo> bills = new ArrayList<>();

        if (cachedBills != null) {
            bills = gson.fromJson(cachedBills, new TypeToken<ArrayList<BillBo>>() {
            }.getType());
            System.out.println("Cached data");

            bills = (ArrayList<BillBo>) bills.stream()
                    .filter(bill -> bill.getDueDate().equals(date))
                    .collect(Collectors.toList());


            if (!bills.isEmpty()) {
                RedisUtil.closeConnection();
                return bills;
            }
        }
        bills = dao.getBillByDueDate(date);

        RedisUtil.closeConnection();
        return bills;
    }
    public List<PaymentBo> getPayments(){
        Jedis jedis = RedisUtil.getRedisConnection();
        String redisKey = "payments";

        String cachedPayments = jedis.get(redisKey);
        if (cachedPayments != null) {
            RedisUtil.closeConnection();
            return gson.fromJson(cachedPayments, new TypeToken<List<PaymentBo>>() {}.getType());
        }

        List<PaymentBo> payments = dao.getPayments();
        if (payments != null && !payments.isEmpty()) {
            jedis.setex(redisKey, 600, gson.toJson(payments));
        }

        RedisUtil.closeConnection();
        return payments;
    }
    public List<PaymentBo> getPaymentsByCustomerId(int customerId){

            Jedis jedis = RedisUtil.getRedisConnection();

            String cachedBills = jedis.get("payments");
            List<PaymentBo> payments = new ArrayList<>();

            if (cachedBills != null) {
                payments = gson.fromJson(cachedBills, new TypeToken<List<PaymentBo>>() {
                }.getType());
                System.out.println("Cached data");

                payments = (List<PaymentBo>) payments.stream()
                        .filter(payment -> payment.getCustomerId() == customerId)
                        .collect(Collectors.toList());


                if (!payments.isEmpty()) {
                    RedisUtil.closeConnection();
                    return payments;
                }
            }
                payments=dao.getPaymentsByCustomerId(customerId);

                RedisUtil.closeConnection();
                return payments;

    }

}