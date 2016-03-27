package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p>
 * Copyright: Copyright (c) 2015
 * <p>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "SellOrderDetail")
public class SellOrderDetail implements Parcelable // 与数据库不一致
{
    public String id;
    public String contractId;
    public String contractNum;
    public String uId;
    public String regDate;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String note;
    public String isStart;
    public String isEnd;
    public String output;

    public String orderId;
    public String productId;
    public String productBatch;
    public String numOfSellPlant;
    public String sellPrice;
    public String sellDate;
    public String sellQuantity;
    public String actualnumOfSellPlant;
    public String actualsellPrice;
    public String orderid;

    public void setOrderid(String orderid)
    {
        this.orderid = orderid;
    }

    public String getOrderid()
    {
        return orderid;
    }

    public void setActualsellPrice(String actualsellPrice)
    {
        this.actualsellPrice = actualsellPrice;
    }

    public String getActualsellPrice()
    {
        return actualsellPrice;
    }

    public void setActualnumOfSellPlant(String actualnumOfSellPlant)
    {
        this.actualnumOfSellPlant = actualnumOfSellPlant;
    }

    public String getActualnumOfSellPlant()
    {
        return actualnumOfSellPlant;
    }

    public void setSellQuantity(String sellQuantity)
    {
        this.sellQuantity = sellQuantity;
    }

    public String getSellQuantity()
    {
        return sellQuantity;
    }

    public void setSellDate(String sellDate)
    {
        this.sellDate = sellDate;
    }

    public String getSellDate()
    {
        return sellDate;
    }

    public void setSellPrice(String sellPrice)
    {
        this.sellPrice = sellPrice;
    }

    public String getSellPrice()
    {
        return sellPrice;
    }

    public void setNumOfSellPlant(String numOfSellPlant)
    {
        this.numOfSellPlant = numOfSellPlant;
    }

    public String getNumOfSellPlant()
    {
        return numOfSellPlant;
    }

    public void setProductBatch(String productBatch)
    {
        this.productBatch = productBatch;
    }

    public String getProductBatch()
    {
        return productBatch;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getContractId()
    {
        return contractId;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public String getOutput()
    {
        return output;
    }

    public void setContractNum(String contractNum)
    {
        this.contractNum = contractNum;
    }

    public String getContractNum()
    {
        return contractNum;
    }



    public void setIsStart(String isStart)
    {
        this.isStart = isStart;
    }

    public String getIsStart()
    {
        return isStart;
    }

    public void setIsEnd(String isEnd)
    {
        this.isEnd = isEnd;
    }

    public String getIsEnd()
    {
        return isEnd;
    }



    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }

    public String getid()
    {
        return id;
    }

    public void setid(String id)
    {
        this.id = id;
    }

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getregDate()
    {
        return regDate;
    }

    public void setregDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getareaName()
    {
        return areaName;
    }

    public void setareaName(String areaName)
    {
        this.areaName = areaName;
    }


    public String getparkId()
    {
        return parkId;
    }

    public void setparkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getparkName()
    {
        return parkName;
    }

    public void setparkName(String parkName)
    {
        this.parkName = parkName;
    }


    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<SellOrderDetail> CREATOR = new Creator()
    {
        @Override
        public SellOrderDetail createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            SellOrderDetail p = new SellOrderDetail();
            p.setid(source.readString());
            p.setContractId(source.readString());
            p.setContractNum(source.readString());
            p.setuId(source.readString());
            p.setregDate(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setAreaId(source.readString());
            p.setareaName(source.readString());
            p.setNote(source.readString());
            p.setIsStart(source.readString());
            p.setIsEnd(source.readString());
            p.setOutput(source.readString());

            p.setOrderId(source.readString());
            p.setProductId(source.readString());
            p.setProductBatch(source.readString());
            p.setNumOfSellPlant(source.readString());
            p.setSellPrice(source.readString());
            p.setSellDate(source.readString());
            p.setSellQuantity(source.readString());
            p.setActualnumOfSellPlant(source.readString());
            p.setActualsellPrice(source.readString());
            p.setOrderId(source.readString());

            return p;
        }

        @Override
        public SellOrderDetail[] newArray(int size)
        {
            return new SellOrderDetail[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(contractId);
        p.writeString(contractNum);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(note);
        p.writeString(isStart);
        p.writeString(isEnd);
        p.writeString(output);

        p.writeString(orderId);
        p.writeString(productId);
        p.writeString(productBatch);
        p.writeString(numOfSellPlant);
        p.writeString(sellPrice);
        p.writeString(sellDate);
        p.writeString(sellQuantity);
        p.writeString(actualnumOfSellPlant);
        p.writeString(actualsellPrice);
        p.writeString(orderid);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
