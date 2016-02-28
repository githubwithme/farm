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
@Table(name = "ProductBatch")
public class ProductBatch implements Parcelable // 与数据库不一致
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
    public String numberofproductbatch;
    public String dateofproductbatch;
    public String isSell;


    public void setIsSell(String isSell)
    {
        this.isSell = isSell;
    }

    public String getIsSell()
    {
        return isSell;
    }

    public void setDateofproductbatch(String dateofproductbatch)
    {
        this.dateofproductbatch = dateofproductbatch;
    }

    public String getDateofproductbatch()
    {
        return dateofproductbatch;
    }

    public void setNumberofproductbatch(String numberofproductbatch)
    {
        this.numberofproductbatch = numberofproductbatch;
    }

    public String getNumberofproductbatch()
    {
        return numberofproductbatch;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getContractId()
    {
        return contractId;
    }


    public void setContractNum(String contractNum)
    {
        this.contractNum = contractNum;
    }

    public String getContractNum()
    {
        return contractNum;
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

    public static final Creator<ProductBatch> CREATOR = new Creator()
    {
        @Override
        public ProductBatch createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ProductBatch p = new ProductBatch();
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
            p.setNumberofproductbatch(source.readString());
            p.setDateofproductbatch(source.readString());
            p.setIsSell(source.readString());
            return p;
        }

        @Override
        public ProductBatch[] newArray(int size)
        {
            return new ProductBatch[size];
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
        p.writeString(numberofproductbatch);
        p.writeString(dateofproductbatch);
        p.writeString(isSell);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
