package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :sima
 * @version :1.0
 * @createTime：2015-8-5 下午3:04:57
 * @description :实体类
 */
public class DynamicEntity implements Parcelable
{
    String uuid;
    String title;
    String note;
    String date;
    String type;

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

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }


    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Parcelable.Creator<DynamicEntity> CREATOR = new Creator<DynamicEntity>()
    {
        @Override
        public DynamicEntity createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            DynamicEntity p = new DynamicEntity();
            p.setUuid(source.readString());
            p.setTitle(source.readString());
            p.setNote(source.readString());
            p.setDate(source.readString());
            p.setType(source.readString());
            return p;
        }

        @Override
        public DynamicEntity[] newArray(int size)
        {
            return new DynamicEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(uuid);
        p.writeString(title);
        p.writeString(note);
        p.writeString(date);
        p.writeString(type);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
