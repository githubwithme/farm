package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by hasee on 2016/7/12.
 */
@Table(name = "ListYc")
public class ListYc implements Parcelable
{
    public String id;
    public String parkId;
    public String parkName;
    public String parkCount;
    public List<WZ_YCxx> goodsLists;

    public String getParkId()
    {
        return parkId;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParkName()
    {
        return parkName;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getParkCount()
    {
        return parkCount;
    }

    public void setParkCount(String parkCount)
    {
        this.parkCount = parkCount;
    }

    public List<WZ_YCxx> getGoodsLists()
    {
        return goodsLists;
    }

    public void setGoodsLists(List<WZ_YCxx> goodsLists)
    {
        this.goodsLists = goodsLists;
    }
    public static final Parcelable.Creator<ListYc> CREATOR = new Parcelable.Creator()
    {
        @Override
        public ListYc createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ListYc p = new ListYc();
            p.setId(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setParkCount(source.readString());
            p.goodsLists = source.readArrayList(plantgrowthtab.class.getClassLoader());
            return p;
        }
        @Override
        public ListYc[] newArray(int size)
        {
            return new ListYc[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(parkCount);
        p.writeList(goodsLists);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
