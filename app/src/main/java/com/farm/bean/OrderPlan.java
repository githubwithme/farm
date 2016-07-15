package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${hmj} on 2016/7/14.
 */
public class OrderPlan implements Parcelable
{
    String orderNumber;//该园区订单数
    String carNumber;//该园区该天车辆数
    String parkname;//园区名称

    public OrderPlan()
    {
        super();
    }

    protected OrderPlan(Parcel in)
    {
        orderNumber = in.readString();
        carNumber = in.readString();
        parkname = in.readString();
    }

    public static final Creator<OrderPlan> CREATOR = new Creator<OrderPlan>()
    {
        @Override
        public OrderPlan createFromParcel(Parcel in)
        {
            return new OrderPlan(in);
        }

        @Override
        public OrderPlan[] newArray(int size)
        {
            return new OrderPlan[size];
        }
    };

    public String getParkname()
    {
        return parkname;
    }

    public void setParkname(String parkname)
    {
        this.parkname = parkname;
    }

    public String getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public String getCarNumber()
    {
        return carNumber;
    }

    public void setCarNumber(String carNumber)
    {
        this.carNumber = carNumber;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(orderNumber);
        dest.writeString(carNumber);
        dest.writeString(parkname);
    }
}
