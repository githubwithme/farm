package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/7.
 */
@Table(name = "WZ_Detail")
public class WZ_Detail implements Parcelable
{
    public String goodsId;
    public String goodsName;
    public String quantity;

    public String goodsTypeId;
    public String userDefTypeID ;
    public String daysBeforeWarning;
    public String levelOfWarning;
    public String goodsunit;
    public String goodsSpec;
    public String goodsNote;
    public String firs;
    public String sec;
    public String secNum;
    public String three;
    public String threeNum ;
    public String GoodsStatistical ;

    public String getFirs() {
        return firs;
    }

    public void setFirs(String firs) {
        this.firs = firs;
    }

    public String getDaysBeforeWarning() {
        return daysBeforeWarning;
    }

    public void setDaysBeforeWarning(String daysBeforeWarning) {
        this.daysBeforeWarning = daysBeforeWarning;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsid) {
        this.goodsId = goodsid;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsname) {
        this.goodsName = goodsname;
    }

    public String getGoodsNote() {
        return goodsNote;
    }

    public void setGoodsNote(String goodsNote) {
        this.goodsNote = goodsNote;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsStatistical() {
        return GoodsStatistical;
    }

    public void setGoodsStatistical(String goodsStatistical) {
        GoodsStatistical = goodsStatistical;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsunit() {
        return goodsunit;
    }

    public void setGoodsunit(String goodsunit) {
        this.goodsunit = goodsunit;
    }

    public String getLevelOfWarning() {
        return levelOfWarning;
    }

    public void setLevelOfWarning(String levelOfWarning) {
        this.levelOfWarning = levelOfWarning;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getUserDefTypeID() {
        return userDefTypeID;
    }

    public void setUserDefTypeID(String userDefTypeID) {
        this.userDefTypeID = userDefTypeID;
    }
    public static final Creator<WZ_Detail> CREATOR = new Creator()
    {
        @Override
        public WZ_Detail createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            WZ_Detail p = new WZ_Detail();

            p.setGoodsId(source.readString());//仓库物资
            p.setGoodsName(source.readString());
            p.setQuantity(source.readString());
            p.setGoodsTypeId(source.readString());
            p.setUserDefTypeID(source.readString());
            p.setDaysBeforeWarning(source.readString());
            p.setLevelOfWarning(source.readString());
            p.setGoodsunit(source.readString());
            p.setGoodsSpec(source.readString());
            p.setGoodsNote(source.readString());
            p.setFirs(source.readString());
            p.setSec(source.readString());
            p.setSecNum(source.readString());
            p.setThree(source.readString());
            p.setThreeNum(source.readString());
            p.setGoodsStatistical(source.readString());


            return  p;
        }

        @Override
        public WZ_Detail[] newArray(int size)
        {
            return new WZ_Detail[size];
        }
    };
    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(goodsId);
        p.writeString(goodsName);
        p.writeString(quantity);
        p.writeString(goodsTypeId);
        p.writeString(userDefTypeID);
        p.writeString(daysBeforeWarning);
        p.writeString(levelOfWarning);
        p.writeString(goodsunit);
        p.writeString(goodsSpec);
        p.writeString(goodsNote);
        p.writeString(firs);
        p.writeString(sec);
        p.writeString(secNum);
        p.writeString(three);
        p.writeString(threeNum );
        p.writeString(GoodsStatistical);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }

}
