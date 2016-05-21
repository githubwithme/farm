package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/5/21.
 */
@Table(name = "AllType")
public class AllType implements Parcelable {

  /*  " uId ":"1",
            " Id ":"1",  //农产品Id
            “regDate”:” ”,  //注册时间
            " productName ":" ",   //农产品名
            " unit ":" ",   //单位
            " productNote ":" "    //备注*/
    public String uId;
    public String Id;
    public String regDate;
    public String productName;
    public String unit;
    public String productNote;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getProductNote() {
        return productNote;
    }

    public void setProductNote(String productNote) {
        this.productNote = productNote;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public static final Creator<AllType> CREATOR = new Creator() {
        @Override
        public AllType createFromParcel(Parcel source) {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            AllType p = new AllType();
            p.setuId(source.readString());
            p.setId(source.readString());
            p.setRegDate(source.readString());
            p.setProductName(source.readString());
            p.setUnit(source.readString());
            p.setProductNote(source.readString());

            return p;
        }

        @Override
        public AllType[] newArray(int size) {
            return new AllType[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1) {

        p.writeString(uId);
        p.writeString(Id);
        p.writeString(regDate);
        p.writeString(productName);
        p.writeString(unit);
        p.writeString(productNote);


    }

    @Override
    public int describeContents() {
        return 0;
    }
}
