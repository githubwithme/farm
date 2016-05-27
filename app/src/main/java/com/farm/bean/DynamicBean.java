package com.farm.bean;

import java.util.List;

/**
 * Created by ${hmj} on 2016/5/27.
 */
public class DynamicBean
{
    String type;
    public List<DynamicEntity> listdata;


    public void setListdata(List<DynamicEntity> listdata)
    {
        this.listdata = listdata;
    }

    public List<DynamicEntity> getListdata()
    {
        return listdata;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}

