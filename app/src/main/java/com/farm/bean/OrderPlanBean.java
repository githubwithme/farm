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
    String orderNumber;//合计订单数
    String carNumber;//合计车辆数
    String notPayDepositNumber;//待付定金订单数
    String paidDepositNumber;//已付定金订单数
    String readyNumber;//已就绪订单数
    String notreadyNumber;//未就绪订单数
    List<OrderPlan> OrderPlanList;//各个园区订单车辆数
    List<SellOrder_New> sellOrderList;//订单列表


    public OrderPlanBean()
    {
        super();
    }

    protected OrderPlanBean(Parcel in)
    {
        date = in.readString();
        orderNumber = in.readString();
        carNumber = in.readString();
        notPayDepositNumber = in.readString();
        paidDepositNumber = in.readString();
        readyNumber = in.readString();
        notreadyNumber = in.readString();
        OrderPlanList = in.createTypedArrayList(OrderPlan.CREATOR);
        sellOrderList = in.createTypedArrayList(SellOrder_New.CREATOR);
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

    public String getNotPayDepositNumber()
    {
        return notPayDepositNumber;
    }

    public void setNotPayDepositNumber(String notPayDepositNumber)
    {
        this.notPayDepositNumber = notPayDepositNumber;
    }

    public String getPaidDepositNumber()
    {
        return paidDepositNumber;
    }

    public void setPaidDepositNumber(String paidDepositNumber)
    {
        this.paidDepositNumber = paidDepositNumber;
    }

    public String getReadyNumber()
    {
        return readyNumber;
    }

    public void setReadyNumber(String readyNumber)
    {
        this.readyNumber = readyNumber;
    }

    public String getNotreadyNumber()
    {
        return notreadyNumber;
    }

    public void setNotreadyNumber(String notreadyNumber)
    {
        this.notreadyNumber = notreadyNumber;
    }

    public List<OrderPlan> getOrderPlanList()
    {
        return OrderPlanList;
    }

    public void setOrderPlanList(List<OrderPlan> orderPlanList)
    {
        OrderPlanList = orderPlanList;
    }

    public List<SellOrder_New> getSellOrderList()
    {
        return sellOrderList;
    }

    public void setSellOrderList(List<SellOrder_New> sellOrderList)
    {
        this.sellOrderList = sellOrderList;
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
        dest.writeString(notPayDepositNumber);
        dest.writeString(paidDepositNumber);
        dest.writeString(readyNumber);
        dest.writeString(notreadyNumber);
        dest.writeTypedList(OrderPlanList);
        dest.writeTypedList(sellOrderList);
    }
}
