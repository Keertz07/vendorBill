package com.zoho.Servletnew.bo;

public class BilledItemsBo {

    private int billedItemsId;
    private int billId;
    private int productId;
    private int quantity;
    private double pricePerUnit;
    private int taxId;
    private double taxAmnt;
    private int taxRate;
    private int discount;
    private double subTotal;

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getBilledItemsId() {
        return billedItemsId;
    }

    public void setBilledItemsId(int billedItemsId) {
        this.billedItemsId = billedItemsId;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getTaxAmnt() {
        return taxAmnt;
    }

    public void setTaxAmnt(double taxAmnt) {
        this.taxAmnt = taxAmnt;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}