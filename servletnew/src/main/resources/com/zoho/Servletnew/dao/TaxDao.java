package com.zoho.Servletnew.dao;
import com.zoho.Servletnew.bo.TaxBo;

import java.util.ArrayList;

public interface TaxDao {
    public int createTax(TaxBo tax);
    public ArrayList<TaxBo> getTaxes();
    public TaxBo getTaxById(int taxId);
    public int editTax(TaxBo tax);
    public int deleteTax(int taxId);
}