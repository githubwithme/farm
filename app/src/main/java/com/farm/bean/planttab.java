package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Description: planttab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "planttab")
public class planttab implements Parcelable// 和数据库表不一致
{
    public String Id;
    public String uId;
    public String regDate;
    public String plantName;
    public String plantType = "";
    public String parkId;
    public String parkName;
    public String areaId;
    public String areaName;
    public String plantNote;
    public String isDelete;
    public String deleteDate;
    public String x;
    public String y;
    public String AYear;
    public List<String> imgUrl;
    public String isobser;

    public String yNum;
    public String hNum;
    public String wNum;
    public String xNum;
    public String cDate;
    public String zDate;
    public String cjUserID;
    public String cjUserName;
    public String yColor;
    public String GrowthDate;
    public String plantvidioCount;
    public String plantCount;

    public String getPlantCount()
    {
        return plantCount;
    }

    public void setPlantCount(String plantCount)
    {
        this.plantCount = plantCount;
    }

    public void setPlantvidioCount(String plantvidioCount)
    {
        this.plantvidioCount = plantvidioCount;
    }

    public String getPlantvidioCount()
    {
        return plantvidioCount;
    }

    public void setGrowthDate(String growthDate)
    {
        GrowthDate = growthDate;
    }

    public String getGrowthDate()
    {
        return GrowthDate;
    }

    public void setyColor(String yColor)
    {
        this.yColor = yColor;
    }

    public String getyColor()
    {
        return yColor;
    }

    public void setyNum(String yNum)
    {
        this.yNum = yNum;
    }

    public String getyNum()
    {
        return yNum;
    }

    public void sethNum(String hNum)
    {
        this.hNum = hNum;
    }

    public String gethNum()
    {
        return hNum;
    }

    public void setwNum(String wNum)
    {
        this.wNum = wNum;
    }

    public String getwNum()
    {
        return wNum;
    }

    public void setxNum(String xNum)
    {
        this.xNum = xNum;
    }

    public String getxNum()
    {
        return xNum;
    }

    public void setcDate(String cDate)
    {
        this.cDate = cDate;
    }

    public String getcDate()
    {
        return cDate;
    }

    public void setzDate(String zDate)
    {
        this.zDate = zDate;
    }

    public String getzDate()
    {
        return zDate;
    }

    public void setCjUserID(String cjUserID)
    {
        this.cjUserID = cjUserID;
    }

    public String getCjUserID()
    {
        return cjUserID;
    }

    public void setCjUserName(String cjUserName)
    {
        this.cjUserName = cjUserName;
    }

    public String getCjUserName()
    {
        return cjUserName;
    }

    public void setIsobser(String isobser)
    {
        this.isobser = isobser;
    }

    public String getIsobser()
    {
        return isobser;
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

    public String getplantNote()
    {
        return plantNote;
    }

    public void setplantNote(String plantNote)
    {
        this.plantNote = plantNote;
    }

    public String getisDelete()
    {
        return isDelete;
    }

    public void setisDelete(String isDelete)
    {
        this.isDelete = isDelete;
    }

    public String getdeleteDate()
    {
        return deleteDate;
    }

    public void setdeleteDate(String deleteDate)
    {
        this.deleteDate = deleteDate;
    }

    public String getx()
    {
        return x;
    }

    public void setx(String x)
    {
        this.x = x;
    }

    public String gety()
    {
        return y;
    }

    public void sety(String y)
    {
        this.y = y;
    }

    public String getAYear()
    {
        return AYear;
    }

    public void setAYear(String AYear)
    {
        this.AYear = AYear;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Parcelable.Creator<planttab> CREATOR = new Creator()
    {
        @Override
        public planttab createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            planttab p = new planttab();
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setregDate(source.readString());
            p.setplantName(source.readString());
            p.setplantType(source.readString());
            p.setparkId(source.readString());
            p.setparkName(source.readString());
            p.setareaId(source.readString());
            p.setareaName(source.readString());
            p.setplantNote(source.readString());
            p.setisDelete(source.readString());
            p.setdeleteDate(source.readString());
            p.setx(source.readString());
            p.sety(source.readString());
            p.setAYear(source.readString());
            p.imgUrl = source.readArrayList(List.class.getClassLoader());
            p.setIsobser(source.readString());

            p.setyNum(source.readString());
            p.sethNum(source.readString());
            p.setwNum(source.readString());
            p.setxNum(source.readString());
            p.setcDate(source.readString());
            p.setzDate(source.readString());
            p.setCjUserID(source.readString());
            p.setCjUserName(source.readString());
            p.setyColor(source.readString());
            p.setGrowthDate(source.readString());
            p.setPlantvidioCount(source.readString());
            p.setPlantCount(source.readString());
            return p;
        }

        @Override
        public planttab[] newArray(int size)
        {
            return new planttab[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(Id);
        p.writeString(uId);
        p.writeString(regDate);
        p.writeString(plantName);
        p.writeString(plantType);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(plantNote);
        p.writeString(isDelete);
        p.writeString(deleteDate);
        p.writeString(x);
        p.writeString(y);
        p.writeString(AYear);
        p.writeList(imgUrl);
        p.writeString(isobser);

        p.writeString(yNum);
        p.writeString(hNum);
        p.writeString(wNum);
        p.writeString(xNum);
        p.writeString(cDate);
        p.writeString(zDate);
        p.writeString(cjUserID);
        p.writeString(cjUserName);
        p.writeString(yColor);
        p.writeString(GrowthDate);
        p.writeString(plantvidioCount);
        p.writeString(plantCount);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
