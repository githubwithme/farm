package com.farm.bean;

import android.view.View;

import java.util.List;

public class Selector extends Entity
{
    List<String> firstSelectType;
    List<String> sortOptions;
    List<String> titleOptions;
    List<Integer> res;
    List<View> selectlaout;

    public void setFirstSelectType(List<String> firstSelectType)
    {
        this.firstSelectType = firstSelectType;
    }

    public List<String> getFirstSelectType()
    {
        return firstSelectType;
    }

    public void setSortOptions(List<String> sortOptions)
    {
        this.sortOptions = sortOptions;
    }

    public List<String> getSortOptions()
    {
        return sortOptions;
    }

    public void setTitleOptions(List<String> titleOptions)
    {
        this.titleOptions = titleOptions;
    }

    public List<String> getTitleOptions()
    {
        return titleOptions;
    }

    public void setRes(List<Integer> res)
    {
        this.res = res;
    }

    public List<Integer> getRes()
    {
        return res;
    }

    public void setSelectlaout(List<View> selectlaout)
    {
        this.selectlaout = selectlaout;
    }

    public List<View> getSelectlaout()
    {
        return selectlaout;
    }
}
