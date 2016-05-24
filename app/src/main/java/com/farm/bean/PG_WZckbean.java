package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/5/24.
 */
@Table(name = "PG_WZckbean")
public class PG_WZckbean implements Parcelable
{
/*    "id": "7",
            "uid": "60",
            "goodsInfoId": "10",
            "goodsInInfoRKId": "8",
            "goodsTypeId": "6",
            "userDefTypeId": "10",
            "goodsId": "86",
            "storehouseId": "9",
            "firs": "桶",
            "firsNum": "5",
            "sec": "",
            "secNum": "0",
            "three": "",
            "threeNum": "0",
            "goodsOutNote": "",
            "parkId": "16"*/
     public String id;
    public String uId;
    public String parkId;
    public String parkName;
    public String goodsInfoId;
    public String goodsInInfoRKId;
    public String goodsTypeId;
    public String userDefTypeId;
    public String goodsId;
    public String goodsName;
    public String storehouseId;
    public String storeName;
    public String firs;
    public String firsNum;
    public String sec;
    public String secNum;
    public String three;
    public String threeNum;
    public String goodsOutNote;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String goodsInfoId) {
        this.goodsInfoId = goodsInfoId;
    }

    public String getGoodsOutNote() {
        return goodsOutNote;
    }

    public void setGoodsOutNote(String goodsOutNote) {
        this.goodsOutNote = goodsOutNote;
    }

    public String getGoodsInInfoRKId() {
        return goodsInInfoRKId;
    }

    public void setGoodsInInfoRKId(String goodsInInfoRKId) {
        this.goodsInInfoRKId = goodsInInfoRKId;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserDefTypeId() {
        return userDefTypeId;
    }

    public void setUserDefTypeId(String userDefTypeId) {
        this.userDefTypeId = userDefTypeId;
    }

    public static final Parcelable.Creator<PG_WZckbean> CREATOR = new Creator()
    {
        @Override
        public PG_WZckbean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            PG_WZckbean p = new PG_WZckbean();
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setGoodsInfoId(source.readString());
            p.setGoodsInInfoRKId(source.readString());
            p.setGoodsTypeId(source.readString());
            p.setUserDefTypeId(source.readString());
            p.setGoodsId(source.readString());
            p.setGoodsName(source.readString());
            p.setStorehouseId(source.readString());
            p.setStoreName(source.readString());
            p.setFirs(source.readString());
            p.setFirsNum(source.readString());
            p.setSec(source.readString());
            p.setSecNum(source.readString());
            p.setThree(source.readString());
            p.setThreeNum(source.readString());
            p.setGoodsOutNote(source.readString());
            return p;
        }

        @Override
        public PG_WZckbean[] newArray(int size)
        {
            return new PG_WZckbean[size];
        }
    };
    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uId);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(goodsInfoId);
        p.writeString(goodsInInfoRKId);
        p.writeString(goodsTypeId);
        p.writeString(userDefTypeId);
        p.writeString(goodsId);
        p.writeString(goodsName);
        p.writeString(storehouseId);
        p.writeString(storeName);
        p.writeString(firs);
        p.writeString(firsNum);
        p.writeString(sec);
        p.writeString(secNum);
        p.writeString(three);
        p.writeString(threeNum);
        p.writeString(goodsOutNote);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
