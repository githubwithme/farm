package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
@Table(name = "BatchColor")
public class BatchColor implements Parcelable // 与数据库不一致
{
    public String id;
    public String uId;
    public String regDate;
    public String batchColor;


    public void setBatchColor(String batchColor)
    {
        this.batchColor = batchColor;
    }

    public String getBatchColor()
    {
        return batchColor;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getRegDate()
    {
        return regDate;
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

    public static final Creator<BatchColor> CREATOR = new Creator()
    {
        @Override
        public BatchColor createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            BatchColor p = new BatchColor();
            p.setid(source.readString());
            p.setuId(source.readString());
            p.setRegDate(source.readString());
            p.setBatchColor(source.readString());

            return p;
        }

        @Override
        public BatchColor[] newArray(int size)
        {
            return new BatchColor[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(batchColor);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
