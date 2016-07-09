package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${hmj} on 2016/7/2.
 */
public class AgreementBean implements Parcelable
{
    String id;
    String agreementNumber;
    String agreementTime;
    String year;
    String customerid;
    String customername;
    String regtime;

    public AgreementBean()//实体类需要一个空构造函数
    {
        super();
    }

    protected AgreementBean(Parcel in)
    {
        id = in.readString();
        agreementNumber = in.readString();
        agreementTime = in.readString();
        year = in.readString();
        customerid = in.readString();
        customername = in.readString();
        regtime = in.readString();
    }

    public static final Creator<AgreementBean> CREATOR = new Creator<AgreementBean>()
    {
        @Override
        public AgreementBean createFromParcel(Parcel in)
        {
            return new AgreementBean(in);
        }

        @Override
        public AgreementBean[] newArray(int size)
        {
            return new AgreementBean[size];
        }
    };

    public String getAgreementTime()
    {
        return agreementTime;
    }

    public void setAgreementTime(String agreementTime)
    {
        this.agreementTime = agreementTime;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAgreementNumber()
    {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber)
    {
        this.agreementNumber = agreementNumber;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getCustomerid()
    {
        return customerid;
    }

    public void setCustomerid(String customerid)
    {
        this.customerid = customerid;
    }

    public String getCustomername()
    {
        return customername;
    }

    public void setCustomername(String customername)
    {
        this.customername = customername;
    }

    public String getRegtime()
    {
        return regtime;
    }

    public void setRegtime(String regtime)
    {
        this.regtime = regtime;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(agreementNumber);
        dest.writeString(agreementTime);
        dest.writeString(year);
        dest.writeString(customerid);
        dest.writeString(customername);
        dest.writeString(regtime);
    }
}
