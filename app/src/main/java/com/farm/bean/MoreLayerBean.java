package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p>
 * Copyright: Copyright (c) 2015
 * <p>
 * Company: 广州海川信息科技有限公司
 * 地理坐标信息表:记录销售区域
 *
 * @version 1.0
 */
@Table(name = "MoreLayerBean")
public class MoreLayerBean implements Parcelable // 与数据库不一致
{

    @Id
    public int id;
    public String uuid;
    public String type;
    public String uid;
    public String lat;
    public String lng;
    public String note;
    public String xxzt;


    public void setXxzt(String xxzt)
    {
        this.xxzt = xxzt;
    }

    public String getXxzt()
    {
        return xxzt;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
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


    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
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

    public static final Creator<MoreLayerBean> CREATOR = new Creator()
    {
        @Override
        public MoreLayerBean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            MoreLayerBean p = new MoreLayerBean();
            p.setId(source.readInt());
            p.setUuid(source.readString());
            p.setType(source.readString());
            p.setUid(source.readString());
            p.setLat(source.readString());
            p.setLng(source.readString());
            p.setNote(source.readString());
            p.setXxzt(source.readString());

            return p;
        }

        @Override
        public MoreLayerBean[] newArray(int size)
        {
            return new MoreLayerBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeInt(id);
        p.writeString(uuid);
        p.writeString(type);
        p.writeString(uid);
        p.writeString(lat);
        p.writeString(lng);
        p.writeString(note);
        p.writeString(xxzt);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
