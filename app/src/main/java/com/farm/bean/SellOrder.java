package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Description: areatab 实体类</p>
 * <p>
 * Copyright: Copyright (c) 2015
 * <p>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "SellOrder")
public class SellOrder implements Parcelable // 与数据库不一致
{
    public String id;
    public String uid;
    @Id
    @NoAutoIncrement
    public String uuid;
    public String batchTime;
    public String selltype;
    public String status;
    public String buyers;
    public String email;
    public String phone;
    public String price;
    public String number;
    public String weight;
    public String allvalues;
    public String actualweight;
    public String actualnumber;
    public String actualallvalues;
    public String reg;
    public String saletime;
    public List<SellOrderDetail> sellOrderDetailList;


    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setBatchTime(String batchTime)
    {
        this.batchTime = batchTime;
    }

    public String getBatchTime()
    {
        return batchTime;
    }

    public void setSellOrderDetailList(List<SellOrderDetail> sellOrderDetailList)
    {
        this.sellOrderDetailList = sellOrderDetailList;
    }

    public List<SellOrderDetail> getSellOrderDetailList()
    {
        return sellOrderDetailList;
    }

    public void setSaletime(String saletime)
    {
        this.saletime = saletime;
    }

    public String getSaletime()
    {
        return saletime;
    }

    public void setReg(String reg)
    {
        this.reg = reg;
    }

    public String getReg()
    {
        return reg;
    }

    public void setActualallvalues(String actualallvalues)
    {
        this.actualallvalues = actualallvalues;
    }

    public String getActualallvalues()
    {
        return actualallvalues;
    }

    public void setActualnumber(String actualnumber)
    {
        this.actualnumber = actualnumber;
    }

    public String getActualnumber()
    {
        return actualnumber;
    }

    public void setActualweight(String actualweight)
    {
        this.actualweight = actualweight;
    }

    public String getActualweight()
    {
        return actualweight;
    }

    public void setAllvalues(String allvalues)
    {
        this.allvalues = allvalues;
    }

    public String getAllvalues()
    {
        return allvalues;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getNumber()
    {
        return number;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPrice()
    {
        return price;
    }

    public void setBuyers(String buyers)
    {
        this.buyers = buyers;
    }

    public String getBuyers()
    {
        return buyers;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setSelltype(String selltype)
    {
        this.selltype = selltype;
    }

    public String getSelltype()
    {
        return selltype;
    }



    public String getid()
    {
        return id;
    }

    public void setid(String id)
    {
        this.id = id;
    }






    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<SellOrder> CREATOR = new Creator()
    {
        @Override
        public SellOrder createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            SellOrder p = new SellOrder();
            p.setid(source.readString());
            p.setUid(source.readString());
            p.setUuid(source.readString());
            p.setBatchTime(source.readString());
            p.setSelltype(source.readString());
            p.setStatus(source.readString());
            p.setBuyers(source.readString());
            p.setEmail(source.readString());
            p.setPhone(source.readString());
            p.setPrice(source.readString());
            p.setNumber(source.readString());
            p.setWeight(source.readString());
            p.setAllvalues(source.readString());
            p.setActualweight(source.readString());
            p.setActualnumber(source.readString());
            p.setActualallvalues(source.readString());
            p.setReg(source.readString());
            p.setSaletime(source.readString());
            p.sellOrderDetailList = source.readArrayList(sellOrderDetailTab.class.getClassLoader());
            return p;


        }

        @Override
        public SellOrder[] newArray(int size)
        {
            return new SellOrder[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uid);
        p.writeString(uuid);
        p.writeString(batchTime);
        p.writeString(selltype);
        p.writeString(status);
        p.writeString(buyers);
        p.writeString(email);
        p.writeString(phone);
        p.writeString(price);
        p.writeString(number);
        p.writeString(weight);
        p.writeString(allvalues);
        p.writeString(actualweight);
        p.writeString(actualnumber);
        p.writeString(actualallvalues);
        p.writeString(reg);
        p.writeString(saletime);
        p.writeList(sellOrderDetailList);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
