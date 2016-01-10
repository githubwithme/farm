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
@Table(name = "PlantGcjl")
public class PlantGcjl implements Parcelable
{
    public String Id;
    public String uId;
    public String regDate;
    public String gcdId;
    public String gcq;
    public String jjbx;
    public String ybx;
    public String gxbx;
    public String lbx;
    public String ghbx;
    public String csgbx;
    public String cygbx;
    public String szfx;
    public String cjUserID;
    public String cjUserName;
    public String yNum;
    public String hNum;
    public String wNum;
    public String xNum;
    public List<String> imgUrl;
    public List<plantgrowthtab> plantGrowth;
    public Dictionary bx;


    public Dictionary getBx()
    {
        return bx;
    }

    public void setBx(Dictionary bx)
    {
        this.bx = bx;
    }

    public String getxNum()
    {
        return xNum;
    }

    public void setxNum(String xNum)
    {
        this.xNum = xNum;
    }

    public String getwNum()
    {
        return wNum;
    }

    public void setwNum(String wNum)
    {
        this.wNum = wNum;
    }

    public String gethNum()
    {
        return hNum;
    }

    public void sethNum(String hNum)
    {
        this.hNum = hNum;
    }

    public String getyNum()
    {
        return yNum;
    }

    public void setyNum(String yNum)
    {
        this.yNum = yNum;
    }

    public List<plantgrowthtab> getPlantGrowth()
    {
        return plantGrowth;
    }

    public void setPlantGrowth(List<plantgrowthtab> plantGrowth)
    {
        this.plantGrowth = plantGrowth;
    }

    public void setImgUrl(List<String> imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public List<String> getImgUrl()
    {
        return imgUrl;
    }

    public String getCjUserName()
    {
        return cjUserName;
    }

    public void setCjUserName(String cjUserName)
    {
        this.cjUserName = cjUserName;
    }

    public String getCjUserID()
    {
        return cjUserID;
    }

    public void setCjUserID(String cjUserID)
    {
        this.cjUserID = cjUserID;
    }

    public String getSzfx()
    {
        return szfx;
    }

    public void setSzfx(String szfx)
    {
        this.szfx = szfx;
    }

    public String getCygbx()
    {
        return cygbx;
    }

    public void setCygbx(String cygbx)
    {
        this.cygbx = cygbx;
    }

    public String getCsgbx()
    {
        return csgbx;
    }

    public void setCsgbx(String csgbx)
    {
        this.csgbx = csgbx;
    }

    public String getGhbx()
    {
        return ghbx;
    }

    public void setGhbx(String ghbx)
    {
        this.ghbx = ghbx;
    }

    public String getLbx()
    {
        return lbx;
    }

    public void setLbx(String lbx)
    {
        this.lbx = lbx;
    }

    public String getGxbx()
    {
        return gxbx;
    }

    public void setGxbx(String gxbx)
    {
        this.gxbx = gxbx;
    }

    public String getYbx()
    {
        return ybx;
    }

    public void setYbx(String ybx)
    {
        this.ybx = ybx;
    }

    public String getJjbx()
    {
        return jjbx;
    }

    public void setJjbx(String jjbx)
    {
        this.jjbx = jjbx;
    }

    public String getGcq()
    {
        return gcq;
    }

    public void setGcq(String gcq)
    {
        this.gcq = gcq;
    }

    public String getGcdId()
    {
        return gcdId;
    }

    public void setGcdId(String gcdId)
    {
        this.gcdId = gcdId;
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getId()
    {
        return Id;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<PlantGcjl> CREATOR = new Creator()
    {
        @Override
        public PlantGcjl createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            PlantGcjl p = new PlantGcjl();
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setRegDate(source.readString());
            p.setGcdId(source.readString());
            p.setGcq(source.readString());
            p.setJjbx(source.readString());
            p.setYbx(source.readString());
            p.setGxbx(source.readString());
            p.setLbx(source.readString());
            p.setGhbx(source.readString());
            p.setCsgbx(source.readString());
            p.setCygbx(source.readString());
            p.setSzfx(source.readString());
            p.setCjUserID(source.readString());
            p.setCjUserName(source.readString());
            p.setyNum(source.readString());
            p.sethNum(source.readString());
            p.setwNum(source.readString());
            p.setxNum(source.readString());
            p.imgUrl = source.readArrayList(List.class.getClassLoader());
            p.plantGrowth = source.readArrayList(plantgrowthtab.class.getClassLoader());
            p.bx = source.readParcelable(Dictionary.class.getClassLoader());
            return p;
        }

        @Override
        public PlantGcjl[] newArray(int size)
        {
            return new PlantGcjl[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(Id);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(gcdId);
        p.writeString(gcq);
        p.writeString(jjbx);
        p.writeString(ybx);
        p.writeString(gxbx);
        p.writeString(lbx);
        p.writeString(ghbx);
        p.writeString(csgbx);
        p.writeString(cygbx);
        p.writeString(szfx);
        p.writeString(cjUserID);
        p.writeString(cjUserName);
        p.writeString(yNum);
        p.writeString(hNum);
        p.writeString(wNum);
        p.writeString(xNum);
        p.writeList(imgUrl);
        p.writeList(plantGrowth);
        p.writeParcelable((Parcelable) bx, arg1);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
