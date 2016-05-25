package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Description: areatab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "SellOrder_New")
public class SpecialCostBean implements Parcelable // 与数据库不一致
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
    public String address;
    public String email;
    public String phone;
    public String price;
    public String number;
    public String weight;
    public String sumvalues;
    public String actualprice = "";
    public String actualweight = "";
    public String actualnumber = "";
    public String actualsumvalues = "";
    public String reg;
    public String saletime;
    public String year;
    public String note;
    public String feedbacknote;
    public String deposit;
    public String xxzt;
    public String producer;
    public String finalpayment;
    public List<SellOrderDetail_New> sellOrderDetailList;


    public void setFinalpayment(String finalpayment)
    {
        this.finalpayment = finalpayment;
    }

    public String getFinalpayment()
    {
        return finalpayment;
    }

    public void setProducer(String producer)
    {
        this.producer = producer;
    }

    public String getProducer()
    {
        return producer;
    }

    public void setXxzt(String xxzt)
    {
        this.xxzt = xxzt;
    }

    public String getXxzt()
    {
        return xxzt;
    }

    public void setFeedbacknote(String feedbacknote)
    {
        this.feedbacknote = feedbacknote;
    }

    public String getFeedbacknote()
    {
        return feedbacknote;
    }

    public void setActualprice(String actualprice)
    {
        this.actualprice = actualprice;
    }

    public String getActualprice()
    {
        return actualprice;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setDeposit(String deposit)
    {
        this.deposit = deposit;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getYear()
    {
        return year;
    }

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

    public void setSellOrderDetailList(List<SellOrderDetail_New> sellOrderDetailList)
    {
        this.sellOrderDetailList = sellOrderDetailList;
    }

    public List<SellOrderDetail_New> getSellOrderDetailList()
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

    public void setActualsumvalues(String actualsumvalues)
    {
        this.actualsumvalues = actualsumvalues;
    }

    public String getActualsumvalues()
    {
        return actualsumvalues;
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


    public void setSumvalues(String sumvalues)
    {
        this.sumvalues = sumvalues;
    }

    public String getSumvalues()
    {
        return sumvalues;
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

    public static final Creator<SpecialCostBean> CREATOR = new Creator()
    {
        @Override
        public SpecialCostBean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            SpecialCostBean p = new SpecialCostBean();
            p.setid(source.readString());
            p.setUid(source.readString());
            p.setUuid(source.readString());
            p.setBatchTime(source.readString());
            p.setSelltype(source.readString());
            p.setStatus(source.readString());
            p.setBuyers(source.readString());
            p.setAddress(source.readString());
            p.setEmail(source.readString());
            p.setPhone(source.readString());
            p.setPrice(source.readString());
            p.setNumber(source.readString());
            p.setWeight(source.readString());
            p.setSumvalues(source.readString());
            p.setActualprice(source.readString());
            p.setActualweight(source.readString());
            p.setActualnumber(source.readString());
            p.setActualsumvalues(source.readString());
            p.setReg(source.readString());
            p.setSaletime(source.readString());
            p.setYear(source.readString());
            p.setNote(source.readString());
            p.setFeedbacknote(source.readString());
            p.setDeposit(source.readString());
            p.setXxzt(source.readString());
            p.setProducer(source.readString());
            p.setFinalpayment(source.readString());
            p.sellOrderDetailList = source.readArrayList(sellOrderDetailTab.class.getClassLoader());
            return p;


        }

        @Override
        public SpecialCostBean[] newArray(int size)
        {
            return new SpecialCostBean[size];
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
        p.writeString(address);
        p.writeString(email);
        p.writeString(phone);
        p.writeString(price);
        p.writeString(number);
        p.writeString(weight);
        p.writeString(sumvalues);
        p.writeString(actualprice);
        p.writeString(actualweight);
        p.writeString(actualnumber);
        p.writeString(actualsumvalues);
        p.writeString(reg);
        p.writeString(saletime);
        p.writeString(year);
        p.writeString(note);
        p.writeString(feedbacknote);
        p.writeString(deposit);
        p.writeString(xxzt);
        p.writeString(producer);
        p.writeString(finalpayment);
        p.writeList(sellOrderDetailList);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
