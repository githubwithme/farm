package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/12.
 */
@Table(name = "PeopelList")
public class PeopelList  implements Parcelable
{
    public String id;
    public String realName;
    public String userlevelName;
    public String parkId ;
    public String parkName;
    public String areaId;
    public String areaName;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserlevelName() {
        return userlevelName;
    }

    public void setUserlevelName(String userlevelName) {
        this.userlevelName = userlevelName;
    }
    public static final Creator<PeopelList> CREATOR = new Creator()
    {
        @Override
        public PeopelList createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            PeopelList p = new PeopelList();
            p.setRealName(source.readString());
            p.setParkId(source.readString());
            p.setParkName(source.readString());
            p.setAreaId(source.readString());
            p.setAreaName(source.readString());

            return  p;
        }

        @Override
        public PeopelList[] newArray(int size)
        {
            return new PeopelList[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {

        p.writeString(id);
        p.writeString(realName);
        p.writeString(parkName);
        p.writeString(userlevelName);
        p.writeString(parkId);
        p.writeString(parkName);
        p.writeString(areaId);
        p.writeString(areaName);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
