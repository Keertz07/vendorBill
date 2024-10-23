package com.zoho.Servletnew.Service;
import com.zoho.Servletnew.bo.BillBo;
import com.zoho.Servletnew.bo.BilledItemsBo;
import java.util.List;
import java.util.ArrayList;
import com.zoho.Servletnew.bo.PaymentBo;

public interface BillService{


    public int createBill(BillBo bill,List<BilledItemsBo> billedItemsList);
    public List<BillBo> getBills();
    public BillBo getBillById(int billId);
    public int editBill(BillBo bill,List<BilledItemsBo> billedItemsBoList);
    public int deleteBill(int billId);
    public ArrayList<BillBo> getBillByCustomerId(int id);
    public ArrayList<BillBo> getBillByStatus(String status);
    public ArrayList<BillBo> getBillByDueDate(String date);
    public List<PaymentBo> getPayments();
    public List<PaymentBo> getPaymentsByCustomerId(int customerId);
    public int recordPayment(BillBo bill,String paymentDate);


}