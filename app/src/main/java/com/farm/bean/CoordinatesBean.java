package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *地理坐标信息表:记录销售区域
 * @version 1.0
 */
@Table(name = "CoordinatesBean")
public class CoordinatesBean implements Parcelable // 与数据库不一致
{

    @Id
    public int id;
    public String uuid;
    public String type;
    public String uid;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String contractid;
    public String contractname;
    public String batchid;
    public String saleid;
    public String lat;
    public String lng;
    public String numofplant;
    public String weightofplant;


    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setWeightofplant(String weightofplant)
    {
        this.weightofplant = weightofplant;
    }

    public String getWeightofplant()
    {
        return weightofplant;
    }

    public void setNumofplant(String numofplant)
    {
        this.numofplant = numofplant;
    }

    public String getNumofplant()
    {
        return numofplant;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public void setSaleid(String saleid)
    {
        this.saleid = saleid;
    }

    public String getSaleid()
    {
        return saleid;
    }

    public void setBatchid(String batchid)
    {
        this.batchid = batchid;
    }

    public String getBatchid()
    {
        return batchid;
    }

    public void setContractname(String contractname)
    {
        this.contractname = contractname;
    }

    public String getContractname()
    {
        return contractname;
    }

    public void setContractid(String contractid)
    {
        this.contractid = contractid;
    }

    public String getContractid()
    {
        return contractid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
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

    public static final Creator<CoordinatesBean> CREATOR = new Creator()
    {
        @Override
        public CoordinatesBean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            CoordinatesBean p = new CoordinatesBean();
            p.setId(source.readInt());
            p.setUuid(source.readString());
            p.setType(source.readString());
            p.setUid(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setparkName(source.readString());
            p.setAreaId(source.readString());
            p.setareaName(source.readString());
            p.setContractid(source.readString());
            p.setContractname(source.readString());
            p.setBatchid(source.readString());
            p.setSaleid(source.readString());
            p.setLat(source.readString());
            p.setLng(source.readString());
            p.setNumofplant(source.readString());
            p.setWeightofplant(source.readString());
            return p;
        }

        @Override
        public CoordinatesBean[] newArray(int size)
        {
            return new CoordinatesBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeInt(id);
        p.writeString(uuid);
        p.writeString(type);
        p.writeString(uid);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(contractid);
        p.writeString(contractname);
        p.writeString(batchid);
        p.writeString(saleid);
        p.writeString(lat);
        p.writeString(lng);
        p.writeString(numofplant);
        p.writeString(weightofplant);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
