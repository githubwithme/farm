package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${hmj} on 2016/7/14.
 */
public class OrderPlan implements Parcelable
{
    String orderId;
    String orderStatus;
    String prepareStatus;
    String buyer;
    String product;
    String parkname;

    public OrderPlan()
    {
        super();
    }

    public String getParkname()
    {
        return parkname;
    }

    public void setParkname(String parkname)
    {
        this.parkname = parkname;
    }

    protected OrderPlan(Parcel in)
    {
        orderId = in.readString();
        orderStatus = in.readString();
        prepareStatus = in.readString();
        buyer = in.readString();
        product = in.readString();
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

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getPrepareStatus()
    {
        return prepareStatus;
    }

    public void setPrepareStatus(String prepareStatus)
    {
        this.prepareStatus = prepareStatus;
    }

    public String getBuyer()
    {
        return buyer;
    }

    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(orderId);
        dest.writeString(orderStatus);
        dest.writeString(prepareStatus);
        dest.writeString(buyer);
        dest.writeString(product);
    }
}
