package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/6.
 */
@Table(name = "Wz_Storehouse")
public class Wz_Storehouse implements Parcelable
{

    public String Id;
    //仓库
    public String parkId;
    public String parkName;
    public String storehouseId ;
    public String storehouseName;
    public String storeName;
    //仓库物资
    public String goodsId;
    public String goodsName;


    public String goodsStatistical;
    public String goodsunit;

    public String batchNumber;//仓库物资
    public String batchName;

    public String quantity;

    public String inGoodsValue;//仓库物资
    public String expDate;
    public String inDate;

    public String avePrice;
    public String stockValue;


    public String getGoodsStatistical()
    {
        return goodsStatistical;
    }

    public void setGoodsStatistical(String goodsStatistical)
    {
        this.goodsStatistical = goodsStatistical;
    }

    public String getGoodsunit()
    {
        return goodsunit;
    }

    public void setGoodsunit(String goodsunit)
    {
        this.goodsunit = goodsunit;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getAvePrice() {
        return avePrice;
    }

    public void setAvePrice(String avePrice) {
        this.avePrice = avePrice;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getStockValue() {
        return stockValue;
    }

    public void setStockValue(String stockValue) {
        this.stockValue = stockValue;
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

    public static final Creator<Wz_Storehouse> CREATOR = new Creator()
    {
        @Override
        public Wz_Storehouse createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Wz_Storehouse p = new Wz_Storehouse();
            p.setId(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setStorehouseId(source.readString());
            p.setStorehouseName(source.readString());
            p.setStoreName(source.readString());
            p.setGoodsId(source.readString());
            p.setGoodsName(source.readString());
            p.setGoodsStatistical(source.readString());
            p.setGoodsunit(source.readString());

            p.setBatchNumber(source.readString());//仓库物资
            p.setBatchName(source.readString());

            p.setQuantity(source.readString());

            p.setInGoodsValue(source.readString());
            p.setExpDate(source.readString());
            p.setInDate(source.readString());



            p.setAvePrice(source.readString());
            p.setStockValue(source.readString());
            return  p;
        }

        @Override
        public Wz_Storehouse[] newArray(int size)
        {
            return new Wz_Storehouse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {

        p.writeString(Id);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(storehouseId);
        p.writeString(storehouseName);
        p.writeString(storeName);

        p.writeString(goodsId);
        p.writeString(goodsName);

        p.writeString(batchNumber);
        p.writeString(batchName);

        p.writeString(quantity);

        p.writeString(inGoodsValue);
        p.writeString(expDate);
        p.writeString(inDate);

        p.writeString(avePrice);
        p.writeString(stockValue);



    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
