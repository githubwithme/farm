package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/11.
 */
@Table(name = "ParkGoodsBean")
public class ParkGoodsBean implements Parcelable
{
    String parkId;
    String parkName;
    String goodsAllValues;
    String goodsCount;
    String storehouseId;
    String storehouseName;

    List<ParkGoods> goodsStockLists;
    List<WZ_Pcxx> batchNameLists;


    public List<WZ_Pcxx> getBatchNameLists()
    {
        return batchNameLists;
    }

    public void setBatchNameLists(List<WZ_Pcxx> batchNameLists)
    {
        this.batchNameLists = batchNameLists;
    }

    public String getStorehouseId()
    {
        return storehouseId;
    }

    public void setStorehouseId(String storehouseId)
    {
        this.storehouseId = storehouseId;
    }

    public String getStorehouseName()
    {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName)
    {
        this.storehouseName = storehouseName;
    }

    public String getGoodsCount()
    {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount)
    {
        this.goodsCount = goodsCount;
    }

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

    public List<ParkGoods> getGoodsStockLists()
    {
        return goodsStockLists;
    }

    public void setGoodsStockLists(List<ParkGoods> goodsStockLists)
    {
        this.goodsStockLists = goodsStockLists;
    }

    public static final Creator<ParkGoodsBean> CREATOR = new Creator()
    {
        @Override
        public ParkGoodsBean createFromParcel(Parcel source)
        {


            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ParkGoodsBean p = new ParkGoodsBean();
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setGoodsAllValues(source.readString());
            p.setGoodsAllValues(source.readString());
            p.setStorehouseId(source.readString());
            p.setStorehouseName(source.readString());
            p.goodsStockLists = source.readArrayList(plantgrowthtab.class.getClassLoader());
            p.batchNameLists = source.readArrayList(plantgrowthtab.class.getClassLoader());
            return  p;
        }

        @Override
        public ParkGoodsBean[] newArray(int size)
        {
            return new ParkGoodsBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {


        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(goodsAllValues);
        p.writeString(goodsCount);
        p.writeString(storehouseId);
        p.writeString(storehouseName);
        p.writeList(goodsStockLists);
        p.writeList(batchNameLists);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
