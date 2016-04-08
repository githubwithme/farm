package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
@Table(name = "WZ_CRk")
public class WZ_CRk implements Parcelable
{
    public String inDate;
    public String batchNumber;
    public String batchName;
    public String loadingFee;
    public String shippingFee;
    public String inGoodsValue;
    public String inType;  //c
    public List<WZ_RKxx> wzcrkxx;


    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getInGoodsValue() {
        return inGoodsValue;
    }

    public void setInGoodsValue(String inGoodsValue) {
        this.inGoodsValue = inGoodsValue;
    }

    public String getLoadingFee() {
        return loadingFee;
    }

    public void setLoadingFee(String loadingFee) {
        this.loadingFee = loadingFee;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public List<WZ_RKxx> getWzcrkxx() {
        return wzcrkxx;
    }

    public void setWzcrkxx(List<WZ_RKxx> wzcrkxx) {
        this.wzcrkxx = wzcrkxx;
    }
    public static final Parcelable.Creator<WZ_CRk> CREATOR = new Creator()
    {
        @Override
        public WZ_CRk createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            WZ_CRk p = new WZ_CRk();
            p.setInDate(source.readString());
            p.setBatchNumber(source.readString());
            p.setBatchName(source.readString());
            p.setLoadingFee(source.readString());
            p.setShippingFee(source.readString());
            p.setInGoodsValue(source.readString());
            p.setInType(source.readString());
            p.wzcrkxx = source.readArrayList(plantgrowthtab.class.getClassLoader());
            return p;
        }

        @Override
        public WZ_CRk[] newArray(int size)
        {
            return new WZ_CRk[size];
        }
    };
    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(inDate);
        p.writeString(batchNumber);
        p.writeString(batchName);
        p.writeString(loadingFee);
        p.writeString(shippingFee);
        p.writeString(inGoodsValue);
        p.writeString(inType);
        p.writeList(wzcrkxx);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
