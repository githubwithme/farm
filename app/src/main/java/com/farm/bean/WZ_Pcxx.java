package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/7.
 */
@Table(name = "Wz_Storehouse")
public class WZ_Pcxx  implements Parcelable
{

    public String batchNumber;
    public String batchName;
    public String parkId;
    public String parkName;
    public String storehouseId ;
    public String storehouseName;
    public String inDate;
    public String expDate;
    public String number;
    public String quantity;
    public String inGoodsValue;
    public String sumWeight;

    public String getSumWeight()
    {
        return sumWeight;
    }

    public void setSumWeight(String sumWeight)
    {
        this.sumWeight = sumWeight;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
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

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(String storehouseId) {
        this.storehouseId = storehouseId;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public static final Creator<WZ_Pcxx> CREATOR = new Creator()
    {
        @Override
        public WZ_Pcxx createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            WZ_Pcxx p = new WZ_Pcxx();

            p.setBatchNumber(source.readString());//仓库物资
            p.setBatchName(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setStorehouseId(source.readString());
            p.setStorehouseName(source.readString());
            p.setInDate(source.readString());
            p.setExpDate(source.readString());
            p.setNumber(source.readString());
            p.setQuantity(source.readString());
            p.setInGoodsValue(source.readString());
            p.setSumWeight(source.readString());


            return  p;
        }

        @Override
        public WZ_Pcxx[] newArray(int size)
        {
            return new WZ_Pcxx[size];
        }
    };
    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(batchNumber);
        p.writeString(batchName);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(storehouseId);
        p.writeString(storehouseName);
        p.writeString(expDate);
        p.writeString(inDate);
        p.writeString(number);
        p.writeString(quantity);
        p.writeString(inGoodsValue);
        p.writeString(sumWeight);







    }

    @Override
    public int describeContents()
    {
        return 0;
    }


}
