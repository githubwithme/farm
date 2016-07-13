package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/7.
 */
@Table(name = "WZ_Pcxx")
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
    public String stockValue;


    public String GoodsStatistical;
    public String goodsunit;
    public String goodsTypeId;
    public String goodsTypeName;
    public String userDefTypeId;
    public String userDefTypeName;
    public String goodsInInfoId;
    public String firs;
    public String firsNum;
    public String sec;
    public String secNum;
    public String three;
    public String threeNum;
    public String Id;
    public String goodsName;

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

    public String getFirs()
    {
        return firs;
    }

    public void setFirs(String firs)
    {
        this.firs = firs;
    }

    public String getFirsNum()
    {
        return firsNum;
    }

    public void setFirsNum(String firsNum)
    {
        this.firsNum = firsNum;
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

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getStockValue()
    {
        return stockValue;
    }

    public void setStockValue(String stockValue)
    {
        this.stockValue = stockValue;
    }

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
            p.setStockValue(source.readString());




            p.setGoodsStatistical(source.readString());
            p.setGoodsunit(source.readString());
            p.setGoodsTypeId(source.readString());
            p.setGoodsTypeName(source.readString());
            p.setUserDefTypeId(source.readString());
            p.setUserDefTypeName(source.readString());
            p.setGoodsInInfoId(source.readString());
            p.setFirs(source.readString());
            p.setFirsNum(source.readString());
            p.setSec(source.readString());
            p.setSecNum(source.readString());
            p.setThree(source.readString());
            p.setThreeNum(source.readString());
            p.setId(source.readString());
            p.setGoodsName(source.readString());


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
        p.writeString(stockValue);
        p.writeString(GoodsStatistical);
        p.writeString(goodsunit);
        p.writeString(goodsTypeId);
        p.writeString(goodsTypeName);
        p.writeString(userDefTypeId);
        p.writeString(userDefTypeName);
        p.writeString(goodsInInfoId);
        p.writeString(firs);
        p.writeString(firsNum);
        p.writeString(sec);
        p.writeString(secNum);
        p.writeString(three);
        p.writeString(threeNum);
        p.writeString(Id);
        p.writeString(goodsName);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }


}
