package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by ${hmj} on 2016/7/11.
 */
@Table(name = "ParkGoods")
public class ParkGoods implements Parcelable
{

    String id;
    String goodsName;
    String inGoodsValue;
    String GoodsStatistical;
    String goodsunit;
    String expDate;
    String parkId;
    String parkName;
    String goodsTypeId;
    String goodsTypeName;
    String userDefTypeId;
    String userDefTypeName;
    String batchName;
    String goodsInInfoId;
    String firs;
    String firsNum;
    String sec;
    String secNum;
    String three;
    String threeNum;
    String inDate;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getInGoodsValue()
    {
        return inGoodsValue;
    }

    public void setInGoodsValue(String inGoodsValue)
    {
        this.inGoodsValue = inGoodsValue;
    }

    public String getGoodsStatistical()
    {
        return GoodsStatistical;
    }

    public void setGoodsStatistical(String goodsStatistical)
    {
        GoodsStatistical = goodsStatistical;
    }

    public String getGoodsunit()
    {
        return goodsunit;
    }

    public void setGoodsunit(String goodsunit)
    {
        this.goodsunit = goodsunit;
    }

    public String getExpDate()
    {
        return expDate;
    }

    public void setExpDate(String expDate)
    {
        this.expDate = expDate;
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

    public String getGoodsTypeId()
    {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId)
    {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsTypeName()
    {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName)
    {
        this.goodsTypeName = goodsTypeName;
    }

    public String getUserDefTypeId()
    {
        return userDefTypeId;
    }

    public void setUserDefTypeId(String userDefTypeId)
    {
        this.userDefTypeId = userDefTypeId;
    }

    public String getUserDefTypeName()
    {
        return userDefTypeName;
    }

    public void setUserDefTypeName(String userDefTypeName)
    {
        this.userDefTypeName = userDefTypeName;
    }

    public String getGoodsInInfoId()
    {
        return goodsInInfoId;
    }

    public void setGoodsInInfoId(String goodsInInfoId)
    {
        this.goodsInInfoId = goodsInInfoId;
    }

    public String getBatchName()
    {
        return batchName;
    }

    public void setBatchName(String batchName)
    {
        this.batchName = batchName;
    }

    public String getFirsNum()
    {
        return firsNum;
    }

    public void setFirsNum(String firsNum)
    {
        this.firsNum = firsNum;
    }

    public String getFirs()
    {
        return firs;
    }

    public void setFirs(String firs)
    {
        this.firs = firs;
    }

    public String getSec()
    {
        return sec;
    }

    public void setSec(String sec)
    {
        this.sec = sec;
    }

    public String getSecNum()
    {
        return secNum;
    }

    public void setSecNum(String secNum)
    {
        this.secNum = secNum;
    }

    public String getThree()
    {
        return three;
    }

    public void setThree(String three)
    {
        this.three = three;
    }

    public String getThreeNum()
    {
        return threeNum;
    }

    public void setThreeNum(String threeNum)
    {
        this.threeNum = threeNum;
    }

    public String getInDate()
    {
        return inDate;
    }

    public void setInDate(String inDate)
    {
        this.inDate = inDate;
    }
    public static final Creator<ParkGoods> CREATOR = new Creator()
    {
        @Override
        public ParkGoods createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ParkGoods p = new ParkGoods();
            p.setId(source.readString());
            p.setGoodsName(source.readString());
            p.setInGoodsValue(source.readString());
            p.setGoodsStatistical(source.readString());
            p.setGoodsunit(source.readString());
            p.setExpDate(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setGoodsTypeId(source.readString());
            p.setGoodsTypeName(source.readString());
            p.setUserDefTypeId(source.readString());
            p.setUserDefTypeName(source.readString());
            p.setBatchName(source.readString());
            p.setGoodsInInfoId(source.readString());
            p.setFirs(source.readString());
            p.setFirsNum(source.readString());
            p.setSec(source.readString());
            p.setSecNum(source.readString());
            p.setThree(source.readString());
            p.setThreeNum(source.readString());
            p.setInDate(source.readString());
            return  p;
        }

        @Override
        public ParkGoods[] newArray(int size)
        {
            return new ParkGoods[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(goodsName);
        p.writeString(inGoodsValue);
        p.writeString(GoodsStatistical);
        p.writeString(goodsunit);
        p.writeString(expDate);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(goodsTypeId);
        p.writeString(goodsTypeName);
        p.writeString(userDefTypeId);
        p.writeString(userDefTypeName);
        p.writeString(batchName);
        p.writeString(goodsInInfoId);
        p.writeString(firs);
        p.writeString(firsNum);
        p.writeString(sec);
        p.writeString(secNum);
        p.writeString(three);
        p.writeString(threeNum);
        p.writeString(inDate);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }

}
