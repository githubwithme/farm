package com.farm.bean;

import java.io.Serializable;
import java.util.List;

public class Dictionary implements Serializable
{
    String BELONG;
    List<String> SORT;
    List<String> firstItemName;
    List<String> firstItemID;
    List<List<String>> secondItemName;
    List<List<String>> secondItemID;

    public void setBELONG(String bELONG)
    {
        BELONG = bELONG;
    }

    public String getBELONG()
    {
        return BELONG;
    }

    public void setSORT(List<String> sORT)
    {
        SORT = sORT;
    }

    public List<String> getSORT()
    {
        return SORT;
    }

    public void setFirstItemID(List<String> firstItemID)
    {
        this.firstItemID = firstItemID;
    }

    public List<String> getFirstItemID()
    {
        return firstItemID;
    }

    public void setSecondItemID(List<List<String>> secondItemID)
    {
        this.secondItemID = secondItemID;
    }

    public List<List<String>> getSecondItemID()
    {
        return secondItemID;
    }

    public void setFirstItemName(List<String> firstItemName)
    {
        this.firstItemName = firstItemName;
    }

    public List<String> getFirstItemName()
    {
        return firstItemName;
    }

    public List<List<String>> getSecondItemName()
    {
        return secondItemName;
    }

    public void setSecondItemName(List<List<String>> secondItemName)
    {
        this.secondItemName = secondItemName;
    }
}
