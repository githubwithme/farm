package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Description: plantgrowthtab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "PlantGcd")
public class PlantGcd implements Parcelable
{
    public String Id;
    public String uId;
    public String plantgcdName;
    public String regDate;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String x;
    public String y;
    public List<String> imgUrl;
    public String newDate;
    public String growthCount;
    public String growthvidioCount;
    public String plants;
    public String average_hnum;//观测点下最新一次观测记录，hnum平均值
    public String average_wnum;
    public String average_ynum;
    public String average_xnum;
    public String cdate;//观测点下最新一次观测记录，cdate=1的个数
    public String zdate;//观测点下最新一次观测记录，zdate=1的个数
    public String planttype;//观测点下最新一次观测记录，planttype=1的个数


    public String getAverage_hnum()
    {
        return average_hnum;
    }

    public void setAverage_hnum(String average_hnum)
    {
        this.average_hnum = average_hnum;
    }

    public String getAverage_wnum()
    {
        return average_wnum;
    }

    public void setAverage_wnum(String average_wnum)
    {
        this.average_wnum = average_wnum;
    }

    public String getAverage_ynum()
    {
        return average_ynum;
    }

    public void setAverage_ynum(String average_ynum)
    {
        this.average_ynum = average_ynum;
    }

    public String getAverage_xnum()
    {
        return average_xnum;
    }

    public void setAverage_xnum(String average_xnum)
    {
        this.average_xnum = average_xnum;
    }

    public String getCdate()
    {
        return cdate;
    }

    public void setCdate(String cdate)
    {
        this.cdate = cdate;
    }

    public String getZdate()
    {
        return zdate;
    }

    public void setZdate(String zdate)
    {
        this.zdate = zdate;
    }

    public String getPlanttype()
    {
        return planttype;
    }

    public void setPlanttype(String planttype)
    {
        this.planttype = planttype;
    }

    public String getPlants()
    {
        return plants;
    }

    public void setPlants(String plants)
    {
        this.plants = plants;
    }

    public String getGrowthvidioCount()
    {
        return growthvidioCount;
    }

    public void setGrowthvidioCount(String growthvidioCount)
    {
        this.growthvidioCount = growthvidioCount;
    }

    public String getGrowthCount()
    {
        return growthCount;
    }

    public void setGrowthCount(String growthCount)
    {
        this.growthCount = growthCount;
    }

    public void setX(String x)
    {
        this.x = x;
    }

    public String getX()
    {
        return x;
    }

    public void setY(String y)
    {
        this.y = y;
    }

    public String getY()
    {
        return y;
    }

    public void setNewDate(String newDate)
    {
        this.newDate = newDate;
    }

    public String getNewDate()
    {
        return newDate;
    }

    public void setPlantgcdName(String plantgcdName)
    {
        this.plantgcdName = plantgcdName;
    }

    public String getPlantgcdName()
    {
        return plantgcdName;
    }

    public void setImgUrl(List<String> imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public List<String> getImgUrl()
    {
        return imgUrl;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String Id)
    {
        this.Id = Id;
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

    public String getareaId()
    {
        return areaId;
    }

    public void setareaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getareaName()
    {
        return areaName;
    }

    public void setareaName(String areaName)
    {
        this.areaName = areaName;
    }


    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<PlantGcd> CREATOR = new Creator()
    {
        @Override
        public PlantGcd createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            PlantGcd p = new PlantGcd();
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setPlantgcdName(source.readString());
            p.setregDate(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setareaId(source.readString());
            p.setareaName(source.readString());
            p.setX(source.readString());
            p.setY(source.readString());
            p.imgUrl = source.readArrayList(List.class.getClassLoader());
            p.setNewDate(source.readString());
            p.setGrowthCount(source.readString());
            p.setGrowthvidioCount(source.readString());
            p.setPlants(source.readString());
            p.setAverage_hnum(source.readString());
            p.setAverage_wnum(source.readString());
            p.setAverage_ynum(source.readString());
            p.setAverage_xnum(source.readString());
            p.setCdate(source.readString());
            p.setZdate(source.readString());
            p.setPlanttype(source.readString());
            return p;
        }

        @Override
        public PlantGcd[] newArray(int size)
        {
            return new PlantGcd[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(Id);
        p.writeString(uId);
        p.writeString(plantgcdName);
        p.writeString(regDate);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(x);
        p.writeString(y);
        p.writeList(imgUrl);
        p.writeString(newDate);
        p.writeString(growthCount);
        p.writeString(growthvidioCount);
        p.writeString(plants);
        p.writeString(average_hnum);
        p.writeString(average_wnum);
        p.writeString(average_ynum);
        p.writeString(average_xnum);
        p.writeString(cdate);
        p.writeString(zdate);
        p.writeString(planttype);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
