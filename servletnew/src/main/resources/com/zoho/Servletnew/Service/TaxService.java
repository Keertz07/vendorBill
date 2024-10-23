package com.zoho.Servletnew.Service;
import com.zoho.Servletnew.bo.TaxBo;

import java.util.ArrayList;

public interface TaxService {

    public int createTax(TaxBo tax);
    public ArrayList<TaxBo> getTaxes();
    public TaxBo getTaxById(int taxId);
    public int editTax(TaxBo tax);
    public int deleteTax(int taxId);

}