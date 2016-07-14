package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/14.
 */
public class OrderPlanBean implements Parcelable
{
    String date;
    String orderNumber;
    String carNumber;
    List<OrderPlan> orderList;


    public OrderPlanBean()//实体类需要一个空构造函数
    {
        super();
    }

    public static Creator<OrderPlanBean> getCREATOR()
    {
        return CREATOR;
    }

    protected OrderPlanBean(Parcel in)
    {
        date = in.readString();
        orderNumber = in.readString();
        carNumber = in.readString();
    }

    public static final Creator<OrderPlanBean> CREATOR = new Creator<OrderPlanBean>()
    {
        @Override
        public OrderPlanBean createFromParcel(Parcel in)
        {
            return new OrderPlanBean(in);
        }

        @Override
        public OrderPlanBean[] newArray(int size)
        {
            return new OrderPlanBean[size];
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

    public List<OrderPlan> getOrderList()
    {
        return orderList;
    }

    public void setOrderList(List<OrderPlan> orderList)
    {
        this.orderList = orderList;
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
        dest.writeString(orderNumber);
        dest.writeString(carNumber);
    }


}
