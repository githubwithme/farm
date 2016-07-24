package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/22.
 */
@Table(name = "ContractAssessBean")
public class ContractAssessBean implements Parcelable
{
    public String contractId;//承包区id
    public String contractName;//承包区id
    public String assessDate;//考核时间
    public List<ContractAssess> contractAssessList;//图片集

    public ContractAssessBean()
    {
        super();
    }

    protected ContractAssessBean(Parcel in)
    {
        contractId = in.readString();
        contractName = in.readString();
        assessDate = in.readString();
        contractAssessList = in.createTypedArrayList(ContractAssess.CREATOR);
    }

    public static final Creator<ContractAssessBean> CREATOR = new Creator<ContractAssessBean>()
    {
        @Override
        public ContractAssessBean createFromParcel(Parcel in)
        {
            return new ContractAssessBean(in);
        }

        @Override
        public ContractAssessBean[] newArray(int size)
        {
            return new ContractAssessBean[size];
        }
    };

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

    public String getAssessDate()
    {
        return assessDate;
    }

    public void setAssessDate(String assessDate)
    {
        this.assessDate = assessDate;
    }

    public List<ContractAssess> getContractAssessList()
    {
        return contractAssessList;
    }

    public void setContractAssessList(List<ContractAssess> contractAssessList)
    {
        this.contractAssessList = contractAssessList;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(contractId);
        dest.writeString(contractName);
        dest.writeString(assessDate);
        dest.writeTypedList(contractAssessList);
    }
}
