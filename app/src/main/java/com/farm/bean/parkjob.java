package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
@Table(name = "parkjob")
public class parkjob implements Parcelable // 与数据库不一致
{
    public String parkName;
    public List<areajob> arealist;


    public void setArealist(List<areajob> arealist)
    {
        this.arealist = arealist;
    }

    public List<areajob> getArealist()
    {
        return arealist;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getParkName()
    {
        return parkName;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<parkjob> CREATOR = new Creator()
    {
        @Override
        public parkjob createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            parkjob p = new parkjob();
            p.setParkName(source.readString());
            p.arealist = source.readArrayList(plantgrowthtab.class.getClassLoader());
            return p;
        }

        @Override
        public parkjob[] newArray(int size)
        {
            return new parkjob[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(parkName);
        p.writeList(arealist);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
