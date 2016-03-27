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
@Table(name = "BatchOfProduct")
public class BatchOfProduct implements Parcelable // 与数据库不一致
{
    public String id;
    public String uId;
    public String regDate;
    public String numberofproductbatch;
    public String dateofproductbatch;
    public String isSell;
    public String numberofsold;
    public String numberforsale;
    public String output;


    public String getOutput()
    {
        return output;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public void setNumberforsale(String numberforsale)
    {
        this.numberforsale = numberforsale;
    }

    public String getNumberforsale()
    {
        return numberforsale;
    }

    public void setNumberofsold(String numberofsold)
    {
        this.numberofsold = numberofsold;
    }

    public String getNumberofsold()
    {
        return numberofsold;
    }

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




    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<BatchOfProduct> CREATOR = new Creator()
    {
        @Override
        public BatchOfProduct createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            BatchOfProduct p = new BatchOfProduct();
            p.setid(source.readString());
            p.setuId(source.readString());
            p.setregDate(source.readString());
            p.setNumberofproductbatch(source.readString());
            p.setDateofproductbatch(source.readString());
            p.setIsSell(source.readString());
            p.setNumberofsold(source.readString());
            p.setNumberforsale(source.readString());
            p.setOutput(source.readString());
            return p;
        }

        @Override
        public BatchOfProduct[] newArray(int size)
        {
            return new BatchOfProduct[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(numberofproductbatch);
        p.writeString(dateofproductbatch);
        p.writeString(isSell);
        p.writeString(numberofsold);
        p.writeString(numberforsale);
        p.writeString(output);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
