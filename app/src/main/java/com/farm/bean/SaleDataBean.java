package com.farm.bean;

import java.util.List;

/**
 * Created by ${hmj} on 2016/6/27.
 */
public class SaleDataBean
{
    String batchtime;
    List<ParkDataBean> parklist;

    public String getBatchtime()
    {
        return batchtime;
    }

    public void setBatchtime(String batchtime)
    {
        this.batchtime = batchtime;
    }

    public List<ParkDataBean> getParklist()
    {
        return parklist;
    }

    public void setParklist(List<ParkDataBean> parklist)
    {
        this.parklist = parklist;
    }
}
