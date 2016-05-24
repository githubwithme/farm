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
    public String id;
    public String uId;
    public String userId;
    public String parkId;
    public String confirmUserId;
    public String inDate;
    public String batchNumber;
    public String batchName;
    public String loadingFee;
    public String shippingFee;
    public String inGoodsValue;
    public String outGoodsValue;
    public String regDate;
    public String inType;  //c
    public String isConfirm;  //c
    public List<WZ_RKxx> wzcrkxx;
    public List<PG_WZckbean> breakOffList;

    public List<PG_WZckbean> getBreakOffList() {
        return breakOffList;
    }

    public void setBreakOffList(List<PG_WZckbean> breakOffList) {
        this.breakOffList = breakOffList;
    }

    public String getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(String confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getOutGoodsValue() {
        return outGoodsValue;
    }

    public void setOutGoodsValue(String outGoodsValue) {
        this.outGoodsValue = outGoodsValue;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setUserId(source.readString());
            p.setParkId(source.readString());
            p.setConfirmUserId(source.readString());
            p.setInDate(source.readString());
            p.setBatchNumber(source.readString());
            p.setBatchName(source.readString());
            p.setLoadingFee(source.readString());
            p.setShippingFee(source.readString());
            p.setInGoodsValue(source.readString());
            p.setOutGoodsValue(source.readString());
            p.setRegDate(source.readString());
            p.setInType(source.readString());
            p.setIsConfirm(source.readString());
            p.wzcrkxx = source.readArrayList(plantgrowthtab.class.getClassLoader());
            p.breakOffList = source.readArrayList(plantgrowthtab.class.getClassLoader());
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
        p.writeString(id);
        p.writeString(uId);
        p.writeString(parkId);
        p.writeString(confirmUserId);
        p.writeString(inDate);
        p.writeString(batchNumber);
        p.writeString(batchName);
        p.writeString(loadingFee);
        p.writeString(shippingFee);
        p.writeString(inGoodsValue);
        p.writeString(outGoodsValue);
        p.writeString(regDate);
        p.writeString(inType);
        p.writeString(isConfirm);
        p.writeList(wzcrkxx);
        p.writeList(breakOffList);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
