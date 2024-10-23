package com.zoho.Servletnew.dao;
import com.zoho.Servletnew.bo.BilledItemsBo;
import com.zoho.Servletnew.bo.BillBo;
import java.util.List;
import java.util.ArrayList;
import com.zoho.Servletnew.bo.PaymentBo;




public interface BillDao {
    public int createBill(BillBo bill ,List<BilledItemsBo> billedItemsList);

    public List<BillBo> getBills();
    public BillBo getBillById(int billId);

    public ArrayList<BillBo> getBillByCustomerId(int id);
    public int editBill(BillBo bill,List<BilledItemsBo> billedItemsBoList);
    public int deleteBill(int billId);
    public ArrayList<BillBo> getBillByStatus(String status);

    public ArrayList<BillBo> getBillByDueDate(String date);
    public List<PaymentBo> getPayments();
    public List<PaymentBo> getPaymentsByCustomerId(int customerId);

    public int recordPayment(BillBo bill,String paymentDate);
}