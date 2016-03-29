package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "BatchOfProduct")
public class BatchOfProduct implements Parcelable // 与数据库不一致
{
    public String id;
    public String uId;
    public String regDate;
    @Id
    @NoAutoIncrement
    public String batchTime;
    public String number;
    public String weight;
    public String sellnumber;
    public String status;


    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setSellnumber(String sellnumber)
    {
        this.sellnumber = sellnumber;
    }

    public String getSellnumber()
    {
        return sellnumber;
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


    public void setBatchTime(String batchTime)
    {
        this.batchTime = batchTime;
    }

    public String getBatchTime()
    {
        return batchTime;
    }

    public String getid()
    {
        return id;
    }

    public void setid(String id)
    {
        this.id = id;
    }

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }




    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<BatchOfProduct> CREATOR = new Creator()
    {
        @Override
        public BatchOfProduct createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            BatchOfProduct p = new BatchOfProduct();
            p.setid(source.readString());
            p.setuId(source.readString());
            p.setRegDate(source.readString());
            p.setBatchTime(source.readString());
            p.setNumber(source.readString());
            p.setWeight(source.readString());
            p.setSellnumber(source.readString());
            p.setStatus(source.readString());

            return p;
        }

        @Override
        public BatchOfProduct[] newArray(int size)
        {
            return new BatchOfProduct[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(batchTime);
        p.writeString(number);
        p.writeString(weight);
        p.writeString(sellnumber);
        p.writeString(status);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
