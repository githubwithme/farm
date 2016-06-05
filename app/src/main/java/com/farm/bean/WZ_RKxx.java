package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/7.
 */
@Table(name = "WZ_RKxx")
public class WZ_RKxx implements Parcelable
{
    public String id;
    public String parkId;
    public String parkName;
    public String storehouseId  ;
    public String storehouseName;
    public String goodsid;
    public String goodsname;
    public String goodsInInfoRKId;//c
    public String batchName;//c

    public String quantity;
    public String price;
    public String inGoodsvalue;
    public String outGoodsvalue;//c
    public String inType;
    public String outType;//c

    public String expDate;
    public String note;
    public String sumWeight;


    public String getSumWeight()
    {
        return sumWeight;
    }

    public void setSumWeight(String sumWeight)
    {
        this.sumWeight = sumWeight;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getGoodsInInfoRKId() {
        return goodsInInfoRKId;
    }

    public void setGoodsInInfoRKId(String goodsInInfoRKId) {
        this.goodsInInfoRKId = goodsInInfoRKId;
    }

    public String getOutGoodsvalue() {
        return outGoodsvalue;
    }

    public void setOutGoodsvalue(String outGoodsvalue) {
        this.outGoodsvalue = outGoodsvalue;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInGoodsvalue() {
        return inGoodsvalue;
    }

    public void setInGoodsvalue(String inGoodsvalue) {
        this.inGoodsvalue = inGoodsvalue;
    }

    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
    public static final Creator<WZ_RKxx> CREATOR = new Creator()
    {
        @Override
        public WZ_RKxx createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            WZ_RKxx p = new WZ_RKxx();

            p.setId(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setStorehouseId(source.readString());
            p.setStorehouseName(source.readString());
            p.setGoodsid(source.readString());
            p.setGoodsname(source.readString());
            p.setGoodsInInfoRKId(source.readString());
            p.setBatchName(source.readString());
            p.setQuantity(source.readString());
            p.setPrice(source.readString());
            p.setInGoodsvalue(source.readString());
            p.setOutGoodsvalue(source.readString());
            p.setInType(source.readString());
            p.setOutType(source.readString());
            p.setExpDate(source.readString());
            p.setNote(source.readString());
            p.setSumWeight(source.readString());



            return  p;
        }

        @Override
        public WZ_RKxx[] newArray(int size)
        {
            return new WZ_RKxx[size];
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
        p.writeString(goodsid);
        p.writeString(goodsname);
        p.writeString(goodsInInfoRKId);
        p.writeString(batchName);
        p.writeString(quantity);
        p.writeString(price);
        p.writeString(inGoodsvalue);
        p.writeString(outGoodsvalue);
        p.writeString(inType);
        p.writeString(outType);
        p.writeString(expDate);
        p.writeString(note);
        p.writeString(sumWeight);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }

}
