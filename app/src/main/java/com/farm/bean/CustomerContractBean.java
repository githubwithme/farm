package com.farm.bean;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/1.
 */
public class CustomerContractBean
{
    String customerName;
    String customerPhone;
    List<AgreementBean> agreementBeanlist;


    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public List<AgreementBean> getAgreementBeanlist()
    {
        return agreementBeanlist;
    }

    public void setAgreementBeanlist(List<AgreementBean> agreementBeanlist)
    {
        this.agreementBeanlist = agreementBeanlist;
    }
}
