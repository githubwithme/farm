package com.farm.bean;

import java.io.Serializable;
import java.util.HashMap;

public class Dictionary_wheel implements Serializable
{
    String[] firstItemName;
    String[] firstItemID;
    HashMap<String, String[]> secondItemName;
    HashMap<String, String[]> secondItemID;

    public void setFirstItemID(String[] firstItemID)
    {
        this.firstItemID = firstItemID;
    }

    public String[] getFirstItemID()
    {
        return firstItemID;
    }

    public void setFirstItemName(String[] firstItemName)
    {
        this.firstItemName = firstItemName;
    }

    public String[] getFirstItemName()
    {
        return firstItemName;
    }

    public void setSecondItemID(HashMap<String, String[]> secondItemID)
    {
        this.secondItemID = secondItemID;
    }

    public HashMap<String, String[]> getSecondItemID()
    {
        return secondItemID;
    }

    public void setSecondItemName(HashMap<String, String[]> secondItemName)
    {
        this.secondItemName = secondItemName;
    }

    public HashMap<String, String[]> getSecondItemName()
    {
        return secondItemName;
    }
}
