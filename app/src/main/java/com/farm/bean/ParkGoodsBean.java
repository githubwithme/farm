package com.farm.bean;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/11.
 */
public class ParkGoodsBean
{
    String parkId;
    String parkName;
    String goodsAllValues;
    List<ParkGoods> parkGoodsList;

    public String getParkId()
    {
        return parkId;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getParkName()
    {
        return parkName;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getGoodsAllValues()
    {
        return goodsAllValues;
    }

    public void setGoodsAllValues(String goodsAllValues)
    {
        this.goodsAllValues = goodsAllValues;
    }

    public List<ParkGoods> getParkGoodsList()
    {
        return parkGoodsList;
    }

    public void setParkGoodsList(List<ParkGoods> parkGoodsList)
    {
        this.parkGoodsList = parkGoodsList;
    }


}
