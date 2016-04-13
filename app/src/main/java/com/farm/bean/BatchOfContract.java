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
@Table(name = "BatchOfContract")
public class BatchOfContract implements Parcelable // 与数据库不一致
{
    public String id;
    @Id
    @NoAutoIncrement
    public String uuid;
    public String uId;
    public String regDate;
    public String batchTime;
    public String number;
    public String weight;
    public String sellnumber;
    public String status;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String contractId;
    public String contractName;
    public String isdrawer;
    public String xxzt;


    public void setXxzt(String xxzt)
    {
        this.xxzt = xxzt;
    }

    public String getXxzt()
    {
        return xxzt;
    }

    public void setIsdrawer(String isdrawer)
    {
        this.isdrawer = isdrawer;
    }

    public String getIsdrawer()
    {
        return isdrawer;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setContractName(String contractName)
    {
        this.contractName = contractName;
    }

    public String getContractName()
    {
        return contractName;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getContractId()
    {
        return contractId;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getParkName()
    {
        return parkName;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getParkId()
    {
        return parkId;
    }

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

    public static final Creator<BatchOfContract> CREATOR = new Creator()
    {
        @Override
        public BatchOfContract createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            BatchOfContract p = new BatchOfContract();
            p.setid(source.readString());
            p.setUuid(source.readString());
            p.setuId(source.readString());
            p.setRegDate(source.readString());
            p.setBatchTime(source.readString());
            p.setNumber(source.readString());
            p.setWeight(source.readString());
            p.setSellnumber(source.readString());
            p.setStatus(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setAreaId(source.readString());
            p.setAreaName(source.readString());
            p.setContractId(source.readString());
            p.setContractName(source.readString());
            p.setIsdrawer(source.readString());
            p.setXxzt(source.readString());

            return p;
        }

        @Override
        public BatchOfContract[] newArray(int size)
        {
            return new BatchOfContract[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uuid);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(batchTime);
        p.writeString(number);
        p.writeString(weight);
        p.writeString(sellnumber);
        p.writeString(status);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(contractId);
        p.writeString(contractName);
        p.writeString(isdrawer);
        p.writeString(xxzt);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
