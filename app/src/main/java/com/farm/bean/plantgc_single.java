package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: plantgrowthtab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "plantgc_single")
public class plantgc_single implements Parcelable
{
    public String Id;
    public String uId;
    public String regDate;
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String plantId;
    public String plantName;
    public String plantType;
    public String zDate;
    public String yNum;
    public String lyNum;
    public String wNum;
    public String hNum;
    public String yColor;
    public String xNum;
    public String cDate;
    public String ext1;
    public String ext2;
    public String ext3;
    public String plantGrowthNote;
    public String isDelete;
    public String DeleteDate;
    public String AYear;
    public String cjUserID;
    public String cjUserName;
    public String cjDate;
    public String gcq;
    public String SFCY;
    public String SFYZ;


    public String getSFYZ()
    {
        return SFYZ;
    }

    public void setSFYZ(String SFYZ)
    {
        this.SFYZ = SFYZ;
    }

    public String getSFCY()
    {
        return SFCY;
    }

    public void setSFCY(String SFCY)
    {
        this.SFCY = SFCY;
    }

    public String getGcq()
    {
        return gcq;
    }

    public void setGcq(String gcq)
    {
        this.gcq = gcq;
    }

    public String getLyNum()
    {
        return lyNum;
    }

    public void setLyNum(String lyNum)
    {
        this.lyNum = lyNum;
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

    public String getplantId()
    {
        return plantId;
    }

    public void setplantId(String plantId)
    {
        this.plantId = plantId;
    }

    public String getplantName()
    {
        return plantName;
    }

    public void setplantName(String plantName)
    {
        this.plantName = plantName;
    }

    public String getplantType()
    {
        return plantType;
    }

    public void setplantType(String plantType)
    {
        this.plantType = plantType;
    }


    public String getzDate()
    {
        return zDate;
    }

    public void setzDate(String zDate)
    {
        this.zDate = zDate;
    }

    public String getyNum()
    {
        return yNum;
    }

    public void setyNum(String yNum)
    {
        this.yNum = yNum;
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

    public String getyColor()
    {
        return yColor;
    }

    public void setyColor(String yColor)
    {
        this.yColor = yColor;
    }

    public String getxNum()
    {
        return xNum;
    }

    public void setxNum(String xNum)
    {
        this.xNum = xNum;
    }

    public String getcDate()
    {
        return cDate;
    }

    public void setcDate(String cDate)
    {
        this.cDate = cDate;
    }

    public String getext1()
    {
        return ext1;
    }

    public void setext1(String ext1)
    {
        this.ext1 = ext1;
    }

    public String getext2()
    {
        return ext2;
    }

    public void setext2(String ext2)
    {
        this.ext2 = ext2;
    }

    public String getext3()
    {
        return ext3;
    }

    public void setext3(String ext3)
    {
        this.ext3 = ext3;
    }

    public String getplantGrowthNote()
    {
        return plantGrowthNote;
    }

    public void setplantGrowthNote(String plantGrowthNote)
    {
        this.plantGrowthNote = plantGrowthNote;
    }

    public String getisDelete()
    {
        return isDelete;
    }

    public void setisDelete(String isDelete)
    {
        this.isDelete = isDelete;
    }

    public String getDeleteDate()
    {
        return DeleteDate;
    }

    public void setDeleteDate(String DeleteDate)
    {
        this.DeleteDate = DeleteDate;
    }

    public String getAYear()
    {
        return AYear;
    }

    public void setAYear(String AYear)
    {
        this.AYear = AYear;
    }

    public String getcjUserID()
    {
        return cjUserID;
    }

    public void setcjUserID(String cjUserID)
    {
        this.cjUserID = cjUserID;
    }

    public String getcjUserName()
    {
        return cjUserName;
    }

    public void setcjUserName(String cjUserName)
    {
        this.cjUserName = cjUserName;
    }

    public String getcjDate()
    {
        return cjDate;
    }

    public void setcjDate(String cjDate)
    {
        this.cjDate = cjDate;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<plantgc_single> CREATOR = new Creator()
    {
        @Override
        public plantgc_single createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            plantgc_single p = new plantgc_single();
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setregDate(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setareaId(source.readString());
            p.setareaName(source.readString());
            p.setplantId(source.readString());
            p.setplantName(source.readString());
            p.setplantType(source.readString());
            p.setzDate(source.readString());
            p.setyNum(source.readString());
            p.setLyNum(source.readString());
            p.setwNum(source.readString());
            p.sethNum(source.readString());
            p.setyColor(source.readString());
            p.setxNum(source.readString());
            p.setcDate(source.readString());
            p.setext1(source.readString());
            p.setext2(source.readString());
            p.setext3(source.readString());
            p.setplantGrowthNote(source.readString());
            p.setisDelete(source.readString());
            p.setDeleteDate(source.readString());
            p.setAYear(source.readString());
            p.setcjUserID(source.readString());
            p.setcjUserName(source.readString());
            p.setcjDate(source.readString());
            p.setGcq(source.readString());
            p.setSFCY(source.readString());
            p.setSFYZ(source.readString());
            return p;
        }

        @Override
        public plantgc_single[] newArray(int size)
        {
            return new plantgc_single[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(Id);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(plantId);
        p.writeString(plantName);
        p.writeString(plantType);
        p.writeString(zDate);
        p.writeString(yNum);
        p.writeString(lyNum);
        p.writeString(wNum);
        p.writeString(hNum);
        p.writeString(yColor);
        p.writeString(xNum);
        p.writeString(cDate);
        p.writeString(ext1);
        p.writeString(ext2);
        p.writeString(ext3);
        p.writeString(plantGrowthNote);
        p.writeString(isDelete);
        p.writeString(DeleteDate);
        p.writeString(AYear);
        p.writeString(cjUserID);
        p.writeString(cjUserName);
        p.writeString(cjDate);
        p.writeString(gcq);
        p.writeString(SFCY);
        p.writeString(SFYZ);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    private static plantgc_single singleton = new plantgc_single();

    private plantgc_single()
    {
    }

    public static plantgc_single getInstance()
    {
        return singleton;
    }

    public void clearAll()
    {
        singleton = new plantgc_single();
    }
}
