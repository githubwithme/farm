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
/*    id
            uid
    goodsInfoId        总表id
    goodsInInfoRKId    出库哪个批次id
    goodsTypeId     一级id
    userDefTypeId   二级id
    goodsId       物资id
    storehouseId   仓库id
    firs      一级单位
    firsNum   一级数量
    sec       二级单位
    secNum    二级数量
    three     三级单位
    threeNum  三级数量
    goodsOutNote   备注
    isConfirm   是否确认*/
    public String uid="";
    public String goodsInInfoRKId="";
    public String goodsTypeId="";
    public String userDefTypeId="";
    public String goodsId="";
    public String storehouseId="";
    public String firs="";
    public String firsNum ="";
    public String sec="";
    public String secNum="";
    public String three="";
    public String goodsOutNote="";
    public String threeNum="";




    public String getGoodsInInfoRKId() {
        return goodsInInfoRKId;
    }

    public void setGoodsInInfoRKId(String goodsInInfoRKId) {
        this.goodsInInfoRKId = goodsInInfoRKId;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getGoodsOutNote() {
        return goodsOutNote;
    }

    public void setGoodsOutNote(String goodsOutNote) {
        this.goodsOutNote = goodsOutNote;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
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

    public String getUserDefTypeId() {
        return userDefTypeId;
    }

    public void setUserDefTypeId(String userDefTypeId) {
        this.userDefTypeId = userDefTypeId;
    }

    public static final Creator<PG_CKBean> CREATOR = new Creator()
    {
        @Override
        public PG_CKBean createFromParcel(Parcel source)
        {
            PG_CKBean p = new PG_CKBean();
            p.setUid(source.readString());
            p.setGoodsInInfoRKId(source.readString());
            p.setGoodsTypeId(source.readString());
            p.setUserDefTypeId(source.readString());
            p.setGoodsId(source.readString());
            p.setStorehouseId(source.readString());
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
        public PG_CKBean[] newArray(int size)
        {
            return new PG_CKBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(uid);
        p.writeString(goodsInInfoRKId);
        p.writeString(goodsTypeId);
        p.writeString(userDefTypeId);
        p.writeString(goodsId);
        p.writeString(storehouseId);
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
