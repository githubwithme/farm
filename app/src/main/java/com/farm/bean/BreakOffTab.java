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
@Table(name = "BreakOffTab")
public class BreakOffTab implements Parcelable // 与数据库不一致
{
    public String id;
    public String contractId;
    public String contractNum;
    public String uId;
    public String regDate;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String note;
    public String isStart;
    public String isEnd;
    public String numberofbreakoff;
    public String dateofbreakoff;
    public String output;
    public String productbatchid;

    public String getproductbatchid()
    {
        return productbatchid;
    }

    public void setproductbatchid(String productbatchid)
    {
        this.productbatchid = productbatchid;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getContractId()
    {
        return contractId;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public String getOutput()
    {
        return output;
    }

    public void setContractNum(String contractNum)
    {
        this.contractNum = contractNum;
    }

    public String getContractNum()
    {
        return contractNum;
    }

    public void setDateofbreakoff(String dateofbreakoff)
    {
        this.dateofbreakoff = dateofbreakoff;
    }

    public String getDateofbreakoff()
    {
        return dateofbreakoff;
    }

    public void setNumberofbreakoff(String numberofbreakoff)
    {
        this.numberofbreakoff = numberofbreakoff;
    }

    public String getNumberofbreakoff()
    {
        return numberofbreakoff;
    }

    public void setIsStart(String isStart)
    {
        this.isStart = isStart;
    }

    public String getIsStart()
    {
        return isStart;
    }

    public void setIsEnd(String isEnd)
    {
        this.isEnd = isEnd;
    }

    public String getIsEnd()
    {
        return isEnd;
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

    public static final Creator<BreakOffTab> CREATOR = new Creator()
    {
        @Override
        public BreakOffTab createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            BreakOffTab p = new BreakOffTab();
            p.setid(source.readString());
            p.setContractId(source.readString());
            p.setContractNum(source.readString());
            p.setuId(source.readString());
            p.setregDate(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setAreaId(source.readString());
            p.setareaName(source.readString());
            p.setNote(source.readString());
            p.setIsStart(source.readString());
            p.setIsEnd(source.readString());
            p.setNumberofbreakoff(source.readString());
            p.setDateofbreakoff(source.readString());
            p.setOutput(source.readString());
            p.setproductbatchid(source.readString());
            return p;
        }

        @Override
        public BreakOffTab[] newArray(int size)
        {
            return new BreakOffTab[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(contractId);
        p.writeString(contractNum);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(note);
        p.writeString(isStart);
        p.writeString(isEnd);
        p.writeString(numberofbreakoff);
        p.writeString(dateofbreakoff);
        p.writeString(output);
        p.writeString(productbatchid);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
