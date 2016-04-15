package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *地理坐标信息表:记录销售区域
 * @version 1.0
 */
@Table(name = "CoordinatesBean")
public class CoordinatesBean implements Parcelable // 与数据库不一致
{

    @Id
    public int id;
    public String uuid;
    public String lat;
    public String lng;
    public String registime;





    public void setRegistime(String registime)
    {
        this.registime = registime;
    }

    public String getRegistime()
    {
        return registime;
    }


    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLat()
    {
        return lat;
    }


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }




    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<CoordinatesBean> CREATOR = new Creator()
    {
        @Override
        public CoordinatesBean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            CoordinatesBean p = new CoordinatesBean();
            p.setId(source.readInt());
            p.setUuid(source.readString());
            p.setLat(source.readString());
            p.setLng(source.readString());
            p.setRegistime(source.readString());
            return p;
        }

        @Override
        public CoordinatesBean[] newArray(int size)
        {
            return new CoordinatesBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeInt(id);
        p.writeString(uuid);
        p.writeString(lat);
        p.writeString(lng);
        p.writeString(registime);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
