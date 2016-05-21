package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/5/21.
 */
@Table(name = "PG_CKBean")
public class PG_CKBean implements Parcelable {
    @Id
    @NoAutoIncrement
    public String id="";
    public String uid="";
    public String wzFirst="";
    public String wzSecond="";
    public String goodsId="";
    public String storehouseId ="";
    public String goodsName="";
    public String goodsSum="";
    public String batchNumber="";
    public String goodsDw="";


    public String getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(String storehouseId) {
        this.storehouseId = storehouseId;
    }

    public String getGoodsDw() {
        return goodsDw;
    }

    public void setGoodsDw(String goodsDw) {
        this.goodsDw = goodsDw;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
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

    public String getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(String goodsSum) {
        this.goodsSum = goodsSum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWzFirst() {
        return wzFirst;
    }

    public void setWzFirst(String wzFirst) {
        this.wzFirst = wzFirst;
    }

    public String getWzSecond() {
        return wzSecond;
    }

    public void setWzSecond(String wzSecond) {
        this.wzSecond = wzSecond;
    }

    public static final Creator<PG_CKBean> CREATOR = new Creator()
    {
        @Override
        public PG_CKBean createFromParcel(Parcel source)
        {
            PG_CKBean p = new PG_CKBean();
            p.setId(source.readString());
            p.setUid(source.readString());
            p.setWzFirst(source.readString());
            p.setWzSecond(source.readString());
            p.setGoodsId(source.readString());
            p.setStorehouseId(source.readString());
            p.setGoodsName(source.readString());
            p.setGoodsSum(source.readString());
            p.setBatchNumber(source.readString());
            p.setGoodsDw(source.readString());


            return p;


        }

        @Override
        public PG_CKBean[] newArray(int size)
        {
            return new PG_CKBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uid);
        p.writeString(wzFirst);
        p.writeString(wzSecond);
        p.writeString(goodsId);
        p.writeString(storehouseId);
        p.writeString(goodsName);
        p.writeString(goodsSum);
        p.writeString(batchNumber);
        p.writeString(goodsDw);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
