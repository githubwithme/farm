package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/22.
 */
@Table(name = "ContractAssess")
public class ContractAssess implements Parcelable
{
    public String commandId;//指令id
    public String uuid;///考核记录唯一标识
    public String uId;//农场id
    public String parkId;//园区id
    public String parkName;//园区id
    public String areaId;//片区id
    public String areaName;//片区id
    public String contractId;//承包区id
    public String contractName;//承包区id
    public String registerId;//登记者id
    public String registerName;//登记者姓名
    public String assessDate;//考核时间
    public String assessType;//考核类型（0警告，1不合格，2合格）
    public List<String> urls;//图片集

    public ContractAssess()
    {
        super();
    }

    protected ContractAssess(Parcel in)
    {
        commandId = in.readString();
        uuid = in.readString();
        uId = in.readString();
        parkId = in.readString();
        parkName = in.readString();
        areaId = in.readString();
        areaName = in.readString();
        contractId = in.readString();
        contractName = in.readString();
        registerId = in.readString();
        registerName = in.readString();
        assessDate = in.readString();
        assessType = in.readString();
        urls = in.createStringArrayList();
    }

    public static final Creator<ContractAssess> CREATOR = new Creator<ContractAssess>()
    {
        @Override
        public ContractAssess createFromParcel(Parcel in)
        {
            return new ContractAssess(in);
        }

        @Override
        public ContractAssess[] newArray(int size)
        {
            return new ContractAssess[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(commandId);
        dest.writeString(uuid);
        dest.writeString(uId);
        dest.writeString(parkId);
        dest.writeString(parkName);
        dest.writeString(areaId);
        dest.writeString(areaName);
        dest.writeString(contractId);
        dest.writeString(contractName);
        dest.writeString(registerId);
        dest.writeString(registerName);
        dest.writeString(assessDate);
        dest.writeString(assessType);
        dest.writeStringList(urls);
    }

    public String getCommandId()
    {
        return commandId;
    }

    public void setCommandId(String commandId)
    {
        this.commandId = commandId;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
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

    public String getParkName()
    {
        return parkName;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getContractId()
    {
        return contractId;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getContractName()
    {
        return contractName;
    }

    public void setContractName(String contractName)
    {
        this.contractName = contractName;
    }

    public String getRegisterId()
    {
        return registerId;
    }

    public void setRegisterId(String registerId)
    {
        this.registerId = registerId;
    }

    public String getRegisterName()
    {
        return registerName;
    }

    public void setRegisterName(String registerName)
    {
        this.registerName = registerName;
    }

    public String getAssessDate()
    {
        return assessDate;
    }

    public void setAssessDate(String assessDate)
    {
        this.assessDate = assessDate;
    }

    public String getAssessType()
    {
        return assessType;
    }

    public void setAssessType(String assessType)
    {
        this.assessType = assessType;
    }

    public List<String> getUrls()
    {
        return urls;
    }

    public void setUrls(List<String> urls)
    {
        this.urls = urls;
    }
}
