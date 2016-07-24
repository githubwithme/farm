package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${hmj} on 2016/7/23.
 */
public class ContractCommandProgressBean implements Parcelable
{
    String uuid;//唯一标识
    String commandId;//指令id
    String contractId;//承包区id
    String contractName;//承包区名称
    String progress;//进度
    String recordTime;//记录时间

    public ContractCommandProgressBean()
    {
        super();
    }

    protected ContractCommandProgressBean(Parcel in)
    {
        uuid = in.readString();
        commandId = in.readString();
        contractId = in.readString();
        contractName = in.readString();
        progress = in.readString();
        recordTime = in.readString();
    }

    public static final Creator<ContractCommandProgressBean> CREATOR = new Creator<ContractCommandProgressBean>()
    {
        @Override
        public ContractCommandProgressBean createFromParcel(Parcel in)
        {
            return new ContractCommandProgressBean(in);
        }

        @Override
        public ContractCommandProgressBean[] newArray(int size)
        {
            return new ContractCommandProgressBean[size];
        }
    };

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getCommandId()
    {
        return commandId;
    }

    public void setCommandId(String commandId)
    {
        this.commandId = commandId;
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

    public String getProgress()
    {
        return progress;
    }

    public void setProgress(String progress)
    {
        this.progress = progress;
    }

    public String getRecordTime()
    {
        return recordTime;
    }

    public void setRecordTime(String recordTime)
    {
        this.recordTime = recordTime;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uuid);
        dest.writeString(commandId);
        dest.writeString(contractId);
        dest.writeString(contractName);
        dest.writeString(progress);
        dest.writeString(recordTime);
    }
}
