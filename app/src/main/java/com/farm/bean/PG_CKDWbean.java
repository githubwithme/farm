package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/5/23.
 */
@Table(name = "PG_CKDWbean")
public class PG_CKDWbean implements Parcelable{

/*    "firsNum": "10",
            "secNum": "10",
            "threeNum": "10",
            "goodsId": "88",
            "storehouseId": "9",
            "firs": "件",
            "sec": "盒",
            "three": "包",
            "goodsInInfoId": "10"*/
    @Id
    @NoAutoIncrement
    public String firs="";
    public String firsNum="";
    public String sec="";
    public String secNum="";
    public String three="";
    public String threeNum="";
    public String goodsId="";
    public String storehouseId="";
    public String goodsInInfoId="";
    public String batchname="";


    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    public String getFirs() {
        return firs;
    }

    public void setFirs(String firs) {
        this.firs = firs;
    }

    public String getFirsNum() {
        return firsNum;
    }

    public void setFirsNum(String firsNum) {
        this.firsNum = firsNum;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsInInfoId() {
        return goodsInInfoId;
    }

    public void setGoodsInInfoId(String goodsInInfoId) {
        this.goodsInInfoId = goodsInInfoId;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getSecNum() {
        return secNum;
    }

    public void setSecNum(String secNum) {
        this.secNum = secNum;
    }

    public String getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(String storehouseId) {
        this.storehouseId = storehouseId;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getThreeNum() {
        return threeNum;
    }

    public void setThreeNum(String threeNum) {
        this.threeNum = threeNum;
    }

    public static final Parcelable.Creator<PG_CKDWbean> CREATOR = new Parcelable.Creator()
    {
        @Override
        public PG_CKDWbean createFromParcel(Parcel source)
        {
            PG_CKDWbean p = new PG_CKDWbean();
            p.setFirs(source.readString());
            p.setFirsNum(source.readString());
            p.setSec(source.readString());
            p.setSecNum(source.readString());
            p.setThree(source.readString());
            p.setThreeNum(source.readString());
            p.setGoodsId(source.readString());
            p.setGoodsInInfoId(source.readString());
            p.setStorehouseId(source.readString());
            p.setBatchname(source.readString());
            return p;


        }

        @Override
        public PG_CKDWbean[] newArray(int size)
        {
            return new PG_CKDWbean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(firs);
        p.writeString(firsNum);
        p.writeString(sec);
        p.writeString(secNum);
        p.writeString(three);
        p.writeString(goodsId);
        p.writeString(goodsInInfoId);
        p.writeString(storehouseId);
        p.writeString(batchname);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
