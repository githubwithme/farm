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
@Table(name = "BatchTime")
public class BatchTime implements Parcelable // 与数据库不一致
{
    public String id;
    public String uId;
    public String parkId;
    public String regDate;
    public String batchColor;
    public String batchTime;
    public String year;
//    public List<BreakOff_New> breakOff_newList;
    public List<BreakOff_New> breakOffList;
    public String allsaleout;
    public String allsalein;
    public String allsalefor;
    public String allnewsale;

    public void setAllsaleout(String allsaleout)
    {
        this.allsaleout = allsaleout;
    }

    public String getAllsaleout()
    {
        return allsaleout;
    }

    public void setAllsalein(String allsalein)
    {
        this.allsalein = allsalein;
    }

    public String getAllsalein()
    {
        return allsalein;
    }

    public void setAllsalefor(String allsalefor)
    {
        this.allsalefor = allsalefor;
    }

    public String getAllsalefor()
    {
        return allsalefor;
    }

    public void setAllnewsale(String allnewsale)
    {
        this.allnewsale = allnewsale;
    }

    public String getAllnewsale()
    {
        return allnewsale;
    }

    public List<BreakOff_New> getBreakOffList() {
        return breakOffList;
    }

    public void setBreakOffList(List<BreakOff_New> breakOffList) {
        this.breakOffList = breakOffList;
    }

    public void setBatchTime(String batchTime)
    {
        this.batchTime = batchTime;
    }

    public String getBatchTime()
    {
        return batchTime;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getYear()
    {
        return year;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getParkId()
    {
        return parkId;
    }

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

    public static final Creator<BatchTime> CREATOR = new Creator()
    {
        @Override
        public BatchTime createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            BatchTime p = new BatchTime();
            p.setid(source.readString());
            p.setuId(source.readString());
            p.setParkId(source.readString());
            p.setRegDate(source.readString());
            p.setBatchColor(source.readString());
            p.setBatchTime(source.readString());
            p.setYear(source.readString());
//            p.breakOff_newList = source.readArrayList(plantgrowthtab.class.getClassLoader());
            p.breakOffList = source.readArrayList(BreakOff_New.class.getClassLoader());
            p.setAllsaleout(source.readString());
            p.setAllsalein(source.readString());
            p.setAllsalefor(source.readString());
            p.setAllnewsale(source.readString());
            return p;
        }

        @Override
        public BatchTime[] newArray(int size)
        {
            return new BatchTime[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uId);
        p.writeString(parkId);
        p.writeString(regDate);
        p.writeString(batchColor);
        p.writeString(batchTime);
        p.writeString(year);
//        p.writeList(breakOff_newList);
        p.writeList(breakOffList);
        p.writeString(allsaleout);
        p.writeString(allsalein);
        p.writeString(allsalefor);
        p.writeString(allnewsale);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
