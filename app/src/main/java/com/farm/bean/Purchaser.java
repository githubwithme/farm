package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by hasee on 2016/6/12.
 */
@Table(name = "Purchaser")
public class Purchaser implements Parcelable
{
    public String id;
    public String purchaser;
    public String phone;
    public String ems;
    public String address;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPurchaser()
    {
        return purchaser;
    }

    public void setPurchaser(String purchaser)
    {
        this.purchaser = purchaser;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEms()
    {
        return ems;
    }

    public void setEms(String ems)
    {
        this.ems = ems;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
    public static final Creator<Purchaser> CREATOR = new Creator()
    {
        @Override
        public Purchaser createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Purchaser p = new Purchaser();
            p.setId(source.readString());
            p.setPurchaser(source.readString());
            p.setPhone(source.readString());
            p.setEms(source.readString());
            p.setAddress(source.readString());

            return p;
        }

        @Override
        public Purchaser[] newArray(int size)
        {
            return new Purchaser[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(purchaser);
        p.writeString(phone);
        p.writeString(ems);
        p.writeString(address);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
