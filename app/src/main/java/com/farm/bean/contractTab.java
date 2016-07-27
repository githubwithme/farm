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
@Table(name = "contractTab")
public class contractTab implements Parcelable // 与数据库不一致
{
    public String id;
    public String contractid;
    public String contractname;
    public String contractNum;
    public String uId;
    public String regDate;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String note;
    public String numOfPlant;
    public String gainnumber;
    public String contractarea;
    public String amountforsale;
    public String allsaleout;
    public String allsalein;
    public String allsalefor;
    public String allnewsale;
    public String allnumber;
    public List<breakofftab> breakofftabList;
    public List<BreakOff_New> BreakOffList;
    public List<BatchTime> batchTimeList;

    public List<BatchTime> getBatchTimeList()
    {
        return batchTimeList;
    }

    public void setBatchTimeList(List<BatchTime> batchTimeList)
    {
        this.batchTimeList = batchTimeList;
    }

    public String getContractid()
    {
        return contractid;
    }

    public void setContractid(String contractid)
    {
        this.contractid = contractid;
    }

    public String getContractname()
    {
        return contractname;
    }

    public void setContractname(String contractname)
    {
        this.contractname = contractname;
    }

    public String getAllnumber()
    {
        return allnumber;
    }

    public void setAllnumber(String allnumber)
    {
        this.allnumber = allnumber;
    }

    public List<BreakOff_New> getBreakOffList() {
        return BreakOffList;
    }

    public void setBreakOffList(List<BreakOff_New> breakOffList) {
        BreakOffList = breakOffList;
    }

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

    public void setNumOfPlant(String numOfPlant)
    {
        this.numOfPlant = numOfPlant;
    }

    public String getNumOfPlant()
    {
        return numOfPlant;
    }

    public void setAmountforsale(String amountforsale)
    {
        this.amountforsale = amountforsale;
    }

    public String getAmountforsale()
    {
        return amountforsale;
    }

    public void setbreakofftabList(List<breakofftab> breakofftabList)
    {
        this.breakofftabList = breakofftabList;
    }

    public List<breakofftab> getbreakofftabList()
    {
        return breakofftabList;
    }

    public void setContractarea(String contractarea)
    {
        this.contractarea = contractarea;
    }

    public String getContractarea()
    {
        return contractarea;
    }

    public void setGainnumber(String gainnumber)
    {
        this.gainnumber = gainnumber;
    }

    public String getGainnumber()
    {
        return gainnumber;
    }


    public void setContractNum(String contractNum)
    {
        this.contractNum = contractNum;
    }

    public String getContractNum()
    {
        return contractNum;
    }


    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
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

    public String getregDate()
    {
        return regDate;
    }

    public void setregDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getareaName()
    {
        return areaName;
    }

    public void setareaName(String areaName)
    {
        this.areaName = areaName;
    }


    public String getparkId()
    {
        return parkId;
    }

    public void setparkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getparkName()
    {
        return parkName;
    }

    public void setparkName(String parkName)
    {
        this.parkName = parkName;
    }


    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<contractTab> CREATOR = new Creator()
    {
        @Override
        public contractTab createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            contractTab p = new contractTab();
            p.setid(source.readString());
            p.setContractid(source.readString());
            p.setContractname(source.readString());
            p.setContractNum(source.readString());
            p.setuId(source.readString());
            p.setregDate(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setAreaId(source.readString());
            p.setareaName(source.readString());
            p.setNote(source.readString());
            p.setNumOfPlant(source.readString());
            p.setGainnumber(source.readString());
            p.setContractarea(source.readString());
            p.setAmountforsale(source.readString());
            p.setAllsaleout(source.readString());
            p.setAllsalein(source.readString());
            p.setAllsalefor(source.readString());
            p.setAllnewsale(source.readString());
            p.setAllnumber(source.readString());
            p.breakofftabList = source.readArrayList(breakofftab.class.getClassLoader());
            p.BreakOffList = source.readArrayList(breakofftab.class.getClassLoader());
            p.batchTimeList = source.readArrayList(breakofftab.class.getClassLoader());
            return p;
        }

        @Override
        public contractTab[] newArray(int size)
        {
            return new contractTab[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(contractid);
        p.writeString(contractname);
        p.writeString(contractNum);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(note);
        p.writeString(numOfPlant);
        p.writeString(gainnumber);
        p.writeString(contractarea);
        p.writeString(amountforsale);
        p.writeString(allsaleout);
        p.writeString(allsalein);
        p.writeString(allsalefor);
        p.writeString(allnewsale);
        p.writeString(allnumber);
        p.writeList(breakofftabList);
        p.writeList(BreakOffList);
        p.writeList(batchTimeList);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
