package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/8.
 */
@Table(name = "WZ_YCxx")
public class WZ_YCxx implements Parcelable
{


    public String id;
    public String parkId;
    public String parkName;
    public String storehouseId ;
    public String storehouseName;
    public String goodsId;
    public String goodsName;
    public String nowDate;
    public String type;
    public String expDate;
    public String expDate1;
    public String batchNumber;
    public String batchName;
    public String expQuantity;
    public String quantity;
    public String flashStr;
    public String daysBeforeWarning;  //警戒天数
    public String levelOfWarning;   //警戒数量
    public String firsNum;   //
    public String firs;   //
    public String sec;   //
    public String secNum;   //
    public String three;   //
    public String threeNum;   //




    public String getDaysBeforeWarning()
    {
        return daysBeforeWarning;
    }

    public void setDaysBeforeWarning(String daysBeforeWarning)
    {
        this.daysBeforeWarning = daysBeforeWarning;
    }

    public String getLevelOfWarning()
    {
        return levelOfWarning;
    }

    public void setLevelOfWarning(String levelOfWarning)
    {
        this.levelOfWarning = levelOfWarning;
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

    public String getExpDate1()
    {
        return expDate1;
    }

    public void setExpDate1(String expDate1)
    {
        this.expDate1 = expDate1;
    }

    public String getFlashStr() {
        return flashStr;
    }

    public void setFlashStr(String flashStr) {
        this.flashStr = flashStr;
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

    public String getExpQuantity() {
        return expQuantity;
    }

    public void setExpQuantity(String expQuantity) {
        this.expQuantity = expQuantity;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public static final Creator<WZ_YCxx> CREATOR = new Creator()
    {
        @Override
        public WZ_YCxx createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            WZ_YCxx p = new WZ_YCxx();
            p.setId(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setStorehouseId(source.readString());
            p.setStorehouseName(source.readString());
            p.setGoodsId(source.readString());
            p.setGoodsName(source.readString());
            p.setNowDate(source.readString());
            p.setType(source.readString());
            p.setExpDate(source.readString());
            p.setExpDate1(source.readString());

            p.setBatchNumber(source.readString());//仓库物资
            p.setBatchName(source.readString());
            p.setExpQuantity(source.readString());

            p.setQuantity(source.readString());
            p.setFlashStr(source.readString());


            p.setDaysBeforeWarning(source.readString());
            p.setLevelOfWarning(source.readString());
            p.setFirsNum(source.readString());
            p.setFirs(source.readString());
            p.setSec(source.readString());
            p.setSecNum(source.readString());
            p.setThree(source.readString());
            p.setThreeNum(source.readString());
            return  p;
        }

        @Override
        public WZ_YCxx[] newArray(int size)
        {
            return new WZ_YCxx[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {

        p.writeString(id);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(storehouseId);
        p.writeString(storehouseName);
        p.writeString(goodsId);
        p.writeString(goodsName);
        p.writeString(nowDate);
        p.writeString(type);
        p.writeString(expDate);
        p.writeString(expDate1);
        p.writeString(batchNumber);
        p.writeString(batchName);
        p.writeString(expQuantity);
        p.writeString(quantity);
        p.writeString(flashStr);
        p.writeString(daysBeforeWarning);
        p.writeString(levelOfWarning);
        p.writeString(firsNum);
        p.writeString(firs);
        p.writeString(sec);
        p.writeString(secNum);
        p.writeString(three);
        p.writeString(threeNum);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
