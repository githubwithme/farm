package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hasee on 2016/8/6.
 */
public class OrderTypeNum implements Parcelable
{

    String typeNum1;//待审批数量
    String typeNum2;//待付定金的数量
    String typeNum3;//待采收的数量
    String typeNum4;//待结算的数量
    public OrderTypeNum()
    {
        super();
    }

    protected OrderTypeNum(Parcel in)
    {
        typeNum1 = in.readString();
        typeNum2 = in.readString();
        typeNum3 = in.readString();
        typeNum4 = in.readString();
    }

    public static final Creator<OrderTypeNum> CREATOR = new Creator<OrderTypeNum>()
    {
        @Override
        public OrderTypeNum createFromParcel(Parcel in)
        {
            return new OrderTypeNum(in);
        }

        @Override
        public OrderTypeNum[] newArray(int size)
        {
            return new OrderTypeNum[size];
        }
    };

    public String getTypeNum1()
    {
        return typeNum1;
    }

    public void setTypeNum1(String typeNum1)
    {
        this.typeNum1 = typeNum1;
    }

    public String getTypeNum2()
    {
        return typeNum2;
    }

    public void setTypeNum2(String typeNum2)
    {
        this.typeNum2 = typeNum2;
    }

    public String getTypeNum3()
    {
        return typeNum3;
    }

    public void setTypeNum3(String typeNum3)
    {
        this.typeNum3 = typeNum3;
    }

    public String getTypeNum4()
    {
        return typeNum4;
    }

    public void setTypeNum4(String typeNum4)
    {
        this.typeNum4 = typeNum4;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(typeNum1);
        dest.writeString(typeNum2);
        dest.writeString(typeNum3);
        dest.writeString(typeNum4);

    }
}
