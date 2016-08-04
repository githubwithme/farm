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
    public List<BreakOff_New> breakOffList;
    public List<contractTab> contracttabList;
    public List<areatab> areatabList;
    public List<parktab> parklist;
    public String allsaleout;
    public String allsalein;
    public String allsalefor;
    public String allnewsale;
    public String allnumber;
    public String flashStr;  //c


    public BatchTime()
    {
        super();
    }

    protected BatchTime(Parcel in)
    {
        id = in.readString();
        uId = in.readString();
        parkId = in.readString();
        regDate = in.readString();
        batchColor = in.readString();
        batchTime = in.readString();
        year = in.readString();
        breakOffList = in.createTypedArrayList(BreakOff_New.CREATOR);
        contracttabList = in.createTypedArrayList(contractTab.CREATOR);
        areatabList = in.createTypedArrayList(areatab.CREATOR);
        parklist = in.createTypedArrayList(parktab.CREATOR);
        allsaleout = in.readString();
        allsalein = in.readString();
        allsalefor = in.readString();
        allnewsale = in.readString();
        allnumber = in.readString();
        flashStr = in.readString();
    }

    public static final Creator<BatchTime> CREATOR = new Creator<BatchTime>()
    {
        @Override
        public BatchTime createFromParcel(Parcel in)
        {
            return new BatchTime(in);
        }

        @Override
        public BatchTime[] newArray(int size)
        {
            return new BatchTime[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(id);
        parcel.writeString(uId);
        parcel.writeString(parkId);
        parcel.writeString(regDate);
        parcel.writeString(batchColor);
        parcel.writeString(batchTime);
        parcel.writeString(year);
        parcel.writeTypedList(breakOffList);
        parcel.writeTypedList(contracttabList);
        parcel.writeTypedList(areatabList);
        parcel.writeTypedList(parklist);
        parcel.writeString(allsaleout);
        parcel.writeString(allsalein);
        parcel.writeString(allsalefor);
        parcel.writeString(allnewsale);
        parcel.writeString(allnumber);
        parcel.writeString(flashStr);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public String getParkId()
    {
        return parkId;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getBatchColor()
    {
        return batchColor;
    }

    public void setBatchColor(String batchColor)
    {
        this.batchColor = batchColor;
    }

    public String getBatchTime()
    {
        return batchTime;
    }

    public void setBatchTime(String batchTime)
    {
        this.batchTime = batchTime;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public List<BreakOff_New> getBreakOffList()
    {
        return breakOffList;
    }

    public void setBreakOffList(List<BreakOff_New> breakOffList)
    {
        this.breakOffList = breakOffList;
    }

    public List<contractTab> getContracttabList()
    {
        return contracttabList;
    }

    public void setContracttabList(List<contractTab> contracttabList)
    {
        this.contracttabList = contracttabList;
    }

    public List<areatab> getAreatabList()
    {
        return areatabList;
    }

    public void setAreatabList(List<areatab> areatabList)
    {
        this.areatabList = areatabList;
    }

    public List<parktab> getParklist()
    {
        return parklist;
    }

    public void setParklist(List<parktab> parklist)
    {
        this.parklist = parklist;
    }

    public String getAllsaleout()
    {
        return allsaleout;
    }

    public void setAllsaleout(String allsaleout)
    {
        this.allsaleout = allsaleout;
    }

    public String getAllsalein()
    {
        return allsalein;
    }

    public void setAllsalein(String allsalein)
    {
        this.allsalein = allsalein;
    }

    public String getAllsalefor()
    {
        return allsalefor;
    }

    public void setAllsalefor(String allsalefor)
    {
        this.allsalefor = allsalefor;
    }

    public String getAllnewsale()
    {
        return allnewsale;
    }

    public void setAllnewsale(String allnewsale)
    {
        this.allnewsale = allnewsale;
    }

    public String getAllnumber()
    {
        return allnumber;
    }

    public void setAllnumber(String allnumber)
    {
        this.allnumber = allnumber;
    }

    public String getFlashStr()
    {
        return flashStr;
    }

    public void setFlashStr(String flashStr)
    {
        this.flashStr = flashStr;
    }
}
