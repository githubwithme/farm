package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "goodslisttab")
public class goodslisttab implements Parcelable
{
    @Id
    @NoAutoIncrement
    public String id="";
    public String uID="";
    public String regDate="";
    public String goodsName="";
    public String goodsunit="";
    public String goodsSpec="";
    public String goodsNote="";
    public String goodsProducer="";
    public String goodsTypeID="";
    public String userDefTypeID="";
    public String daysBeforeWarning="";
    public String levelOfWarning="";
    public String isDelete="";
    public String DeleteDate="";
    public String imgurl="";
    public String goodsSum="";
    public String parkId="";
    public String parkName="";
    public String areaId="";
    public String areaName="";
    public String YL="";
    public String ZS="";


    public String getZS()
    {
        return ZS;
    }

    public void setZS(String ZS)
    {
        this.ZS = ZS;
    }

    public String getYL()
    {
        return YL;
    }

    public void setYL(String YL)
    {
        this.YL = YL;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getParkName()
    {
        return parkName;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getParkId()
    {
        return parkId;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getGoodsSum()
    {
        return goodsSum;
    }

    public void setGoodsSum(String goodsSum)
    {
        this.goodsSum = goodsSum;
    }

    public void setImgurl(String imgurl)
    {
        this.imgurl = imgurl;
    }

    public String getImgurl()
    {
        return imgurl;
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getuID()
    {
        return uID;
    }

    public void setuID(String uID)
    {
        this.uID = uID;
    }

    public String getregDate()
    {
        return regDate;
    }

    public void setregDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getgoodsName()
    {
        return goodsName;
    }

    public void setgoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getgoodsunit()
    {
        return goodsunit;
    }

    public void setgoodsunit(String goodsunit)
    {
        this.goodsunit = goodsunit;
    }

    public String getgoodsSpec()
    {
        return goodsSpec;
    }

    public void setgoodsSpec(String goodsSpec)
    {
        this.goodsSpec = goodsSpec;
    }

    public String getgoodsNote()
    {
        return goodsNote;
    }

    public void setgoodsNote(String goodsNote)
    {
        this.goodsNote = goodsNote;
    }

    public String getgoodsProducer()
    {
        return goodsProducer;
    }

    public void setgoodsProducer(String goodsProducer)
    {
        this.goodsProducer = goodsProducer;
    }

    public String getgoodsTypeID()
    {
        return goodsTypeID;
    }

    public void setgoodsTypeID(String goodsTypeID)
    {
        this.goodsTypeID = goodsTypeID;
    }

    public String getuserDefTypeID()
    {
        return userDefTypeID;
    }

    public void setuserDefTypeID(String userDefTypeID)
    {
        this.userDefTypeID = userDefTypeID;
    }

    public String getdaysBeforeWarning()
    {
        return daysBeforeWarning;
    }

    public void setdaysBeforeWarning(String daysBeforeWarning)
    {
        this.daysBeforeWarning = daysBeforeWarning;
    }

    public String getlevelOfWarning()
    {
        return levelOfWarning;
    }

    public void setlevelOfWarning(String levelOfWarning)
    {
        this.levelOfWarning = levelOfWarning;
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

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<goodslisttab> CREATOR = new Creator()
    {
        @Override
        public goodslisttab createFromParcel(Parcel source)
        {
            goodslisttab p = new goodslisttab();
            p.setId(source.readString());
            p.setuID(source.readString());
            p.setregDate(source.readString());
            p.setgoodsName(source.readString());
            p.setgoodsunit(source.readString());
            p.setgoodsSpec(source.readString());
            p.setgoodsNote(source.readString());
            p.setgoodsProducer(source.readString());
            p.setgoodsTypeID(source.readString());
            p.setuserDefTypeID(source.readString());
            p.setdaysBeforeWarning(source.readString());
            p.setlevelOfWarning(source.readString());
            p.setisDelete(source.readString());
            p.setDeleteDate(source.readString());
            p.setImgurl(source.readString());
            p.setGoodsSum(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setAreaId(source.readString());
            p.setAreaName(source.readString());
            p.setYL(source.readString());
            p.setZS(source.readString());
            return p;
        }

        @Override
        public goodslisttab[] newArray(int size)
        {
            return new goodslisttab[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uID);
        p.writeString(regDate);
        p.writeString(goodsName);
        p.writeString(goodsunit);
        p.writeString(goodsSpec);
        p.writeString(goodsNote);
        p.writeString(goodsProducer);
        p.writeString(goodsTypeID);
        p.writeString(userDefTypeID);
        p.writeString(daysBeforeWarning);
        p.writeString(levelOfWarning);
        p.writeString(isDelete);
        p.writeString(DeleteDate);
        p.writeString(imgurl);
        p.writeString(goodsSum);
        p.writeString(parkId);
        p.writeString(goodsSum);
        p.writeString(areaId);
        p.writeString(areaName);
        p.writeString(YL);
        p.writeString(ZS);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
