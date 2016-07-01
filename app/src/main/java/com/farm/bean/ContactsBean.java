package com.farm.bean;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/1.
 */
public class ContactsBean
{
    String Type;
    List<commembertab> commembertablist;

    public String getType()
    {
        return Type;
    }

    public void setType(String type)
    {
        Type = type;
    }

    public List<commembertab> getCommembertablist()
    {
        return commembertablist;
    }

    public void setCommembertablist(List<commembertab> commembertablist)
    {
        this.commembertablist = commembertablist;
    }
}
