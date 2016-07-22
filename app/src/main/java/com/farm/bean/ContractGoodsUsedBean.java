package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
@Table(name = "ContractGoodsUsedBean")

public class ContractGoodsUsedBean implements Parcelable
{
    public String contractId;//承包区id
    public String contractName;//承包区名称
    public String date;//承包区id
    public String confirmUserId;//确认者id
    public String isConfirm;  //是否确认
    public String flashStr;  //是否已读
    public List<ContractGoodsUsed> contractGoodsUsedList;//承包户具体物资使用情况

    public ContractGoodsUsedBean()
    {
        super();
    }

    protected ContractGoodsUsedBean(Parcel in)
    {
        date = in.readString();
        confirmUserId = in.readString();
        isConfirm = in.readString();
        flashStr = in.readString();
        contractGoodsUsedList = in.createTypedArrayList(ContractGoodsUsed.CREATOR);
    }

    public static final Creator<ContractGoodsUsedBean> CREATOR = new Creator<ContractGoodsUsedBean>()
    {
        @Override
        public ContractGoodsUsedBean createFromParcel(Parcel in)
        {
            return new ContractGoodsUsedBean(in);
        }

        @Override
        public ContractGoodsUsedBean[] newArray(int size)
        {
            return new ContractGoodsUsedBean[size];
        }
    };

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getConfirmUserId()
    {
        return confirmUserId;
    }

    public void setConfirmUserId(String confirmUserId)
    {
        this.confirmUserId = confirmUserId;
    }

    public String getIsConfirm()
    {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm)
    {
        this.isConfirm = isConfirm;
    }

    public String getFlashStr()
    {
        return flashStr;
    }

    public void setFlashStr(String flashStr)
    {
        this.flashStr = flashStr;
    }

    public List<ContractGoodsUsed> getContractGoodsUsedList()
    {
        return contractGoodsUsedList;
    }

    public void setContractGoodsUsedList(List<ContractGoodsUsed> contractGoodsUsedList)
    {
        this.contractGoodsUsedList = contractGoodsUsedList;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(date);
        dest.writeString(confirmUserId);
        dest.writeString(isConfirm);
        dest.writeString(flashStr);
        dest.writeTypedList(contractGoodsUsedList);
    }
}
