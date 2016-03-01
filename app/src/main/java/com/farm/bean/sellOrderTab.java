package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p>
 * Copyright: Copyright (c) 2015
 * <p>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "sellOrderTab")
public class sellOrderTab implements Parcelable // 与数据库不一致
{
    public String id;
    public String SN;
    public String sellType;
    public String status;
    public String sellAmount;
    public String dataofOrder;


    public void setDataofOrder(String dataofOrder)
    {
        this.dataofOrder = dataofOrder;
    }

    public String getDataofOrder()
    {
        return dataofOrder;
    }

    public void setSellAmount(String sellAmount)
    {
        this.sellAmount = sellAmount;
    }

    public String getSellAmount()
    {
        return sellAmount;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setSellType(String sellType)
    {
        this.sellType = sellType;
    }

    public String getSellType()
    {
        return sellType;
    }

    public void setSN(String SN)
    {
        this.SN = SN;
    }

    public String getSN()
    {
        return SN;
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

    public static final Creator<sellOrderTab> CREATOR = new Creator()
    {
        @Override
        public sellOrderTab createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            sellOrderTab p = new sellOrderTab();
            p.setid(source.readString());
            p.setSN(source.readString());
            p.setSellType(source.readString());
            p.setStatus(source.readString());
            p.setSellAmount(source.readString());
            p.setDataofOrder(source.readString());

            return p;
        }

        @Override
        public sellOrderTab[] newArray(int size)
        {
            return new sellOrderTab[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(SN);
        p.writeString(sellType);
        p.writeString(status);
        p.writeString(sellAmount);
        p.writeString(dataofOrder);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}