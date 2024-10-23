package com.zoho.Servletnew.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.zoho.Servletnew.bo.BillBo;
import com.zoho.Servletnew.bo.BilledItemsBo;
import com.zoho.Servletnew.dao.BillDao;
import com.zoho.Servletnew.utils.DatabaseConnection;
import com.zoho.Servletnew.bo.PaymentBo;

public class BillDaoImpl implements BillDao {

    @Override
    public int createBill(BillBo bill, List<BilledItemsBo> billedItemsList){
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO bill (billNo,customerId, billDate, dueDate, amount, balance, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bill.getBillNo());
            ps.setInt(2, bill.getCustomerId());
            ps.setString(3, bill.getBillDate());
            ps.setString(4, bill.getDueDate());
            ps.setDouble(5, bill.getAmount());
            ps.setDouble(6, bill.getBalance());
            ps.setString(7, bill.getStatus());

            int status = ps.executeUpdate();
            if (status > 0) {
                int billId = 0;

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        billId = generatedKeys.getInt(1);
                    }
                }

                int overallStatus = 0;
                for (BilledItemsBo item : billedItemsList) {
                    String query = "INSERT INTO billedItems (billId, productId, quantity, pricePerUnit, taxId, taxAmount, taxRate, discount, subTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps1 = conn.prepareStatement(query)) {
                        ps1.setInt(1, billId);
                        ps1.setInt(2, item.getProductId());
                        ps1.setInt(3, item.getQuantity());
                        ps1.setDouble(4, item.getPricePerUnit());
                        ps1.setInt(5, item.getTaxId());
                        ps1.setDouble(6, item.getTaxAmnt());
                        ps1.setInt(7, item.getTaxRate());
                        ps1.setInt(8, item.getDiscount());
                        ps1.setDouble(9, item.getQuantity() * item.getPricePerUnit());

                        int status1 = ps1.executeUpdate();

                        String query1 = "UPDATE items SET quantity = (quantity - ?) WHERE productId = ?;";
                        try (PreparedStatement ps2 = conn.prepareStatement(query1)) {
                            ps2.setInt(1, item.getQuantity());
                            ps2.setInt(2, item.getProductId());
                            int status2 = ps2.executeUpdate();
                        }

                        if (status1 > 0) {
                            overallStatus += status1;
                        }
                    }
                }

                return overallStatus;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public List<BillBo> getBills(){
        List<BillBo> list=new ArrayList<>();
        BillBo bill;
        try{
            Connection con=DatabaseConnection.getConnection();
            String query="SELECT * from bill";
            Statement st=con.createStatement();
            ResultSet resultSet=st.executeQuery(query);
            while(resultSet.next()){
                bill=new BillBo();
                bill.setBillId(resultSet.getInt("billId"));
                bill.setBillNo(resultSet.getString("billNo"));
                bill.setCustomerId(resultSet.getInt("customerId"));
                bill.setBillDate(resultSet.getString("billDate"));
                bill.setDueDate(resultSet.getString("dueDate"));
                bill.setAmount(resultSet.getDouble("amount"));
                bill.setBalance(resultSet.getDouble("balance"));
                bill.setStatus(resultSet.getString("status"));

                list.add(bill);
            }
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public BillBo getBillById(int billId) {
        BillBo bill = null;

        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM bill WHERE billId = " + billId;
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(query);

            if (resultSet.next()) {
                bill = new BillBo();
                bill.setBillId(resultSet.getInt("billId"));
                bill.setBillNo(resultSet.getString("billNo"));
                bill.setCustomerId(resultSet.getInt("customerId"));
                bill.setBillDate(resultSet.getString("billDate"));
                bill.setDueDate(resultSet.getString("dueDate"));
                bill.setAmount(resultSet.getDouble("amount"));
                bill.setBalance(resultSet.getDouble("balance"));
                bill.setStatus(resultSet.getString("status"));

                ArrayList<BilledItemsBo> billedItemsList = new ArrayList<>();
                String billedItemsQuery = "SELECT * FROM billedItems WHERE billId = " + billId;
                Statement billedItemsSt = con.createStatement();
                ResultSet billedItemsRs = billedItemsSt.executeQuery(billedItemsQuery);

                while (billedItemsRs.next()) {
                    BilledItemsBo billedItem = new BilledItemsBo();

                    billedItem.setBilledItemsId(billedItemsRs.getInt("billedItemsId"));
                    billedItem.setBillId(billedItemsRs.getInt("billId"));
                    billedItem.setProductId(billedItemsRs.getInt("productId"));
                    billedItem.setQuantity(billedItemsRs.getInt("quantity"));
                    billedItem.setPricePerUnit(billedItemsRs.getDouble("pricePerUnit"));
                    billedItem.setTaxId(billedItemsRs.getInt("taxId"));
                    billedItem.setTaxAmnt(billedItemsRs.getDouble("taxAmount"));
                    billedItem.setTaxRate(billedItemsRs.getInt("taxRate"));
                    billedItem.setDiscount(billedItemsRs.getInt("discount"));
                    billedItem.setSubTotal(billedItemsRs.getDouble("subTotal"));

                    billedItemsList.add(billedItem);
                }

                billedItemsSt.close();

                bill.setBilledItems(billedItemsList);
            }

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bill;
    }


    public ArrayList<BillBo> getBillByCustomerId(int customerId){
        ArrayList<BillBo> list=new ArrayList<>();
        BillBo bill=null;
            try{
                Connection con=DatabaseConnection.getConnection();
                String query="SELECT * from bill where customerId="+customerId;
                Statement st=con.createStatement();
                ResultSet resultSet=st.executeQuery(query);
                while(resultSet.next()){
                    bill=new BillBo();
                    bill.setBillId(resultSet.getInt("billId"));
                    bill.setBillNo(resultSet.getString("billNo"));
                    bill.setCustomerId(resultSet.getInt("customerId"));
                    bill.setBillDate(resultSet.getString("billDate"));
                    bill.setDueDate(resultSet.getString("dueDate"));
                    bill.setAmount(resultSet.getDouble("amount"));
                    bill.setBalance(resultSet.getDouble("balance"));
                    bill.setStatus(resultSet.getString("status"));
                    list.add(bill);

                }
                st.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;

    }

        public int recordPayment(BillBo bill,String paymentDate) {

                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    con = DatabaseConnection.getConnection();

                    String checkBalanceSQL = "SELECT balance FROM bill WHERE billId = ?";
                    ps = con.prepareStatement(checkBalanceSQL);
                    ps.setInt(1, bill.getBillId());
                    rs = ps.executeQuery();

                    double currentBalance = 0;
                    if (rs.next()) {
                        currentBalance = rs.getDouble("balance");
                        System.out.println(currentBalance);
                    }

                    if (bill.getAmount() > currentBalance) {
                        System.out.println("Payment amount exceeds the current balance. Payment not allowed.");
                        return 0;
                    }

                    String updateBalanceSQL = "UPDATE bill SET balance = ROUND(balance - ?, 2), status = 'Partially Paid' WHERE billId = ? AND customerId = ?";
                    ps = con.prepareStatement(updateBalanceSQL);
                    ps.setDouble(1, bill.getAmount());
                    ps.setInt(2, bill.getBillId());
                    ps.setInt(3, bill.getCustomerId());

                    int status = ps.executeUpdate();

                    String updateStatusSQL = "UPDATE bill SET status = 'Paid' WHERE billId = ? AND customerId = ? AND balance = 0";
                    ps = con.prepareStatement(updateStatusSQL);
                    ps.setInt(1, bill.getBillId());
                    ps.setInt(2, bill.getCustomerId());
                    ps.executeUpdate();

                    if (status > 0) {
                        String insertPaymentSQL = "INSERT INTO payments (billId, customerId, paymentDate, amount) VALUES (?, ?, ?, ?)";
                        PreparedStatement ps3 = con.prepareStatement(insertPaymentSQL);
                        ps3.setInt(1, bill.getBillId());
                        ps3.setInt(2, bill.getCustomerId());
                        ps3.setString(3, paymentDate);
                        ps3.setDouble(4, bill.getAmount());
                        ps3.executeUpdate();
                        System.out.println("Payment update success!!!");
                        return status;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                        if (con != null) con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return 0;


        }

    public ArrayList<BillBo> getBillByStatus(String status){
        ArrayList<BillBo> list=new ArrayList<>();

        BillBo bill=null;
            try{
                Connection con=DatabaseConnection.getConnection();
                String query="SELECT * from bill where status='" +status + "'";
                Statement st=con.createStatement();
                ResultSet resultSet=st.executeQuery(query);
                while(resultSet.next()){
                    bill=new BillBo();
                    bill.setBillId(resultSet.getInt("billId"));
                    bill.setBillNo(resultSet.getString("billNo"));
                    bill.setCustomerId(resultSet.getInt("customerId"));
                    bill.setBillDate(resultSet.getString("billDate"));
                    bill.setDueDate(resultSet.getString("dueDate"));
                    bill.setAmount(resultSet.getDouble("amount"));
                    bill.setBalance(resultSet.getDouble("balance"));
                    bill.setStatus(resultSet.getString("status"));
                    list.add(bill);

                }
                st.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

    public ArrayList<BillBo> getBillByDueDate(String date){
            ArrayList<BillBo> list=new ArrayList<>();
            BillBo bill=null;
            try{
                Connection con=DatabaseConnection.getConnection();
                String query="SELECT * from bill where dueDate='" +date + "'";
                Statement st=con.createStatement();
                ResultSet resultSet=st.executeQuery(query);
                while(resultSet.next()){
                    bill=new BillBo();
                    bill.setBillId(resultSet.getInt("billId"));
                    bill.setBillNo(resultSet.getString("billNo"));
                    bill.setCustomerId(resultSet.getInt("customerId"));
                    bill.setBillDate(resultSet.getString("billDate"));
                    bill.setDueDate(resultSet.getString("dueDate"));
                    bill.setAmount(resultSet.getDouble("amount"));
                    bill.setBalance(resultSet.getDouble("balance"));
                    bill.setStatus(resultSet.getString("status"));
                    list.add(bill);

                }
                st.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;

    }

    public int editBill(BillBo bill, List<BilledItemsBo> billedItemsList) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE bill SET billNo = ?, customerId = ?, billDate = ?, dueDate = ?, amount = ?, balance = ?, status = ? WHERE billId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bill.getBillNo());
            ps.setInt(2, bill.getCustomerId());
            ps.setString(3, bill.getBillDate());
            ps.setString(4, bill.getDueDate());
            ps.setDouble(5, bill.getAmount());
            ps.setDouble(6, bill.getBalance());
            ps.setString(7, bill.getStatus());
            ps.setInt(8, bill.getBillId());

            int status = ps.executeUpdate();
            if (status > 0) {

                String fetchQuery = "SELECT productId, quantity FROM billedItems WHERE billId = ?";
                Map<Integer, Integer> existingItemQuantities = new HashMap<>();

                try (PreparedStatement fetchPs = conn.prepareStatement(fetchQuery)) {
                    fetchPs.setInt(1, bill.getBillId());
                    try (ResultSet rs = fetchPs.executeQuery()) {
                        while (rs.next()) {
                            existingItemQuantities.put(rs.getInt("productId"), rs.getInt("quantity"));
                        }
                    }
                }

                Set<Integer> processedItemIds = new HashSet<>();
                for (BilledItemsBo item : billedItemsList) {
                    processedItemIds.add(item.getProductId());

                    int newQuantity = item.getQuantity();
                    if (existingItemQuantities.containsKey(item.getProductId())) {
                        int currentQuantity = existingItemQuantities.get(item.getProductId());
                        int quantityDifference = newQuantity - currentQuantity;

                        String updateQuery = "UPDATE billedItems SET quantity = ?, pricePerUnit = ?, taxId = ?, taxAmount = ?, taxRate = ?, discount = ?, subTotal = ? WHERE billId = ? AND productId = ?";
                        try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                            updatePs.setInt(1, newQuantity);
                            updatePs.setDouble(2, item.getPricePerUnit());
                            updatePs.setInt(3, item.getTaxId());
                            updatePs.setDouble(4, item.getTaxAmnt());
                            updatePs.setInt(5, item.getTaxRate());
                            updatePs.setInt(6, item.getDiscount());
                            updatePs.setDouble(7, newQuantity * item.getPricePerUnit());
                            updatePs.setInt(8, bill.getBillId());
                            updatePs.setInt(9, item.getProductId());
                            updatePs.executeUpdate();
                        }

                        String updateStockQuery = "UPDATE items SET quantity = quantity + ? WHERE productId = ?";
                        try (PreparedStatement stockPs = conn.prepareStatement(updateStockQuery)) {
                            stockPs.setInt(1, -quantityDifference);  // subtract if new quantity > old, add if new quantity < old
                            stockPs.setInt(2, item.getProductId());
                            stockPs.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO billedItems (billId, productId, quantity, pricePerUnit, taxId, taxAmount, taxRate, discount, subTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                            insertPs.setInt(1, bill.getBillId());
                            insertPs.setInt(2, item.getProductId());
                            insertPs.setInt(3, newQuantity);
                            insertPs.setDouble(4, item.getPricePerUnit());
                            insertPs.setInt(5, item.getTaxId());
                            insertPs.setDouble(6, item.getTaxAmnt());
                            insertPs.setInt(7, item.getTaxRate());
                            insertPs.setInt(8, item.getDiscount());
                            insertPs.setDouble(9, newQuantity * item.getPricePerUnit());
                            insertPs.executeUpdate();
                        }

                        String updateStockQuery = "UPDATE items SET quantity = quantity - ? WHERE productId = ?";
                        try (PreparedStatement stockPs = conn.prepareStatement(updateStockQuery)) {
                            stockPs.setInt(1, newQuantity);
                            stockPs.setInt(2, item.getProductId());
                            stockPs.executeUpdate();
                        }
                    }
                }


                for (Integer existingItemId : existingItemQuantities.keySet()) {
                    if (!processedItemIds.contains(existingItemId)) {

                        int oldQuantity = existingItemQuantities.get(existingItemId);
                        String updateStockQuery = "UPDATE items SET quantity = quantity + ? WHERE productId = ?";
                        try (PreparedStatement stockPs = conn.prepareStatement(updateStockQuery)) {
                            stockPs.setInt(1, oldQuantity);
                            stockPs.setInt(2, existingItemId);
                            stockPs.executeUpdate();
                        }

                        String deleteQuery = "DELETE FROM billedItems WHERE billId = ? AND productId = ?";
                        try (PreparedStatement deletePs = conn.prepareStatement(deleteQuery)) {
                            deletePs.setInt(1, bill.getBillId());
                            deletePs.setInt(2, existingItemId);
                            deletePs.executeUpdate();
                        }
                    }
                }

                return status;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteBill(int billId){
        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "delete from bill where billId="+billId;
            Statement st=con.createStatement();
            int status = st.executeUpdate(sql);

            if (0 < status) {
                System.out.println("Bill Deletion success!!!");
                return status;
            }

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<PaymentBo> getPayments(){
        List<PaymentBo> paymentList = new ArrayList<>();

        String query = "SELECT * FROM payments";

        try{
        Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PaymentBo payment = new PaymentBo();
                payment.setPaymentId(rs.getInt("paymentId"));
                payment.setBillId(rs.getInt("billId"));
                payment.setCustomerId(rs.getInt("customerId"));
                payment.setPaymentDate(rs.getString("paymentDate"));
                payment.setAmount(rs.getDouble("amount"));

                paymentList.add(payment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentList;


    }
    public List<PaymentBo> getPaymentsByCustomerId(int customerId){
        List<PaymentBo> paymentList = new ArrayList<>();

        String query = "SELECT * FROM payments WHERE customerId = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);

           stmt.setInt(1, customerId);

            ResultSet rs = stmt.executeQuery(); {
                while (rs.next()) {
                    PaymentBo payment = new PaymentBo();
                    payment.setPaymentId(rs.getInt("paymentId"));
                    payment.setBillId(rs.getInt("billId"));
                    payment.setCustomerId(rs.getInt("customerId"));
                    payment.setPaymentDate(rs.getString("paymentDate"));
                    payment.setAmount(rs.getDouble("amount"));

                    paymentList.add(payment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentList;
    }

}
