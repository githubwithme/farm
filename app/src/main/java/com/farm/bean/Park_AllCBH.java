package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by hasee on 2016/6/27.
 */
@Table(name = "Park_AllCBH")
public class Park_AllCBH implements Parcelable
{
    public String id;//片区id
    public String uid;//农场id
    public String regDate;
    public String areaName;//片区名字
    public String parkId;//园区名字id
    public String parkName;//园区名字
    public List<contractTab> contractList;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getParkId()
    {
        return parkId;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getParkName()
    {
        return parkName;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public List<contractTab> getContractList()
    {
        return contractList;
    }

    public void setContractList(List<contractTab> contractList)
    {
        this.contractList = contractList;
    }


    public static final Parcelable.Creator<Park_AllCBH> CREATOR = new Creator()
    {
        @Override
        public Park_AllCBH createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Park_AllCBH p = new Park_AllCBH();
            p.setId(source.readString());
            p.setUid(source.readString());
            p.setRegDate(source.readString());
            p.setAreaName(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());

            p.contractList = source.readArrayList(plantgrowthtab.class.getClassLoader());
            return p;
        }
        @Override
        public Park_AllCBH[] newArray(int size)
        {
            return new Park_AllCBH[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uid);
        p.writeString(regDate);
        p.writeString(areaName);
        p.writeString(parkId);
        p.writeString(parkName);

        p.writeList(contractList);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
